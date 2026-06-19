package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.Usuario;

public class RestauranteMapper {

    public static Restaurante toEntity(RestauranteCreateRequest request, Usuario usuario) {
        return Restaurante.builder()
                .nome(request.nome())
                .endereco(EnderecoMapper.toEntity(request.endereco()))
                .tipoCozinha(request.tipoCozinha())
                .horarioFuncionamento(request.horarioFuncionamento())
                .dono(usuario)
                .build();
    }

    public static RestauranteResponse toResponse(Restaurante restaurante) {
        return RestauranteResponse.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .endereco(EnderecoMapper.toResponse(restaurante.getEndereco()))
                .tipoCozinha(restaurante.getTipoCozinha())
                .horarioFuncionamento(restaurante.getHorarioFuncionamento())
                .donoId(restaurante.getDono().getId())
                .build();
    }
}
