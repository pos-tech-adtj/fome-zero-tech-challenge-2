// ItemCardapioRepositoryImpl.java
package com.fiap.fomezero.infrastructure.persistence.repository;

import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.mapper.ItemCardapioJpaMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ItemCardapioRepositoryImpl implements ItemCardapioRepository {

    private final ItemCardapioJpaRepository itemCardapioJpaRepository;

    @Override
    @Transactional
    public ItemCardapio save(ItemCardapio itemCardapio) {
        return ItemCardapioJpaMapper.toDomain(
                itemCardapioJpaRepository.save(ItemCardapioJpaMapper.toJpaEntity(itemCardapio))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCardapio> findById(Long id) {
        return itemCardapioJpaRepository.findByIdWithRestaurante(id)
                .map(ItemCardapioJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> findAll() {
        return itemCardapioJpaRepository.findAllWithRestaurante().stream()
                .map(ItemCardapioJpaMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        itemCardapioJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> findAllByRestauranteId(Long restauranteId) {
        return itemCardapioJpaRepository.findAllByRestauranteId(restauranteId).stream()
                .map(ItemCardapioJpaMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return itemCardapioJpaRepository.existsById(id);
    }
}