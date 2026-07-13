package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.infrastructure.persistence.entity.ItemCardapioJpaEntity;

public class ItemCardapioJpaMapper {

    public static ItemCardapio toDomain(ItemCardapioJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return ItemCardapio.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
                .apenasNoRestaurante(entity.isApenasNoRestaurante())
                .fotoPath(entity.getFotoPath())
                .restaurante(RestauranteJpaMapper.toDomain(entity.getRestaurante()))
                .build();
    }

    public static ItemCardapioJpaEntity toJpaEntity(ItemCardapio domain) {
        if (domain == null) {
            return null;
        }

        return ItemCardapioJpaEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .preco(domain.getPreco())
                .apenasNoRestaurante(domain.isApenasNoRestaurante())
                .fotoPath(domain.getFotoPath())
                .restaurante(RestauranteJpaMapper.toJpaEntity(domain.getRestaurante()))
                .build();
    }
  
}
