package com.fiap.fomezero.infrastructure.persistence.repository;

import com.fiap.fomezero.infrastructure.persistence.entity.ItemCardapioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapioJpaEntity, Long> {

    @Query("SELECT i FROM ItemCardapioJpaEntity i JOIN FETCH i.restaurante WHERE i.restaurante.id = :restauranteId")
    List<ItemCardapioJpaEntity> findAllByRestauranteId(@Param("restauranteId") Long restauranteId);

    @Query("SELECT i FROM ItemCardapioJpaEntity i JOIN FETCH i.restaurante WHERE i.id = :id")
    Optional<ItemCardapioJpaEntity> findByIdWithRestaurante(@Param("id") Long id);

    @Query("SELECT i FROM ItemCardapioJpaEntity i JOIN FETCH i.restaurante")
    List<ItemCardapioJpaEntity> findAllWithRestaurante();
}