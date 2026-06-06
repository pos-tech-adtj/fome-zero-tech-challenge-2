package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.EnderecoRequest;
import com.fiap.fomezero.application.dto.request.EnderecoUpdateRequest;
import com.fiap.fomezero.application.dto.response.EnderecoResponse;
import com.fiap.fomezero.domain.model.Endereco;

import java.time.LocalDateTime;
import java.util.Optional;

public class EnderecoMapper {

    public static Endereco toEntity(EnderecoRequest request) {
        if (request == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        return Endereco.builder()
                .rua(request.rua())
                .numero(request.numero())
                .complemento(request.complemento())
                .bairro(request.bairro())
                .cidade(request.cidade())
                .estado(request.estado())
                .cep(request.cep())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static EnderecoResponse toResponse(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoResponse.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

    public static void updateEntity(Endereco endereco, EnderecoUpdateRequest request) {
        if (request == null) {
            return;
        }

        Optional.ofNullable(request.rua()).ifPresent(endereco::setRua);
        Optional.ofNullable(request.numero()).ifPresent(endereco::setNumero);
        Optional.ofNullable(request.complemento()).ifPresent(endereco::setComplemento);
        Optional.ofNullable(request.bairro()).ifPresent(endereco::setBairro);
        Optional.ofNullable(request.cidade()).ifPresent(endereco::setCidade);
        Optional.ofNullable(request.estado()).ifPresent(endereco::setEstado);
        Optional.ofNullable(request.cep()).ifPresent(endereco::setCep);
        endereco.setUpdatedAt(LocalDateTime.now());
    }
}
