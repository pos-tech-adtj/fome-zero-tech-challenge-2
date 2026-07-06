package com.fiap.fomezero.exception;

public class ItemCardapioNaoEncontradoException extends RuntimeException {

    private static final String MESSAGE = "Item de cardápio não encontrado";

    public ItemCardapioNaoEncontradoException() {
        super(MESSAGE);
    }
}
