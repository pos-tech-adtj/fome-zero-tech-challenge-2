package com.fiap.fomezero.exception;

public class SenhaAtualInvalidaException extends RuntimeException {

    public SenhaAtualInvalidaException() {
        super("Senha atual incorreta");
    }
}