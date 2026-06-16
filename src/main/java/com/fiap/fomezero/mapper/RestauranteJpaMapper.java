package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.infrastructure.persistence.entity.RestauranteJpaEntity;

public class RestauranteJpaMapper {

    public static Restaurante toDomain(RestauranteJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Restaurante.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .tipoCozinha(entity.getTipoCozinha())
                .horarioFuncionamento(entity.getHorarioFuncionamento())
                .dono(UsuarioJpaMapper.toDomain(entity.getDono()))
                .endereco(EnderecoJpaMapper.toDomain(entity.getEndereco()))
                .build();
    }

    public static RestauranteJpaEntity toJpaEntity(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }

        return RestauranteJpaEntity.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .tipoCozinha(restaurante.getTipoCozinha())
                .horarioFuncionamento(restaurante.getHorarioFuncionamento())
                .dono(UsuarioJpaMapper.toJpaEntity(restaurante.getDono()))
                .endereco(EnderecoJpaMapper.toJpaEntity(restaurante.getEndereco()))
                .build();
    }
}