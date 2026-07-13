package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
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
class BuscarRestaurantePorIdUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        Usuario dono = Usuario.builder()
                .id(1L)
                .nome("Dono Teste")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();

        restaurante = Restaurante.builder()
                .id(1L)
                .nome("Restaurante Teste")
                .dono(dono) // <- adicionado
                .build();
    }

    @Test
    @DisplayName("Deve retornar restaurante quando encontrado por ID")
    void deveRetornarRestaurantePorId() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act
        RestauranteResponse response = buscarRestaurantePorIdUseCase.buscarPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(restaurante.getId(), response.getId());
        assertEquals(restaurante.getNome(), response.getNome());
        verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar RestauranteNaoEncontradoException quando ID não existe")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        // Arrange
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        assertThrows(RestauranteNaoEncontradoException.class,
                () -> buscarRestaurantePorIdUseCase.buscarPorId(99L));

        // Assert
        verify(restauranteRepository).findById(99L);
    }
}