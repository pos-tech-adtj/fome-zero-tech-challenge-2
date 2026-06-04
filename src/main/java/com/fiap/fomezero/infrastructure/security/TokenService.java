package com.fiap.fomezero.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fiap.fomezero.domain.port.TokenPort;
import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TokenService implements TokenPort {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-hours:8}")
    private long expirationHours;

    @Override
    public String gerarToken(Long idUsuario, List<String> roles, String login) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(String.valueOf(idUsuario))
                .withClaim("roles", roles)
                .withClaim("login", login)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(expirationHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    @Override
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
