package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.application.mapper.ItemCardapioMapper;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final RestauranteRepository restauranteRepository;

    public ItemCardapioResponse criarItemCardapio(ItemCardapioCreateRequest request) {
        Restaurante restaurante = restauranteRepository.findById(request.restauranteId())
                .orElseThrow(RestauranteNaoEncontradoException::new);

        ItemCardapio itemCardapio = ItemCardapioMapper.toEntity(request, restaurante);
        return ItemCardapioMapper.toResponse(itemCardapioRepository.save(itemCardapio));
    }
}
