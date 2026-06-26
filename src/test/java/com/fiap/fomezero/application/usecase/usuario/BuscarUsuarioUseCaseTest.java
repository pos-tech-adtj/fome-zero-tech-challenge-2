package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private BuscarUsuarioUseCase buscarUsuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .login("usuario@email.com")
                .email("usuario@email.com")
                .nome("João Silva")
                .build();
    }

    @Test
    @DisplayName("Deve retornar usuário quando encontrado por ID")
    void deveRetornarUsuarioPorId() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioResponse response = buscarUsuarioUseCase.buscarUsuarioPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(usuario.getId(), response.getId());
        assertEquals(usuario.getNome(), response.getNome());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando ID não existe")
    void deveLancarExcecaoQuandoIdNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> buscarUsuarioUseCase.buscarUsuarioPorId(99L));

        // Assert
        verify(usuarioRepository).findById(99L);
    }

    @Test
    @DisplayName("Deve retornar lista com todos os usuários")
    void deveListarTodosUsuarios() {
        // Arrange
        Usuario usuario2 = Usuario.builder()
                .id(2L)
                .login("outro@email.com")
                .email("outro@email.com")
                .nome("Maria Souza")
                .build();

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario, usuario2));

        // Act
        List<UsuarioResponse> responses = buscarUsuarioUseCase.listarTodosUsuarios();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários cadastrados")
    void deveRetornarListaVaziaQuandoNaoHaUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(List.of());

        // Act
        List<UsuarioResponse> responses = buscarUsuarioUseCase.listarTodosUsuarios();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar usuários quando encontrados por nome")
    void deveRetornarUsuariosPorNome() {
        // Arrange
        when(usuarioRepository.findAllByNome("João Silva"))
                .thenReturn(Optional.of(List.of(usuario)));

        // Act
        List<UsuarioResponse> responses = buscarUsuarioUseCase.buscarUsuariosPorNome("João Silva");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("João Silva", responses.get(0).getNome());
        verify(usuarioRepository).findAllByNome("João Silva");
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando nenhum usuário encontrado por nome")
    void deveLancarExcecaoQuandoNomeNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findAllByNome("Inexistente"))
                .thenReturn(Optional.empty());

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> buscarUsuarioUseCase.buscarUsuariosPorNome("Inexistente"));

        // Assert
        verify(usuarioRepository).findAllByNome("Inexistente");
    }

    @Test
    @DisplayName("Deve mapear corretamente todos os usuários retornados por nome")
    void deveMapearTodosUsuariosRetornadosPorNome() {
        // Arrange
        Usuario usuario2 = Usuario.builder()
                .id(2L)
                .login("joao2@email.com")
                .email("joao2@email.com")
                .nome("João Silva")
                .build();

        when(usuarioRepository.findAllByNome("João Silva"))
                .thenReturn(Optional.of(List.of(usuario, usuario2)));

        // Act
        List<UsuarioResponse> responses = buscarUsuarioUseCase.buscarUsuariosPorNome("João Silva");

        // Assert
        assertEquals(2, responses.size());
        assertTrue(responses.stream().allMatch(r -> r.getNome().equals("João Silva")));
    }
}