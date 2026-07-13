package com.fiap.fomezero.service;

import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Deve carregar o usuário pelo login com sucesso")
    void deveCarregarUsuarioPeloLogin() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .senha("senha-encoded")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();
        when(usuarioRepository.findByLogin("joao.silva")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("joao.silva");

        assertNotNull(userDetails);
        assertEquals("senha-encoded", userDetails.getPassword());
        verify(usuarioRepository).findByLogin("joao.silva");
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o login não existir")
    void deveLancarExcecaoQuandoLoginNaoExistir() {
        when(usuarioRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("inexistente"));

        assertTrue(exception.getMessage().contains("inexistente"));
        verify(usuarioRepository).findByLogin("inexistente");
    }
}