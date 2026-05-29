package com.fiap.fomezero.exception;

public class SenhaIgualAtualException extends RuntimeException {

    public SenhaIgualAtualException() {
        super("A nova senha não pode ser igual à senha atual");
    }
}