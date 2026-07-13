package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException();
        }
        usuarioRepository.deleteById(id);
    }
}
