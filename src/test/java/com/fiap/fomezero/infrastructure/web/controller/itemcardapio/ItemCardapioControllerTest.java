package com.fiap.fomezero.infrastructure.web.controller.itemcardapio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.usecase.itemcardapio.AtualizarItemCardapioUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.BuscarItemCardapioPorIdUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.CriarItemCardapioUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.DeletarItemCardapioUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.ListarItensCardapioPorRestauranteUseCase;
import com.fiap.fomezero.exception.GlobalExceptionHandler;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import com.fiap.fomezero.infrastructure.web.controller.ItemCardapioController;
import com.fiap.fomezero.infrastructure.web.controller.helpers.ItemCardapioControllerTestHelper;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemCardapioControllerTest {

    private static final String BASE_URL = "/v1/itens-cardapio";

    private MockMvc mockMvc;

    @Mock
    private CriarItemCardapioUseCase criarItemCardapioUseCase;

    @Mock
    private ListarItensCardapioPorRestauranteUseCase listarItensCardapioPorRestauranteUseCase;

    @Mock
    private BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;

    @Mock
    private AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;

    @Mock
    private DeletarItemCardapioUseCase deletarItemCardapioUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);

        ItemCardapioController itemCardapioController = new ItemCardapioController(
                criarItemCardapioUseCase,
                listarItensCardapioPorRestauranteUseCase,
                buscarItemCardapioPorIdUseCase,
                atualizarItemCardapioUseCase,
                deletarItemCardapioUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(itemCardapioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("POST /v1/itens-cardapio deve retornar 201 ao criar item válido")
    void criarItemCardapio_dadosValidos_retorna201() throws Exception {
        // Arrange
        ItemCardapioCreateRequest request = ItemCardapioControllerTestHelper.buildCreateRequest(1L);

        when(criarItemCardapioUseCase.criarItemCardapio(any(ItemCardapioCreateRequest.class)))
                .thenReturn(ItemCardapioControllerTestHelper.buildResponse(1L));

        // Act + Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(criarItemCardapioUseCase).criarItemCardapio(any(ItemCardapioCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/itens-cardapio deve retornar 404 quando restaurante não existe")
    void criarItemCardapio_restauranteInexistente_retorna404() throws Exception {
        // Arrange
        ItemCardapioCreateRequest request = ItemCardapioControllerTestHelper.buildCreateRequest(999L);

        when(criarItemCardapioUseCase.criarItemCardapio(any(ItemCardapioCreateRequest.class)))
                .thenThrow(new RestauranteNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(criarItemCardapioUseCase).criarItemCardapio(any(ItemCardapioCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/itens-cardapio deve retornar 400 quando campos obrigatórios estão ausentes")
    void criarItemCardapio_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/restaurante/{restauranteId} deve retornar lista de itens")
    void listarPorRestaurante_restauranteExistente_retorna200() throws Exception {
        // Arrange
        when(listarItensCardapioPorRestauranteUseCase.listarPorRestaurante(1L))
                .thenReturn(List.of(ItemCardapioControllerTestHelper.buildResponse(1L)));

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/restaurante/1"))
                .andExpect(status().isOk());

        verify(listarItensCardapioPorRestauranteUseCase).listarPorRestaurante(1L);
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/restaurante/{restauranteId} deve retornar 404 quando restaurante não existe")
    void listarPorRestaurante_restauranteInexistente_retorna404() throws Exception {
        // Arrange
        when(listarItensCardapioPorRestauranteUseCase.listarPorRestaurante(999L))
                .thenThrow(new RestauranteNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/restaurante/999"))
                .andExpect(status().isNotFound());

        verify(listarItensCardapioPorRestauranteUseCase).listarPorRestaurante(999L);
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/{id} deve retornar item de cardápio")
    void buscarPorId_existente_retorna200() throws Exception {
        // Arrange
        when(buscarItemCardapioPorIdUseCase.buscarPorId(1L))
                .thenReturn(ItemCardapioControllerTestHelper.buildResponse(1L));

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk());

        verify(buscarItemCardapioPorIdUseCase).buscarPorId(1L);
    }

    @Test
    @DisplayName("GET /v1/itens-cardapio/{id} deve retornar 404 quando item não existe")
    void buscarPorId_inexistente_retorna404() throws Exception {
        // Arrange
        when(buscarItemCardapioPorIdUseCase.buscarPorId(999L))
                .thenThrow(new ItemCardapioNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(buscarItemCardapioPorIdUseCase).buscarPorId(999L);
    }

    @Test
    @DisplayName("PUT /v1/itens-cardapio/{id} deve retornar 200 ao atualizar item existente")
    void atualizarItemCardapio_dadosValidos_retorna200() throws Exception {
        // Arrange
        ItemCardapioUpdateRequest request = ItemCardapioControllerTestHelper.buildUpdateRequest();

        when(atualizarItemCardapioUseCase.atualizarItemCardapio(eq(1L), any(ItemCardapioUpdateRequest.class)))
                .thenReturn(ItemCardapioControllerTestHelper.buildResponse(1L));

        // Act + Assert
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());

        verify(atualizarItemCardapioUseCase)
                .atualizarItemCardapio(eq(1L), any(ItemCardapioUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /v1/itens-cardapio/{id} deve retornar 404 quando item não existe")
    void atualizarItemCardapio_itemInexistente_retorna404() throws Exception {
        // Arrange
        ItemCardapioUpdateRequest request = ItemCardapioControllerTestHelper.buildUpdateRequest();

        when(atualizarItemCardapioUseCase.atualizarItemCardapio(eq(999L), any(ItemCardapioUpdateRequest.class)))
                .thenThrow(new ItemCardapioNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(put(BASE_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(atualizarItemCardapioUseCase)
                .atualizarItemCardapio(eq(999L), any(ItemCardapioUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /v1/itens-cardapio/{id} deve retornar 404 quando novo restaurante não existe")
    void atualizarItemCardapio_restauranteInexistente_retorna404() throws Exception {
        // Arrange
        ItemCardapioUpdateRequest request = ItemCardapioControllerTestHelper.buildUpdateRequestComRestaurante(999L);

        when(atualizarItemCardapioUseCase.atualizarItemCardapio(eq(1L), any(ItemCardapioUpdateRequest.class)))
                .thenThrow(new RestauranteNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(atualizarItemCardapioUseCase)
                .atualizarItemCardapio(eq(1L), any(ItemCardapioUpdateRequest.class));
    }

    @Test
    @DisplayName("DELETE /v1/itens-cardapio/{id} deve retornar 204")
    void deletarItemCardapio_existente_retorna204() throws Exception {
        // Arrange
        doNothing().when(deletarItemCardapioUseCase).deletarItemCardapio(1L);

        // Act + Assert
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(deletarItemCardapioUseCase).deletarItemCardapio(1L);
    }

    @Test
    @DisplayName("DELETE /v1/itens-cardapio/{id} deve retornar 404 quando item não existe")
    void deletarItemCardapio_inexistente_retorna404() throws Exception {
        // Arrange
        doThrow(new ItemCardapioNaoEncontradoException())
                .when(deletarItemCardapioUseCase).deletarItemCardapio(999L);

        // Act + Assert
        mockMvc.perform(delete(BASE_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(deletarItemCardapioUseCase).deletarItemCardapio(999L);
    }
}