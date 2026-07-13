package com.fiap.fomezero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ProblemDetail emailJaCadastrado(EmailJaCadastradoException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Email já cadastrado");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(LoginJaCadastradoException.class)
    public ProblemDetail loginJaCadastrado(LoginJaCadastradoException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Login já cadastrado");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ProblemDetail usuarioNaoEncontrado(UsuarioNaoEncontradoException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Usuário não encontrado");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(UsuarioNaoEDonoException.class)
    public ProblemDetail usuarioNaoEDono(UsuarioNaoEDonoException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Usuário não é dono de restaurante");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(RestauranteNaoEncontradoException.class)
    public ProblemDetail restauranteNaoEncontrado(RestauranteNaoEncontradoException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Restaurante não encontrado");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(ItemCardapioNaoEncontradoException.class)
    public ProblemDetail itemCardapioNaoEncontrado(ItemCardapioNaoEncontradoException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Item de cardápio não encontrado");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail credenciaisInvalidas(CredenciaisInvalidasException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Credenciais inválidas");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(SenhaAtualInvalidaException.class)
    public ProblemDetail senhaAtualInvalida(SenhaAtualInvalidaException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Senha atual inválida");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(SenhaIgualAtualException.class)
    public ProblemDetail senhaIgualAtual(SenhaIgualAtualException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Nova senha igual à atual");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException e) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Erro de validação");

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail badCredentials(BadCredentialsException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Credenciais inválidas");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail problemException(Exception e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Ocorreu um erro inesperado");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }
}
