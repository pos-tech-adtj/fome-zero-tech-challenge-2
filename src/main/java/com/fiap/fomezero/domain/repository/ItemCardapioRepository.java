package com.fiap.fomezero.domain.repository;

import com.fiap.fomezero.domain.model.ItemCardapio;
import java.util.List;
import java.util.Optional;

public interface ItemCardapioRepository {
    ItemCardapio save(ItemCardapio itemCardapio);
    Optional<ItemCardapio> findById(Long id);
    List<ItemCardapio> findAll();
    List<ItemCardapio> findAllByRestauranteId(Long restauranteId);
    void deleteById(Long id);
    boolean existsById(Long id);
}