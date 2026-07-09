package com.fiap.fomezero.infrastructure.web.controller.usuario;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.usecase.usuario.AlterarSenhaUseCase;
import com.fiap.fomezero.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.fiap.fomezero.application.usecase.usuario.BuscarUsuarioUseCase;
import com.fiap.fomezero.application.usecase.usuario.CriarUsuarioUseCase;
import com.fiap.fomezero.application.usecase.usuario.DeletarUsuarioUseCase;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.GlobalExceptionHandler;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.exception.SenhaAtualInvalidaException;
import com.fiap.fomezero.exception.SenhaIgualAtualException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import com.fiap.fomezero.infrastructure.web.controller.UsuarioController;
import com.fiap.fomezero.infrastructure.web.controller.helpers.UsuarioControllerTestHelper;
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

class UsuarioControllerTest {

    private static final String BASE_URL = "/v1/usuarios";

    private MockMvc mockMvc;

    @Mock
    private CriarUsuarioUseCase criarUsuarioUseCase;

    @Mock
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @Mock
    private DeletarUsuarioUseCase deletarUsuarioUseCase;

    @Mock
    private BuscarUsuarioUseCase buscarUsuarioUseCase;

    @Mock
    private AlterarSenhaUseCase alterarSenhaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);

        UsuarioController usuarioController = new UsuarioController(
                criarUsuarioUseCase,
                atualizarUsuarioUseCase,
                deletarUsuarioUseCase,
                buscarUsuarioUseCase,
                alterarSenhaUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
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
    @DisplayName("POST /v1/usuarios deve retornar 201 ao criar usuário válido")
    void criarUsuario_dadosValidos_retorna201() throws Exception {
        // Arrange
        UsuarioCreateRequest request = UsuarioControllerTestHelper.buildCreateRequest();

        when(criarUsuarioUseCase.criarUsuario(any(UsuarioCreateRequest.class)))
                .thenReturn(UsuarioControllerTestHelper.buildResponse());

        // Act + Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(criarUsuarioUseCase).criarUsuario(any(UsuarioCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 409 quando email já está cadastrado")
    void criarUsuario_emailJaCadastrado_retorna409() throws Exception {
        // Arrange
        UsuarioCreateRequest request = UsuarioControllerTestHelper.buildCreateRequest();

        when(criarUsuarioUseCase.criarUsuario(any(UsuarioCreateRequest.class)))
                .thenThrow(new EmailJaCadastradoException());

        // Act + Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isConflict());

        verify(criarUsuarioUseCase).criarUsuario(any(UsuarioCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 409 quando login já está cadastrado")
    void criarUsuario_loginJaCadastrado_retorna409() throws Exception {
        // Arrange
        UsuarioCreateRequest request = UsuarioControllerTestHelper.buildCreateRequest();

        when(criarUsuarioUseCase.criarUsuario(any(UsuarioCreateRequest.class)))
                .thenThrow(new LoginJaCadastradoException());

        // Act + Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isConflict());

        verify(criarUsuarioUseCase).criarUsuario(any(UsuarioCreateRequest.class));
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 400 quando campos obrigatórios estão ausentes")
    void criarUsuario_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /v1/usuarios/{id} deve retornar 204")
    void deletarUsuario_existente_retorna204() throws Exception {
        // Arrange
        doNothing().when(deletarUsuarioUseCase).deletarUsuario(1L);

        // Act + Assert
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(deletarUsuarioUseCase).deletarUsuario(1L);
    }

    @Test
    @DisplayName("DELETE /v1/usuarios/{id} deve retornar 404 quando usuário não existe")
    void deletarUsuario_inexistente_retorna404() throws Exception {
        // Arrange
        doThrow(new UsuarioNaoEncontradoException())
                .when(deletarUsuarioUseCase).deletarUsuario(999L);

        // Act + Assert
        mockMvc.perform(delete(BASE_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(deletarUsuarioUseCase).deletarUsuario(999L);
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 200 ao atualizar usuário existente")
    void atualizarUsuario_dadosValidos_retorna200() throws Exception {
        // Arrange
        UsuarioUpdateRequest request = UsuarioControllerTestHelper.buildUpdateRequest();

        when(atualizarUsuarioUseCase.atualizarUsuario(eq(1L), any(UsuarioUpdateRequest.class)))
                .thenReturn(UsuarioControllerTestHelper.buildResponse());

        // Act + Assert
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());

        verify(atualizarUsuarioUseCase)
                .atualizarUsuario(eq(1L), any(UsuarioUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 404 quando usuário não existe")
    void atualizarUsuario_inexistente_retorna404() throws Exception {
        // Arrange
        UsuarioUpdateRequest request = UsuarioControllerTestHelper.buildUpdateRequest();

        when(atualizarUsuarioUseCase.atualizarUsuario(eq(999L), any(UsuarioUpdateRequest.class)))
                .thenThrow(new UsuarioNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(put(BASE_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(atualizarUsuarioUseCase)
                .atualizarUsuario(eq(999L), any(UsuarioUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 409 quando novo email já está cadastrado")
    void atualizarUsuario_emailJaCadastrado_retorna409() throws Exception {
        // Arrange
        UsuarioUpdateRequest request = UsuarioControllerTestHelper.buildUpdateRequest();

        when(atualizarUsuarioUseCase.atualizarUsuario(eq(1L), any(UsuarioUpdateRequest.class)))
                .thenThrow(new EmailJaCadastradoException());

        // Act + Assert
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isConflict());

        verify(atualizarUsuarioUseCase)
                .atualizarUsuario(eq(1L), any(UsuarioUpdateRequest.class));
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar usuário")
    void buscarUsuarioPorId_existente_retorna200() throws Exception {
        // Arrange
        when(buscarUsuarioUseCase.buscarUsuarioPorId(1L))
                .thenReturn(UsuarioControllerTestHelper.buildResponse());

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk());

        verify(buscarUsuarioUseCase).buscarUsuarioPorId(1L);
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar 404 quando usuário não existe")
    void buscarUsuarioPorId_inexistente_retorna404() throws Exception {
        // Arrange
        when(buscarUsuarioUseCase.buscarUsuarioPorId(999L))
                .thenThrow(new UsuarioNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(buscarUsuarioUseCase).buscarUsuarioPorId(999L);
    }

    @Test
    @DisplayName("GET /v1/usuarios deve retornar lista de usuários")
    void listarUsuarios_retorna200ComLista() throws Exception {
        // Arrange
        when(buscarUsuarioUseCase.listarTodosUsuarios())
                .thenReturn(List.of(UsuarioControllerTestHelper.buildResponse()));

        // Act + Assert
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());

        verify(buscarUsuarioUseCase).listarTodosUsuarios();
    }

    @Test
    @DisplayName("GET /v1/usuarios deve retornar lista vazia")
    void listarUsuarios_semUsuarios_retornaListaVazia() throws Exception {
        // Arrange
        when(buscarUsuarioUseCase.listarTodosUsuarios())
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());

        verify(buscarUsuarioUseCase).listarTodosUsuarios();
    }

    @Test
    @DisplayName("GET /v1/usuarios/nome/{nome} deve retornar usuários encontrados")
    void buscarUsuariosPorNome_existente_retorna200() throws Exception {
        // Arrange
        when(buscarUsuarioUseCase.buscarUsuariosPorNome("João da Silva"))
                .thenReturn(List.of(UsuarioControllerTestHelper.buildResponse()));

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/nome/{nome}", "João da Silva"))
                .andExpect(status().isOk());

        verify(buscarUsuarioUseCase).buscarUsuariosPorNome("João da Silva");
    }

    @Test
    @DisplayName("GET /v1/usuarios/nome/{nome} deve retornar 404 quando nenhum usuário é encontrado")
    void buscarUsuariosPorNome_inexistente_retorna404() throws Exception {
        // Arrange
        when(buscarUsuarioUseCase.buscarUsuariosPorNome("Inexistente"))
                .thenThrow(new UsuarioNaoEncontradoException());

        // Act + Assert
        mockMvc.perform(get(BASE_URL + "/nome/{nome}", "Inexistente"))
                .andExpect(status().isNotFound());

        verify(buscarUsuarioUseCase).buscarUsuariosPorNome("Inexistente");
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 204 ao alterar senha com sucesso")
    void alterarSenha_dadosValidos_retorna204() throws Exception {
        // Arrange
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequest();

        doNothing().when(alterarSenhaUseCase).alterarSenha(eq(1L), any(UsuarioSenhaRequest.class));

        // Act + Assert
        mockMvc.perform(patch(BASE_URL + "/1/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNoContent());

        verify(alterarSenhaUseCase).alterarSenha(eq(1L), any(UsuarioSenhaRequest.class));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 404 quando usuário não existe")
    void alterarSenha_usuarioInexistente_retorna404() throws Exception {
        // Arrange
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequest();

        doThrow(new UsuarioNaoEncontradoException())
                .when(alterarSenhaUseCase).alterarSenha(eq(999L), any(UsuarioSenhaRequest.class));

        // Act + Assert
        mockMvc.perform(patch(BASE_URL + "/999/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound());

        verify(alterarSenhaUseCase).alterarSenha(eq(999L), any(UsuarioSenhaRequest.class));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 422 quando senha atual é inválida")
    void alterarSenha_senhaAtualInvalida_retorna422() throws Exception {
        // Arrange
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequestComSenhaAtualInvalida();

        doThrow(new SenhaAtualInvalidaException())
                .when(alterarSenhaUseCase).alterarSenha(eq(1L), any(UsuarioSenhaRequest.class));

        // Act + Assert
        mockMvc.perform(patch(BASE_URL + "/1/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnprocessableEntity());

        verify(alterarSenhaUseCase).alterarSenha(eq(1L), any(UsuarioSenhaRequest.class));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 422 quando nova senha é igual à atual")
    void alterarSenha_novaSenhaIgualAtual_retorna422() throws Exception {
        // Arrange
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequestComNovaSenhaIgualAtual();

        doThrow(new SenhaIgualAtualException())
                .when(alterarSenhaUseCase).alterarSenha(eq(1L), any(UsuarioSenhaRequest.class));

        // Act + Assert
        mockMvc.perform(patch(BASE_URL + "/1/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnprocessableEntity());

        verify(alterarSenhaUseCase).alterarSenha(eq(1L), any(UsuarioSenhaRequest.class));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 400 quando campos obrigatórios estão ausentes")
    void alterarSenha_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/1/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}