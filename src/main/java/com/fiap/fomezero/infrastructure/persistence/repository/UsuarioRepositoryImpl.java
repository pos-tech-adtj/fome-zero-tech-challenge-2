package com.fiap.fomezero.infrastructure.persistence.repository;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.mapper.UsuarioJpaMapper;
import com.fiap.fomezero.repository.UsuarioJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Usuario save(Usuario usuario) {
        return UsuarioJpaMapper.toDomain(usuarioJpaRepository.save(UsuarioJpaMapper.toJpaEntity(usuario)));
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioJpaRepository.findById(id)
                .map(UsuarioJpaMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioJpaRepository.findByEmail(email)
                .map(UsuarioJpaMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        return usuarioJpaRepository.findByLogin(login)
                .map(UsuarioJpaMapper::toDomain);
    }

    @Override
    public Optional<List<Usuario>> findAllByNome(String nome) {
        return usuarioJpaRepository.findAllByNome(nome)
                .map(usuariosJpa -> usuariosJpa.stream()
                        .map(UsuarioJpaMapper::toDomain)
                        .toList());
    }

    @Override
    public boolean existsById(Long id) {
        return usuarioJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return usuarioJpaRepository.existsByLogin(login);
    }

    @Override
    public void deleteById(Long id) {
        usuarioJpaRepository.deleteById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioJpaRepository.findAll().stream()
                .map(UsuarioJpaMapper::toDomain)
                .toList();
    }
}
