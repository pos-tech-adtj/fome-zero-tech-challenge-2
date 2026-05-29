package com.fiap.fomezero.repository;

import com.fiap.fomezero.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByLogin(String login);

    Optional<List<Usuario>> findAllByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}