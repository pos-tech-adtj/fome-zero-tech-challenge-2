package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.application.mapper.ItemCardapioMapper;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarItensCardapioPorRestauranteUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final RestauranteRepository restauranteRepository;

    public List<ItemCardapioResponse> listarPorRestaurante(Long restauranteId) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new RestauranteNaoEncontradoException();
        }

        return itemCardapioRepository.findAllByRestauranteId(restauranteId).stream()
                .map(ItemCardapioMapper::toResponse)
                .toList();
    }
}
