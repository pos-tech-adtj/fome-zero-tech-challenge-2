package com.fiap.fomezero.repository;

import com.fiap.fomezero.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByLogin(String login);
    Usuario save(Usuario usuario);
}