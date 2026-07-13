package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.SenhaAtualInvalidaException;
import com.fiap.fomezero.exception.SenhaIgualAtualException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AlterarSenhaUseCase alterarSenhaUseCase;

    private Usuario usuario;
    private UsuarioSenhaRequest request;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .login("usuario@email.com")
                .senha("senha-encoded")
                .build();

        request = new UsuarioSenhaRequest("senhaAtual123", "novaSenha456");
    }

    @Test
    @DisplayName("Deve alterar senha com sucesso")
    void deveAlterarSenhaComSucesso() {
        // Arrange
        String novaSenhaEncoded = "novaSenha-encoded";

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())).thenReturn(true);
        when(passwordEncoder.matches(request.novaSenha(), usuario.getSenha())).thenReturn(false);
        when(passwordEncoder.encode(request.novaSenha())).thenReturn(novaSenhaEncoded);

        // Act
        alterarSenhaUseCase.alterarSenha(1L, request);

        // Assert
        assertEquals(novaSenhaEncoded, usuario.getSenha());
        assertNotNull(usuario.getDataUltimaAlteracaoSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> alterarSenhaUseCase.alterarSenha(1L, request));

        // Assert
        verifyNoInteractions(passwordEncoder);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar SenhaAtualInvalidaException quando senha atual está incorreta")
    void deveLancarExcecaoQuandoSenhaAtualInvalida() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())).thenReturn(false);

        // Act
        assertThrows(SenhaAtualInvalidaException.class,
                () -> alterarSenhaUseCase.alterarSenha(1L, request));

        // Assert
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar SenhaIgualAtualException quando nova senha é igual à atual")
    void deveLancarExcecaoQuandoNovaSenhaIgualAtual() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())).thenReturn(true);
        when(passwordEncoder.matches(request.novaSenha(), usuario.getSenha())).thenReturn(true);

        // Act
        assertThrows(SenhaIgualAtualException.class,
                () -> alterarSenhaUseCase.alterarSenha(1L, request));

        // Assert
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar dataUltimaAlteracaoSenha para um horário próximo ao atual")
    void deveAtualizarDataUltimaAlteracaoSenha() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())).thenReturn(true);
        when(passwordEncoder.matches(request.novaSenha(), usuario.getSenha())).thenReturn(false);
        when(passwordEncoder.encode(request.novaSenha())).thenReturn("novaSenha-encoded");

        // Act
        alterarSenhaUseCase.alterarSenha(1L, request);

        // Assert
        LocalDateTime depois = LocalDateTime.now().plusSeconds(1);
        assertTrue(usuario.getDataUltimaAlteracaoSenha().isAfter(antes));
        assertTrue(usuario.getDataUltimaAlteracaoSenha().isBefore(depois));
    }
}