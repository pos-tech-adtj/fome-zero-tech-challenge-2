package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DeletarUsuarioUseCase deletarUsuarioUseCase;

    @Test
    @DisplayName("Deve deletar usuário com sucesso quando ID existe")
    void deveDeletarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // Act
        deletarUsuarioUseCase.deletarUsuario(1L);

        // Assert
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando ID não existe")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> deletarUsuarioUseCase.deletarUsuario(99L));

        // Assert
        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve deletar o usuário com o ID correto")
    void deveDeletarComIdCorreto() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // Act
        deletarUsuarioUseCase.deletarUsuario(1L);

        // Assert
        verify(usuarioRepository).existsById(1L);
        verify(usuarioRepository).deleteById(1L);
    }
}