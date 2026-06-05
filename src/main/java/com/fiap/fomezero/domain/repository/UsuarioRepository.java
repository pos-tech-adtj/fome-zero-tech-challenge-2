package com.fiap.fomezero.domain.repository;

import com.fiap.fomezero.infrastructure.persistence.UsuarioJpaEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Optional<UsuarioJpaEntity> findByEmail(String email);

    Optional<UsuarioJpaEntity> findByLogin(String login);

    List<UsuarioJpaEntity> findAllByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    UsuarioJpaEntity save(UsuarioJpaEntity usuario);

    Optional<UsuarioJpaEntity> findById(Long id);

    List<UsuarioJpaEntity> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);
}
