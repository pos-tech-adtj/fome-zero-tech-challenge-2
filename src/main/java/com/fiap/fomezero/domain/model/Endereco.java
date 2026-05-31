package com.fiap.fomezero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Endereco {

    private Long id;

    private String rua;

    private Integer numero;

    private String complemento;

    private String bairro;

    private String cidade;

    private String estado;

    private String cep;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Endereco() {
        this.createdAt = LocalDateTime.now();
    }
}
