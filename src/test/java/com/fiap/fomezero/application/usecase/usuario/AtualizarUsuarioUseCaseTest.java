package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .login("login.atual")
                .email("atual@email.com")
                .nome("Nome Atual")
                .build();
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso sem alterar email nem login")
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest("Novo Nome", null, null, null, null);
        Usuario usuarioSalvo = Usuario.builder()
                .id(1L)
                .login("login.atual")
                .email("atual@email.com")
                .nome("Novo Nome")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        UsuarioResponse response = atualizarUsuarioUseCase.atualizarUsuario(1L, request);

        // Assert
        assertNotNull(response);
        verify(usuarioRepository).save(usuario);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest("Novo Nome", null, null, null, null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> atualizarUsuarioUseCase.atualizarUsuario(1L, request));

        // Assert
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar EmailJaCadastradoException quando novo email já está em uso")
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, "novo@email.com", null, null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("novo@email.com")).thenReturn(true);

        // Act
        assertThrows(EmailJaCadastradoException.class,
                () -> atualizarUsuarioUseCase.atualizarUsuario(1L, request));

        // Assert
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar LoginJaCadastradoException quando novo login já está em uso")
    void deveLancarExcecaoQuandoLoginJaCadastrado() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, null, "login.novo", null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByLogin("login.novo")).thenReturn(true);

        // Act
        assertThrows(LoginJaCadastradoException.class,
                () -> atualizarUsuarioUseCase.atualizarUsuario(1L, request));

        // Assert
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve validar email quando o novo email é igual ao atual")
    void naoDeveValidarEmailQuandoIgualAoAtual() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, "atual@email.com", null, null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        // Act
        atualizarUsuarioUseCase.atualizarUsuario(1L, request);

        // Assert
        verify(usuarioRepository, never()).existsByEmail(any());
        verify(usuarioRepository).save(any());
    }

    @Test
    @DisplayName("Não deve validar login quando o novo login é igual ao atual")
    void naoDeveValidarLoginQuandoIgualAoAtual() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, null, "login.atual", null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        // Act
        atualizarUsuarioUseCase.atualizarUsuario(1L, request);

        // Assert
        verify(usuarioRepository, never()).existsByLogin(any());
        verify(usuarioRepository).save(any());
    }

    @Test
    @DisplayName("Deve atualizar email e login quando ambos são novos e disponíveis")
    void deveAtualizarEmailELoginComSucesso() {
        // Arrange
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, "novo@email.com", "login.novo", null, null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("novo@email.com")).thenReturn(false);
        when(usuarioRepository.existsByLogin("login.novo")).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(usuario);

        // Act
        UsuarioResponse response = atualizarUsuarioUseCase.atualizarUsuario(1L, request);

        // Assert
        assertNotNull(response);
        verify(usuarioRepository).existsByEmail("novo@email.com");
        verify(usuarioRepository).existsByLogin("login.novo");
        verify(usuarioRepository).save(usuario);
    }
}