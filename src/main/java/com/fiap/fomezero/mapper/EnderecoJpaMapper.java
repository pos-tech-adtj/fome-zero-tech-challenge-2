package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.infrastructure.persistence.entity.EnderecoJpaEntity;

public class EnderecoJpaMapper {

    public static Endereco toDomain(EnderecoJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Endereco.builder()
                .id(entity.getId())
                .rua(entity.getRua())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .cidade(entity.getCidade())
                .estado(entity.getEstado())
                .cep(entity.getCep())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static EnderecoJpaEntity toJpaEntity(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoJpaEntity.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .createdAt(endereco.getCreatedAt())
                .updatedAt(endereco.getUpdatedAt())
                .build();
    }
}