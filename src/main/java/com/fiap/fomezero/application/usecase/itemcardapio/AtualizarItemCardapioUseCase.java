package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.application.mapper.ItemCardapioMapper;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final RestauranteRepository restauranteRepository;

    public ItemCardapioResponse atualizarItemCardapio(Long id, ItemCardapioUpdateRequest request) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(id)
                .orElseThrow(ItemCardapioNaoEncontradoException::new);

        Restaurante restaurante = null;
        if (request.restauranteId() != null) {
            restaurante = restauranteRepository.findById(request.restauranteId())
                    .orElseThrow(RestauranteNaoEncontradoException::new);
        }

        ItemCardapioMapper.updateEntity(itemCardapio, request, restaurante);
        return ItemCardapioMapper.toResponse(itemCardapioRepository.save(itemCardapio));
    }
}
