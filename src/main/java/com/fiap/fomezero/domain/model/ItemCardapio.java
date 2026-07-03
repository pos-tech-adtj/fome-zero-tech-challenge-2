package com.fiap.fomezero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ItemCardapio {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean apenasNoRestaurante;
    private String fotoPath;
    private Restaurante restaurante;

    public ItemCardapio() {
    }

}