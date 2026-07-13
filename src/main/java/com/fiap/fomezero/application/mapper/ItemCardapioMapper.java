package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;

import java.util.Optional;

public class ItemCardapioMapper {

    public static ItemCardapio toEntity(ItemCardapioCreateRequest request, Restaurante restaurante) {
        return ItemCardapio.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .preco(request.preco())
                .apenasNoRestaurante(Boolean.TRUE.equals(request.apenasNoRestaurante()))
                .fotoPath(request.fotoPath())
                .restaurante(restaurante)
                .build();
    }

    public static ItemCardapioResponse toResponse(ItemCardapio itemCardapio) {
        Restaurante restaurante = itemCardapio.getRestaurante();

        return ItemCardapioResponse.builder()
                .id(itemCardapio.getId())
                .nome(itemCardapio.getNome())
                .descricao(itemCardapio.getDescricao())
                .preco(itemCardapio.getPreco())
                .apenasNoRestaurante(itemCardapio.isApenasNoRestaurante())
                .fotoPath(itemCardapio.getFotoPath())
                .restauranteId(restaurante != null ? restaurante.getId() : null)
                .restauranteNome(restaurante != null ? restaurante.getNome() : null)
                .build();
    }

    public static void updateEntity(ItemCardapio itemCardapio, ItemCardapioUpdateRequest request, Restaurante restaurante) {
        Optional.ofNullable(request.nome()).ifPresent(itemCardapio::setNome);
        Optional.ofNullable(request.descricao()).ifPresent(itemCardapio::setDescricao);
        Optional.ofNullable(request.preco()).ifPresent(itemCardapio::setPreco);
        Optional.ofNullable(request.apenasNoRestaurante()).ifPresent(itemCardapio::setApenasNoRestaurante);
        Optional.ofNullable(request.fotoPath()).ifPresent(itemCardapio::setFotoPath);
        Optional.ofNullable(restaurante).ifPresent(itemCardapio::setRestaurante);
    }
}
