package com.fiap.fomezero.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    private static String message = "Usuário não encontrado";

    public UsuarioNaoEncontradoException() {
        super(message);
    }
}
