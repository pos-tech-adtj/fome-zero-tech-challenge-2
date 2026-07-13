package com.fiap.fomezero.application.usecase.auth;

import com.fiap.fomezero.application.dto.request.LoginRequest;
import com.fiap.fomezero.application.dto.response.LoginResponse;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.port.TokenPort;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenPort tokenPort;

    public LoginResponse autenticarUsuario(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.senha())
        );

        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .orElseThrow(CredenciaisInvalidasException::new);

        List<String> roles = getRoles(usuario.getTipoUsuario());
        String token = tokenPort.gerarToken(usuario.getId(), roles, usuario.getLogin());

        return LoginResponse.from(usuario, token);
    }

    private List<String> getRoles(TipoUsuario tipoUsuario) {
        return List.of("ROLE_" + tipoUsuario.name());
    }
}
