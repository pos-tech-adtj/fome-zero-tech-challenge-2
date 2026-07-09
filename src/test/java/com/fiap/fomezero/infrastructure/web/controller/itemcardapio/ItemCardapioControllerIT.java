package com.fiap.fomezero.infrastructure.web.controller.itemcardapio;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.infrastructure.web.controller.helpers.ItemCardapioControllerTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ItemCardapioControllerIT {

    private static final String BASE_URL = "/v1/itens-cardapio";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        Usuario dono = usuarioRepository.save(Usuario.builder()
                .nome("Dono Restaurante")
                .email("dono@email.com")
                .login("dono")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .build());

        restaurante = restauranteRepository.save(Restaurante.builder()
                .nome("Cantina Fiap")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("11h as 23h")
                .dono(dono)
                .build());
    }

    private ItemCardapio salvarItem(String nome) {
        return itemCardapioRepository.save(ItemCardapio.builder()
                .nome(nome)
                .descricao("Descrição de teste")
                .preco(BigDecimal.valueOf(29.90))
                .apenasNoRestaurante(false)
                .restaurante(restaurante)
                .build());
    }

    @Test
    @DisplayName("POST /v1/itens-cardapio deve retornar 201 ao criar item válido")
    void criarItemCardapio_dadosValidos_retorna201() throws Exception {
        ItemCardapioCreateRequest request = ItemCardapioControllerTestHelper.buildCreateRequest(restaurante.getId());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$.restauranteId").value(restaurante.getId()))
                .andExpect(jsonPath("$.restauranteNome").value("Cantina Fiap"));
    }

    @Test
    @DisplayName("POST /v1/itens-cardapio deve retornar 404 quando restaurante não existe")
    void criarItemCardapio_restauranteInexistente_retorna404() throws Exception {
        ItemCardapioCreateRequest request = ItemCardapioControllerTestHelper.buildCreateRequest(99999L);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Restaurante não encontrado"));
    }

    @Test
    @DisplayName("POST /v1/itens-cardapio deve retornar 400 quando campos obrigatórios estão ausentes")
    void criarItemCardapio_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/restaurante/{restauranteId} deve retornar 200 com lista de itens")
    void listarPorRestaurante_comItens_retorna200ComLista() throws Exception {
        salvarItem("Pizza Calabresa");
        salvarItem("Refrigerante");

        mockMvc.perform(get(BASE_URL + "/restaurante/{restauranteId}", restaurante.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nome", hasItems("Pizza Calabresa", "Refrigerante")));
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/restaurante/{restauranteId} deve retornar 200 com lista vazia quando não há itens")
    void listarPorRestaurante_semItens_retornaListaVazia() throws Exception {
        mockMvc.perform(get(BASE_URL + "/restaurante/{restauranteId}", restaurante.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/restaurante/{restauranteId} deve retornar 404 quando restaurante não existe")
    void listarPorRestaurante_restauranteInexistente_retorna404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/restaurante/{restauranteId}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Restaurante não encontrado"));
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/{id} deve retornar 200 quando item existe")
    void buscarPorId_existente_retorna200() throws Exception {
        ItemCardapio item = salvarItem("Pizza Portuguesa");

        mockMvc.perform(get(BASE_URL + "/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.nome").value("Pizza Portuguesa"));
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/{id} deve retornar 404 quando item não existe")
    void buscarPorId_inexistente_retorna404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Item de cardápio não encontrado"));
    }

    @Test
    @DisplayName("PUT /v1/itens-cardapio/{id} deve retornar 200 ao atualizar item existente")
    void atualizarItemCardapio_dadosValidos_retorna200() throws Exception {
        ItemCardapio item = salvarItem("Nome Original");
        ItemCardapioUpdateRequest update = ItemCardapioControllerTestHelper.buildUpdateRequest();

        mockMvc.perform(put(BASE_URL + "/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pizza Margherita Grande"));
    }

    @Test
    @DisplayName("PUT /v1/itens-cardapio/{id} deve retornar 404 quando item não existe")
    void atualizarItemCardapio_itemInexistente_retorna404() throws Exception {
        ItemCardapioUpdateRequest update = ItemCardapioControllerTestHelper.buildUpdateRequest();

        mockMvc.perform(put(BASE_URL + "/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Item de cardápio não encontrado"));
    }

    @Test
    @DisplayName("PUT /v1/itens-cardapio/{id} deve retornar 404 quando novo restaurante não existe")
    void atualizarItemCardapio_restauranteInexistente_retorna404() throws Exception {
        ItemCardapio item = salvarItem("Nome Original");
        ItemCardapioUpdateRequest update = ItemCardapioControllerTestHelper.buildUpdateRequestComRestaurante(99999L);

        mockMvc.perform(put(BASE_URL + "/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Restaurante não encontrado"));
    }

    @Test
    @DisplayName("DELETE /v1/itens-cardapio/{id} deve retornar 204 ao deletar item existente")
    void deletarItemCardapio_existente_retorna204() throws Exception {
        ItemCardapio item = salvarItem("A Ser Deletado");

        mockMvc.perform(delete(BASE_URL + "/{id}", item.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/{id}", item.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /v1/itens-cardapio/{id} deve retornar 404 quando item não existe")
    void deletarItemCardapio_inexistente_retorna404() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Item de cardápio não encontrado"));
    }
}