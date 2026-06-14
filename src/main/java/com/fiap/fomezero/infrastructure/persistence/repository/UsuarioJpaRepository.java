package com.fiap.fomezero.infrastructure.persistence.repository;

import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Long> {
    Optional<UsuarioJpaEntity> findByEmail(String email);

    Optional<UsuarioJpaEntity> findByLogin(String login);

    Optional<List<UsuarioJpaEntity>> findAllByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}