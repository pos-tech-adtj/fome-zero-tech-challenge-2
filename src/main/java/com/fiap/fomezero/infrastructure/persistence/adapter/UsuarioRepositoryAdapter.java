package com.fiap.fomezero.infrastructure.persistence.adapter;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.repository.UsuarioRepository;
import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.fiap.fomezero.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    // O Spring injeta o motor do JPA aqui
    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        // Busca no banco. Se achar, usa o método toDomain() para converter antes de devolver.
        return jpaRepository.findByLogin(login)
                .map(UsuarioJpaEntity::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Converte o Usuario puro para Entidade -> Salva no banco -> Converte de volta para Usuario puro
        UsuarioJpaEntity entity = new UsuarioJpaEntity(usuario);
        UsuarioJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }
}