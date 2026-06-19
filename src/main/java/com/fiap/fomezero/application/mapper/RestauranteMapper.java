package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public static void updateEntity(Restaurante restaurante, RestauranteUpdateRequest request, Usuario dono) {
        Optional.ofNullable(request.nome()).ifPresent(restaurante::setNome);
        Optional.ofNullable(request.tipoCozinha()).ifPresent(restaurante::setTipoCozinha);
        Optional.ofNullable(request.horarioFuncionamento()).ifPresent(restaurante::setHorarioFuncionamento);
        Optional.ofNullable(request.endereco()).ifPresent(end -> {
            Endereco endereco = restaurante.getEndereco() != null ? restaurante.getEndereco() : new Endereco();
            restaurante.setEndereco(endereco);
            EnderecoMapper.updateEntity(endereco, end);
        });
        Optional.ofNullable(request.donoId()).ifPresent(id -> restaurante.setDono(dono));
        restaurante.setUpdatedAt(LocalDateTime.now());
    }
}
