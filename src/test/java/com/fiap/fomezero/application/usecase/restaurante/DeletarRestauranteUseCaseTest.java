package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private DeletarRestauranteUseCase deletarRestauranteUseCase;

    @Test
    @DisplayName("Deve deletar restaurante com sucesso quando ID existe")
    void deveDeletarRestauranteComSucesso() {
        when(restauranteRepository.existsById(1L)).thenReturn(true);

        deletarRestauranteUseCase.deletarRestaurante(1L);

        verify(restauranteRepository).existsById(1L);
        verify(restauranteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar RestauranteNaoEncontradoException quando ID não existe")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        when(restauranteRepository.existsById(99L)).thenReturn(false);

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> deletarRestauranteUseCase.deletarRestaurante(99L));

        verify(restauranteRepository, never()).deleteById(any());
    }
}