package com.fiap.fomezero.application.usecase;

import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import com.fiap.fomezero.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse atualizarUsuario(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        validarInformacoesAtualizadas(usuario, request);

        UsuarioMapper.updateEntity(usuario, request);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }

    private void validarEmailCadastrado(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException();
        }
    }

    private void validarLoginCadastrado(String login) {
        if (usuarioRepository.existsByLogin(login)) {
            throw new LoginJaCadastradoException();
        }
    }

    private void validarInformacoesAtualizadas(Usuario usuario, UsuarioUpdateRequest request) {
        if (request.email() != null && !request.email().equals(usuario.getEmail())) {
            validarEmailCadastrado(request.email());
        }
        if (request.login() != null &&!request.login().equals(usuario.getLogin())) {
            validarLoginCadastrado(request.login());
        }
    }
}
