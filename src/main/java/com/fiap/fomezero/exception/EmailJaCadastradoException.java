package com.fiap.fomezero.exception;

public class EmailJaCadastradoException extends RuntimeException {

    private static String message = "Já existe um usuário cadastrado com este email";

    public EmailJaCadastradoException() {
        super(message);
    }
}