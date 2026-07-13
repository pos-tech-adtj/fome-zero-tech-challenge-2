package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.application.mapper.ItemCardapioMapper;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarItemCardapioPorIdUseCase {

    private final ItemCardapioRepository itemCardapioRepository;

    public ItemCardapioResponse buscarPorId(Long id) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(id)
                .orElseThrow(ItemCardapioNaoEncontradoException::new);

        return ItemCardapioMapper.toResponse(itemCardapio);
    }
}
