package com.fiap.fomezero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Restaurante {

    private Long id;
    private String nome;
    private Endereco endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private Usuario dono;

    public Restaurante() {
    }

}