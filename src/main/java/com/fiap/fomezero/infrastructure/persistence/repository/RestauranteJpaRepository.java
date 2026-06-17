package com.fiap.fomezero.infrastructure.persistence.repository;

import com.fiap.fomezero.infrastructure.persistence.entity.RestauranteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteJpaRepository extends JpaRepository<RestauranteJpaEntity, Long> {
}