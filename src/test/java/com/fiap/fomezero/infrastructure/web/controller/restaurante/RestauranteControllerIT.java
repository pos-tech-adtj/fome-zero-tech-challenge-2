package com.fiap.fomezero.infrastructure.web.controller.restaurante;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import tools.jackson.databind.ObjectMapper;
import com.fiap.fomezero.application.dto.request.EnderecoRequest;
import com.fiap.fomezero.application.dto.request.EnderecoUpdateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.domain.model.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class RestauranteControllerIT {

    private static final String BASE_URL = "/v1/restaurantes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Usuario dono;
    private Usuario cliente;

    @BeforeEach
    void setUp() {

        dono = usuarioRepository.save(Usuario.builder()
                .nome("Dono Restaurante")
                .email("dono@email.com")
                .login("dono")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .build());

        cliente = usuarioRepository.save(Usuario.builder()
                .nome("Cliente Comum")
                .email("cliente@email.com")
                .login("cliente")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .build());
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 201 ao criar restaurante válido")
    void criarRestaurante_dadosValidos_retorna201() throws Exception {
        RestauranteCreateRequest request = buildCreateRequest(dono.getId());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Sukiya"))
                .andExpect(jsonPath("$.tipoCozinha").value("Japonesa"))
                .andExpect(jsonPath("$.horarioFuncionamento").value("11h as 23h"))
                .andExpect(jsonPath("$.donoId").value(dono.getId()));
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 404 quando donoId não existe")
    void criarRestaurante_donoIdInexistente_retorna404() throws Exception {
        RestauranteCreateRequest request = buildCreateRequest(99999L);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 422 quando usuario existe mas não é DONO_RESTAURANTE")
    void criarRestaurante_usuarionãoEDono_retorna422() throws Exception {
        RestauranteCreateRequest request = buildCreateRequest(cliente.getId());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Usuário não é dono de restaurante"));
    }

    @Test
@DisplayName("POST /v1/restaurantes deve retornar 400 quando campos obrigatórios estão ausentes")
    void criarRestaurante_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("GET /v1/restaurantes deve retornar 200 com lista de restaurantes")
    void listarRestaurantes_retorna200ComLista() throws Exception {
        salvarRestaurante("Pizzaria Teste", dono);
        salvarRestaurante("Hamburgueria Teste", dono);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].nome", hasItems("Pizzaria Teste", "Hamburgueria Teste")));
    }

    @Test
    @DisplayName("GET /v1/restaurantes deve retornar 200 com lista vazia quando não ha restaurantes")
    void listarRestaurantes_semRestaurantes_retornaListaVazia() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar 200 quando restaurante existe")
    void buscarRestaurantePorId_existente_retorna200() throws Exception {
        Restaurante restaurante = salvarRestaurante("Trattoria Test", dono);

        mockMvc.perform(get(BASE_URL + "/{id}", restaurante.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurante.getId()))
                .andExpect(jsonPath("$.nome").value("Trattoria Test"))
                .andExpect(jsonPath("$.donoId").value(dono.getId()));
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar 404 quando restaurante não existe")
    void buscarRestaurantePorId_inexistente_retorna404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Restaurante não encontrado"));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornar 200 ao atualizar restaurante existente")
    void atualizarRestaurante_dadosValidos_retorna200() throws Exception {
        Restaurante restaurante = salvarRestaurante("Nome Original", dono);

        RestauranteUpdateRequest update = new RestauranteUpdateRequest(
                "Nome Atualizado",
                buildEnderecoUpdateRequest(),
                "Japonesa",
                "12h as 22h",
                dono.getId()
        );

        mockMvc.perform(put(BASE_URL + "/{id}", restaurante.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.tipoCozinha").value("Japonesa"));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornar 404 quando restaurante não existe")
    void atualizarRestaurante_restauranteInexistente_retorna404() throws Exception {
        RestauranteUpdateRequest update = new RestauranteUpdateRequest(
                "Qualquer Nome",
                buildEnderecoUpdateRequest(),
                "Francesa",
                "10h as 20h",
                dono.getId()
        );

        mockMvc.perform(put(BASE_URL + "/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Restaurante não encontrado"));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornargit  422 ao trocar dono para usuário que não é DONO_RESTAURANTE")
    void atualizarRestaurante_novoDonanãoEDono_retorna422() throws Exception {
        Restaurante restaurante = salvarRestaurante("Restaurante X", dono);

        RestauranteUpdateRequest update = new RestauranteUpdateRequest(
                "Restaurante X",
                buildEnderecoUpdateRequest(),
                "Japonesa",
                "11h as 23h",
                cliente.getId()
        );

        mockMvc.perform(put(BASE_URL + "/{id}", restaurante.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Usuário não é dono de restaurante"));
    }

    @Test
    @DisplayName("DELETE /v1/restaurantes/{id} deve retornar 204 ao deletar restaurante existente")
    void deletarRestaurante_existente_retorna204() throws Exception {
        Restaurante restaurante = salvarRestaurante("A Ser Deletado", dono);

        mockMvc.perform(delete(BASE_URL + "/{id}", restaurante.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/{id}", restaurante.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /v1/restaurantes/{id} deve retornar 404 quando restaurante não existe")
    void deletarRestaurante_inexistente_retorna404() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Restaurante não encontrado"));
    }

    private RestauranteCreateRequest buildCreateRequest(Long donoId) {
        return new RestauranteCreateRequest(
                "Sukiya",
                buildEnderecoRequest(),
                "Japonesa",
                "11h as 23h",
                donoId
        );
    }

    private EnderecoRequest buildEnderecoRequest() {
        return new EnderecoRequest(
                "Rua dos Testes",
                100,
                "Sala 1",
                "Centro",
                "Sao Paulo",
                "SP",
                "01310-100"
        );
    }

    private EnderecoUpdateRequest buildEnderecoUpdateRequest() {
        return new EnderecoUpdateRequest(
                "Rua dos Testes",
                100,
                "Sala 1",
                "Centro",
                "Sao Paulo",
                "SP",
                "01310-100"
        );
    }

    private Restaurante salvarRestaurante(String nome, Usuario dono) {
        Endereco endereco = Endereco.builder()
                .rua("Rua Teste")
                .numero(1)
                .cidade("Sao Paulo")
                .estado("SP")
                .cep("01310-100")
                .build();

        return restauranteRepository.save(Restaurante.builder()
                .nome(nome)
                .tipoCozinha("Variada")
                .horarioFuncionamento("11h as 23h")
                .dono(dono)
                .endereco(endereco)
                .build());
    }
}