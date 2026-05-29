package com.fiap.fomezero.exception;

public class LoginJaCadastradoException extends RuntimeException {

    private static String message = "Já existe um usuário cadastrado com este login";

    public LoginJaCadastradoException() {
        super(message);
    }
}