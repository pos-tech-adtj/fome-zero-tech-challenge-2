package com.fiap.fomezero.domain.repository;

import com.fiap.fomezero.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByLogin(String login);
    Optional<List<Usuario>> findAllByNome(String nome);
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    void deleteById(Long id);
    List<Usuario> findAll();
}
