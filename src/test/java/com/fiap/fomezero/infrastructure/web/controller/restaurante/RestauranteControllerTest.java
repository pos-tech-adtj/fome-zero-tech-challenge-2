package com.fiap.fomezero.infrastructure.web.controller.restaurante;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.EnderecoResponse;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.application.usecase.restaurante.*;
import com.fiap.fomezero.infrastructure.web.controller.RestauranteController;
import com.fiap.fomezero.infrastructure.web.controller.helpers.RestauranteControllerTestHelper;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.exception.GlobalExceptionHandler;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import com.fiap.fomezero.exception.UsuarioNaoEDonoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestauranteControllerTest {

    private static final String BASE_URL = "/v1/restaurantes";

    private MockMvc mockMvc;

    @Mock
    private CriarRestauranteUseCase criarRestauranteUseCase;

    @Mock
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @Mock
    private BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    @Mock
    private ListarRestaurantesUseCase listarRestaurantesUseCase;

    @Mock
    private DeletarRestauranteUseCase deletarRestauranteUseCase;

    AutoCloseable mock;

    private Usuario dono;
    private Usuario cliente;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);

        RestauranteController restauranteController = new RestauranteController(
                atualizarRestauranteUseCase,
                buscarRestaurantePorIdUseCase,
                criarRestauranteUseCase,
                deletarRestauranteUseCase,
                listarRestaurantesUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();

        dono = RestauranteControllerTestHelper.buildDono();

        cliente = RestauranteControllerTestHelper.buildCliente();
    }


    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 201 ao criar restaurante válido")
    void criarRestaurante_dadosValidos_retorna201() throws Exception {
        // Arrange
        var restauranteCreateRequest = RestauranteControllerTestHelper.buildCreateRequest(dono.getId());
        when(criarRestauranteUseCase.criarRestaurante(any(RestauranteCreateRequest.class)))
                .thenReturn(buildResponse());

        // Act + Assert
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(restauranteCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(criarRestauranteUseCase, times(1)).criarRestaurante(any(RestauranteCreateRequest.class));

    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    private RestauranteResponse buildResponse() {
        return RestauranteResponse.builder()
                .id(1L)
                .nome("Sukiya")
                .tipoCozinha("Japonesa")
                .horarioFuncionamento("11h as 23h")
                .donoId(dono.getId())
                .endereco(buildEnderecoResponse())
                .build();
    }

    private EnderecoResponse buildEnderecoResponse() {
        return EnderecoResponse.builder()
                .rua("Rua dos Testes")
                .numero(100)
                .complemento("Sala 1")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01300-000")
                .build();
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 404 quando donoId não existe")
    void criarRestaurante_donoIdInexistente_retorna404() throws Exception {

        RestauranteCreateRequest request = RestauranteControllerTestHelper.buildCreateRequest(99999L);

        when(criarRestauranteUseCase.criarRestaurante(any(RestauranteCreateRequest.class)))
                .thenThrow(new UsuarioNaoEncontradoException());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(criarRestauranteUseCase).criarRestaurante(any(RestauranteCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 422 quando usuário não é dono")
    void criarRestaurante_usuarioNaoEDono_retorna422() throws Exception {

        RestauranteCreateRequest request = RestauranteControllerTestHelper.buildCreateRequest(cliente.getId());

        when(criarRestauranteUseCase.criarRestaurante(any(RestauranteCreateRequest.class)))
                .thenThrow(new UsuarioNaoEDonoException());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnprocessableEntity());

        verify(criarRestauranteUseCase).criarRestaurante(any(RestauranteCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 400 quando campos obrigatórios estão ausentes")
    void criarRestaurante_camposObrigatoriosAusentes_retorna400() throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /v1/restaurantes deve retornar lista de restaurantes")
    void listarRestaurantes_retorna200ComLista() throws Exception {
        RestauranteResponse restaurante1 = buildResponse();
        RestauranteResponse restaurante2 = RestauranteResponse.builder()
                .id(2L)
                .nome("McDonalds")
                .tipoCozinha("Fast Food")
                .horarioFuncionamento("24h")
                .donoId(dono.getId())
                .endereco(buildEnderecoResponse())
                .build();

        when(listarRestaurantesUseCase.listarRestaurantes())
                .thenReturn(List.of(restaurante1, restaurante2));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());

        verify(listarRestaurantesUseCase).listarRestaurantes();
    }

    @Test
    @DisplayName("GET /v1/restaurantes deve retornar lista vazia")
    void listarRestaurantes_semRestaurantes_retornaListaVazia() throws Exception {

        when(listarRestaurantesUseCase.listarRestaurantes())
                .thenReturn(List.of());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());

        verify(listarRestaurantesUseCase).listarRestaurantes();
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar restaurante")
    void buscarRestaurantePorId_existente_retorna200() throws Exception {

        when(buscarRestaurantePorIdUseCase.buscarPorId(1L))
                .thenReturn(buildResponse());

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk());

        verify(buscarRestaurantePorIdUseCase).buscarPorId(1L);
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar 404")
    void buscarRestaurantePorId_inexistente_retorna404() throws Exception {

        when(buscarRestaurantePorIdUseCase.buscarPorId(999L))
                .thenThrow(new RestauranteNaoEncontradoException());

        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(buscarRestaurantePorIdUseCase).buscarPorId(999L);
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornar 200 ao atualizar restaurante existente")
    void atualizarRestaurante_dadosValidos_retorna200() throws Exception {

        RestauranteUpdateRequest request = RestauranteControllerTestHelper.buildUpdateRequest(dono.getId());

        when(atualizarRestauranteUseCase.atualizarRestaurante(eq(1L), any(RestauranteUpdateRequest.class)))
                .thenReturn(buildResponse());

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());

        verify(atualizarRestauranteUseCase)
                .atualizarRestaurante(eq(1L), any(RestauranteUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornar 404 quando restaurante não existe")
    void atualizarRestaurante_restauranteInexistente_retorna404() throws Exception {

        RestauranteUpdateRequest request = RestauranteControllerTestHelper.buildUpdateRequest(dono.getId());

        when(atualizarRestauranteUseCase.atualizarRestaurante(eq(999L), any(RestauranteUpdateRequest.class)))
                .thenThrow(new RestauranteNaoEncontradoException());

        mockMvc.perform(put(BASE_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(atualizarRestauranteUseCase)
                .atualizarRestaurante(eq(999L), any(RestauranteUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornar 422 quando novo dono não é dono de restaurante")
    void atualizarRestaurante_novoDonoNaoEDono_retorna422() throws Exception {

        RestauranteUpdateRequest request = RestauranteControllerTestHelper.buildUpdateRequest(cliente.getId());

        when(atualizarRestauranteUseCase.atualizarRestaurante(eq(1L), any(RestauranteUpdateRequest.class)))
                .thenThrow(new UsuarioNaoEDonoException());

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnprocessableEntity());

        verify(atualizarRestauranteUseCase)
                .atualizarRestaurante(eq(1L), any(RestauranteUpdateRequest.class));
    }
    @Test
    @DisplayName("DELETE /v1/restaurantes/{id} deve retornar 204")
    void deletarRestaurante_existente_retorna204() throws Exception {

        doNothing().when(deletarRestauranteUseCase)
                .deletarRestaurante(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(deletarRestauranteUseCase)
                .deletarRestaurante(1L);
    }

    @Test
    @DisplayName("DELETE /v1/restaurantes/{id} deve retornar 404 quando restaurante não existe")
    void deletarRestaurante_inexistente_retorna404() throws Exception {

        doThrow(new RestauranteNaoEncontradoException())
                .when(deletarRestauranteUseCase)
                .deletarRestaurante(999L);

        mockMvc.perform(delete(BASE_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(deletarRestauranteUseCase)
                .deletarRestaurante(999L);
    }
}