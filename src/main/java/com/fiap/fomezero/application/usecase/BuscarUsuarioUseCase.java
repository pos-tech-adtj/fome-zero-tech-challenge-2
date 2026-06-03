package com.fiap.fomezero.application.usecase;

import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import com.fiap.fomezero.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        return UsuarioMapper.toResponse(usuario);
    }

    public List<UsuarioResponse> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(UsuarioMapper::toResponse).toList();
    }

    public List<UsuarioResponse> buscarUsuariosPorNome(String nome) {
        List<Usuario> usuarios = usuarioRepository.findAllByNome(nome)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        return usuarios.stream().map(UsuarioMapper::toResponse).toList();
    }

}
