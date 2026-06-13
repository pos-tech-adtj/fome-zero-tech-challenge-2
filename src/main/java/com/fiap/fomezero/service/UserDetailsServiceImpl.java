// Caminho: src/main/java/com/fiap/fomezero/service/UserDetailsServiceImpl.java
package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.repository.UsuarioRepository; // Interface de Domínio
import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity; // Entidade que é UserDetails
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Agora injetamos a interface de domínio, não mais o JPA direto!
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos o usuário usando a interface de domínio.
        // Ele retorna um objeto Usuario limpo.
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        // 2. O Spring Security precisa de um objeto que implemente "UserDetails".
        // A nossa UsuarioJpaEntity implementa essa interface.
        // Então usamos o construtor dela para "encapsular" nosso usuário de domínio e entregá-lo ao Spring.
        return new UsuarioJpaEntity(usuario);
    }
}