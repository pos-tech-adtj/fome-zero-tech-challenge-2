package com.fiap.fomezero.service;

import com.fiap.fomezero.application.dto.request.LoginRequest;
import com.fiap.fomezero.application.dto.response.LoginResponse;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager manager;

    public LoginResponse autenticarUsuario(LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.login(), request.senha());
        Authentication authentication = manager.authenticate(authToken);

        if (!authentication.isAuthenticated()) {
            throw new CredenciaisInvalidasException();
        }

        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .orElseThrow(CredenciaisInvalidasException::new);

        return LoginResponse.from(usuario);
    }

}
