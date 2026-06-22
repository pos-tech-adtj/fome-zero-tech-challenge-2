package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
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
class CriarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CriarRestauranteUseCase criarRestauranteUseCase;

    private Usuario dono;
    private RestauranteCreateRequest request;
    private Restaurante restauranteSalvo;

    @BeforeEach
    void setUp() {
        dono = Usuario.builder()
                .id(1L)
                .nome("Dono Teste")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();

        request = new RestauranteCreateRequest("Restaurante Teste", null, "Italiana", "08:00-22:00", 1L);

        restauranteSalvo = Restaurante.builder()
                .id(10L)
                .nome("Restaurante Teste")
                .tipoCozinha("Italiana")
                .dono(dono)
                .build();
    }

    @Test
    @DisplayName("Deve criar restaurante com sucesso quando usuário é dono")
    void deveCriarRestauranteComSucesso() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Act
        RestauranteResponse response = criarRestauranteUseCase.criarRestaurante(request);

        // Assert
        assertNotNull(response);
        assertEquals(restauranteSalvo.getId(), response.getId());
        assertEquals(restauranteSalvo.getNome(), response.getNome());
        verify(usuarioRepository).findById(1L);
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> criarRestauranteUseCase.criarRestaurante(request));
        // Assert
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEDonoException quando usuário não é dono de restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoEDono() {
        // Arrange
        Usuario usuarioComum = Usuario.builder()
                .id(1L)
                .nome("Cliente")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));

        // Act
        assertThrows(UsuarioNaoEDonoException.class,
                () -> criarRestauranteUseCase.criarRestaurante(request));

        // Assert
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve salvar restaurante com o dono correto")
    void deveSalvarRestauranteComDonoCorreto() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(restauranteRepository.save(any())).thenReturn(restauranteSalvo);

        // Act
        criarRestauranteUseCase.criarRestaurante(request);

        // Assert
        verify(restauranteRepository).save(argThat(restaurante ->
                restaurante.getDono().getId().equals(dono.getId()) &&
                        restaurante.getDono().getTipoUsuario() == TipoUsuario.DONO_RESTAURANTE
        ));
    }
}