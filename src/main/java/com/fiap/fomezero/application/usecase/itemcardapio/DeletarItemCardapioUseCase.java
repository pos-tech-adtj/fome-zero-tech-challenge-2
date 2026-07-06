package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;

    public void deletarItemCardapio(Long id) {
        if (!itemCardapioRepository.existsById(id)) {
            throw new ItemCardapioNaoEncontradoException();
        }

        itemCardapioRepository.deleteById(id);
    }
}
