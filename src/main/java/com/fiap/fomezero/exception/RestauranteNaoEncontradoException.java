package com.fiap.fomezero.exception;

public class RestauranteNaoEncontradoException extends RuntimeException {

    private static String message = "Restaurante não encontrado";

    public RestauranteNaoEncontradoException() {
        super(message);
    }
}
