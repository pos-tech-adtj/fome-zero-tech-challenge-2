package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import com.fiap.fomezero.exception.UsuarioNaoEDonoException;
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
class AtualizarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    private Usuario donoAtual;
    private Restaurante restaurante;
    private RestauranteUpdateRequest requestMesmoDono;

    @BeforeEach
    void setUp() {
        donoAtual = Usuario.builder()
                .id(1L)
                .nome("Dono Atual")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();

        restaurante = Restaurante.builder()
                .id(10L)
                .nome("Restaurante Atual")
                .dono(donoAtual)
                .build();

        requestMesmoDono = new RestauranteUpdateRequest("Novo Nome", null, null, null, 1L);
    }

    @Test
    @DisplayName("Deve atualizar restaurante mantendo o mesmo dono")
    void deveAtualizarRestauranteMantendoMesmoDono() {
        // Arrange
        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        // Act
        RestauranteResponse response = atualizarRestauranteUseCase.atualizarRestaurante(10L, requestMesmoDono);

        // Assert
        assertNotNull(response);
        verifyNoInteractions(usuarioRepository);
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve atualizar restaurante com novo dono válido")
    void deveAtualizarRestauranteComNovoDono() {
        // Arrange
        Usuario novoDono = Usuario.builder()
                .id(2L)
                .nome("Novo Dono")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();

        RestauranteUpdateRequest request = new RestauranteUpdateRequest("Novo Nome", null, null, null, 2L);

        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(novoDono));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        // Act
        RestauranteResponse response = atualizarRestauranteUseCase.atualizarRestaurante(10L, request);

        // Assert
        assertNotNull(response);
        verify(usuarioRepository).findById(2L);
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar RestauranteNaoEncontradoException quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        // Arrange
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        assertThrows(RestauranteNaoEncontradoException.class,
                () -> atualizarRestauranteUseCase.atualizarRestaurante(99L, requestMesmoDono));

        // Assert
        verifyNoInteractions(usuarioRepository);
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando novo dono não existe")
    void deveLancarExcecaoQuandoNovoDonoNaoEncontrado() {
        // Arrange
        RestauranteUpdateRequest request = new RestauranteUpdateRequest("Novo Nome", null, null, null, 99L);

        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> atualizarRestauranteUseCase.atualizarRestaurante(10L, request));

        // Assert
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEDonoException quando novo usuário não é dono de restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoEDono() {
        // Arrange
        Usuario usuarioComum = Usuario.builder()
                .id(2L)
                .nome("Usuário Comum")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();

        RestauranteUpdateRequest request = new RestauranteUpdateRequest("Novo Nome", null, null, null, 2L);

        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioComum));

        // Act
        assertThrows(UsuarioNaoEDonoException.class,
                () -> atualizarRestauranteUseCase.atualizarRestaurante(10L, request));

        // Assert
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve buscar novo dono no repositório quando donoId não muda")
    void naoDeveBuscarDonoQuandoIdNaoMuda() {
        // Arrange
        when(restauranteRepository.findById(10L)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any())).thenReturn(restaurante);

        // Act
        atualizarRestauranteUseCase.atualizarRestaurante(10L, requestMesmoDono);

        // Assert
        verifyNoInteractions(usuarioRepository);
    }
}