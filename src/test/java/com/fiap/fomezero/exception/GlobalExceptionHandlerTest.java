package com.fiap.fomezero.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("EmailJaCadastradoException deve retornar 409 CONFLICT")
    void emailJaCadastradoDeveRetornarConflict() {
        ProblemDetail problem = handler.emailJaCadastrado(new EmailJaCadastradoException());

        assertEquals(HttpStatus.CONFLICT.value(), problem.getStatus());
        assertEquals("Email já cadastrado", problem.getTitle());
        assertNotNull(problem.getDetail());
    }

    @Test
    @DisplayName("LoginJaCadastradoException deve retornar 409 CONFLICT")
    void loginJaCadastradoDeveRetornarConflict() {
        ProblemDetail problem = handler.loginJaCadastrado(new LoginJaCadastradoException());

        assertEquals(HttpStatus.CONFLICT.value(), problem.getStatus());
        assertEquals("Login já cadastrado", problem.getTitle());
    }

    @Test
    @DisplayName("UsuarioNaoEncontradoException deve retornar 404 NOT_FOUND")
    void usuarioNaoEncontradoDeveRetornarNotFound() {
        ProblemDetail problem = handler.usuarioNaoEncontrado(new UsuarioNaoEncontradoException());

        assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
        assertEquals("Usuário não encontrado", problem.getTitle());
    }

    @Test
    @DisplayName("UsuarioNaoEDonoException deve retornar 422 UNPROCESSABLE_ENTITY")
    void usuarioNaoEDonoDeveRetornarUnprocessableEntity() {
        ProblemDetail problem = handler.usuarioNaoEDono(new UsuarioNaoEDonoException());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problem.getStatus());
        assertEquals("Usuário não é dono de restaurante", problem.getTitle());
    }

    @Test
    @DisplayName("RestauranteNaoEncontradoException deve retornar 404 NOT_FOUND")
    void restauranteNaoEncontradoDeveRetornarNotFound() {
        ProblemDetail problem = handler.restauranteNaoEncontrado(new RestauranteNaoEncontradoException());

        assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
        assertEquals("Restaurante não encontrado", problem.getTitle());
    }

    @Test
    @DisplayName("ItemCardapioNaoEncontradoException deve retornar 404 NOT_FOUND")
    void itemCardapioNaoEncontradoDeveRetornarNotFound() {
        ProblemDetail problem = handler.itemCardapioNaoEncontrado(new ItemCardapioNaoEncontradoException());

        assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
        assertEquals("Item de cardápio não encontrado", problem.getTitle());
    }

    @Test
    @DisplayName("CredenciaisInvalidasException deve retornar 401 UNAUTHORIZED")
    void credenciaisInvalidasDeveRetornarUnauthorized() {
        ProblemDetail problem = handler.credenciaisInvalidas(new CredenciaisInvalidasException());

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
        assertEquals("Credenciais inválidas", problem.getTitle());
    }

    @Test
    @DisplayName("SenhaAtualInvalidaException deve retornar 422 UNPROCESSABLE_ENTITY")
    void senhaAtualInvalidaDeveRetornarUnprocessableEntity() {
        ProblemDetail problem = handler.senhaAtualInvalida(new SenhaAtualInvalidaException());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problem.getStatus());
        assertEquals("Senha atual inválida", problem.getTitle());
    }

    @Test
    @DisplayName("SenhaIgualAtualException deve retornar 422 UNPROCESSABLE_ENTITY")
    void senhaIgualAtualDeveRetornarUnprocessableEntity() {
        ProblemDetail problem = handler.senhaIgualAtual(new SenhaIgualAtualException());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problem.getStatus());
        assertEquals("Nova senha igual à atual", problem.getTitle());
    }

    @Test
    @DisplayName("BadCredentialsException deve retornar 401 UNAUTHORIZED")
    void badCredentialsDeveRetornarUnauthorized() {
        ProblemDetail problem = handler.badCredentials(new BadCredentialsException("Credenciais inválidas"));

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
        assertEquals("Credenciais inválidas", problem.getTitle());
        assertEquals("Credenciais inválidas", problem.getDetail());
    }

    @Test
    @DisplayName("Exception genérica deve retornar 400 BAD_REQUEST")
    void exceptionGenericaDeveRetornarBadRequest() {
        ProblemDetail problem = handler.problemException(new RuntimeException("Falha inesperada"));

        assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
        assertEquals("Ocorreu um erro inesperado", problem.getTitle());
        assertEquals("Falha inesperada", problem.getDetail());
    }

    @Test
    @DisplayName("MethodArgumentNotValidException deve retornar 400 com o mapa de erros de campo")
    @SuppressWarnings("unchecked")
    void validationErrorsDeveRetornarBadRequestComErros() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("usuarioCreateRequest", "nome", "Nome é obrigatório"),
                new FieldError("usuarioCreateRequest", "email", "Email inválido")
        ));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ProblemDetail problem = handler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
        assertEquals("Erro de validação", problem.getTitle());

        Map<String, String> errors = (Map<String, String>) problem.getProperties().get("errors");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Nome é obrigatório", errors.get("nome"));
        assertEquals("Email inválido", errors.get("email"));
    }
}