package com.fiap.fomezero.application.usecase;

import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.SenhaAtualInvalidaException;
import com.fiap.fomezero.exception.SenhaIgualAtualException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlterarSenhaUseCase {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public void alterarSenha(Long id, UsuarioSenhaRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new SenhaAtualInvalidaException();
        }

        if (passwordEncoder.matches(request.novaSenha(), usuario.getSenha())) {
            throw new SenhaIgualAtualException();
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuario.setDataUltimaAlteracaoSenha(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}
