package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarRestaurantesUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private ListarRestaurantesUseCase listarRestaurantesUseCase;

    private Usuario dono;

    @BeforeEach
    void setUp() {
        dono = Usuario.builder()
                .id(1L)
                .nome("Dono Teste")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();
    }

    @Test
    @DisplayName("Deve retornar lista com todos os restaurantes")
    void deveRetornarTodosOsRestaurantes() {
        // Arrange
        List<Restaurante> restaurantes = List.of(
                Restaurante.builder().id(1L).nome("Restaurante A").dono(dono).build(),
                Restaurante.builder().id(2L).nome("Restaurante B").dono(dono).build()
        );

        when(restauranteRepository.findAll()).thenReturn(restaurantes);

        // Act
        List<RestauranteResponse> responses = listarRestaurantesUseCase.listarRestaurantes();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(restauranteRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há restaurantes cadastrados")
    void deveRetornarListaVaziaQuandoNaoHaRestaurantes() {
        // Arrange
        when(restauranteRepository.findAll()).thenReturn(List.of());

        // Act
        List<RestauranteResponse> responses = listarRestaurantesUseCase.listarRestaurantes();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(restauranteRepository).findAll();
    }

    @Test
    @DisplayName("Deve mapear corretamente os dados de cada restaurante")
    void deveMapearCorretamenteCadaRestaurante() {
        // Arrange
        List<Restaurante> restaurantes = List.of(
                Restaurante.builder().id(1L).nome("Restaurante A").tipoCozinha("Italiana").dono(dono).build()
        );

        when(restauranteRepository.findAll()).thenReturn(restaurantes);

        // Act
        List<RestauranteResponse> responses = listarRestaurantesUseCase.listarRestaurantes();

        // Assert
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Restaurante A", responses.get(0).getNome());
    }
}