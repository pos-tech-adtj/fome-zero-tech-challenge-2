package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.application.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse criarUsuario(UsuarioCreateRequest request) {
        validarEmailCadastrado(request.email());
        validarLoginCadastrado(request.login());

        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario.setSenha(passwordEncoder.encode(request.senha()));
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
}