package com.fiap.fomezero.exception;

public class UsuarioNaoEDonoException extends RuntimeException {

    private static String message = "Usuário não é dono de restaurante";

    public UsuarioNaoEDonoException() {
        super(message);
    }
}
