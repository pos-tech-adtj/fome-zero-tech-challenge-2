package com.fiap.fomezero.infrastructure.web.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fomezero.application.dto.request.LoginRequest;
import com.fiap.fomezero.application.usecase.auth.AutenticarUsuarioUseCase;
import com.fiap.fomezero.exception.CredenciaisInvalidasException;
import com.fiap.fomezero.exception.GlobalExceptionHandler;
import com.fiap.fomezero.infrastructure.web.controller.AuthController;
import com.fiap.fomezero.infrastructure.web.controller.helpers.AuthControllerTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private static final String BASE_URL = "/v1/auth";

    private MockMvc mockMvc;

    @Mock
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);

        AuthController authController = new AuthController(autenticarUsuarioUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
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

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 200 com credenciais válidas")
    void login_credenciaisValidas_retorna200() throws Exception {
        // Arrange
        LoginRequest request = AuthControllerTestHelper.buildLoginRequest();

        when(autenticarUsuarioUseCase.autenticarUsuario(any(LoginRequest.class)))
                .thenReturn(AuthControllerTestHelper.buildLoginResponse());

        // Act + Assert
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(autenticarUsuarioUseCase).autenticarUsuario(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 401 quando credenciais são inválidas")
    void login_credenciaisInvalidas_retorna401() throws Exception {
        // Arrange
        LoginRequest request = AuthControllerTestHelper.buildLoginRequest();

        when(autenticarUsuarioUseCase.autenticarUsuario(any(LoginRequest.class)))
                .thenThrow(new CredenciaisInvalidasException());

        // Act + Assert
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized());

        verify(autenticarUsuarioUseCase).autenticarUsuario(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 400 quando campos obrigatórios estão ausentes")
    void login_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}