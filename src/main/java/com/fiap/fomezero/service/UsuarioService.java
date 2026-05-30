package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.exception.SenhaAtualInvalidaException;
import com.fiap.fomezero.exception.SenhaIgualAtualException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import com.fiap.fomezero.mapper.UsuarioMapper;
import com.fiap.fomezero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

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

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException();
        }
        usuarioRepository.deleteById(id);
    }
  
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
