package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarItensCardapioPorRestauranteUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private ListarItensCardapioPorRestauranteUseCase listarItensCardapioPorRestauranteUseCase;

    @Test
    @DisplayName("Deve listar os itens de cardápio quando o restaurante existe")
    void deveListarItensQuandoRestauranteExiste() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .build();

        ItemCardapio item1 = ItemCardapio.builder()
                .id(10L)
                .nome("Pizza Margherita")
                .preco(BigDecimal.valueOf(49.90))
                .apenasNoRestaurante(false)
                .restaurante(restaurante)
                .build();

        ItemCardapio item2 = ItemCardapio.builder()
                .id(11L)
                .nome("Refrigerante")
                .preco(BigDecimal.valueOf(8.00))
                .apenasNoRestaurante(false)
                .restaurante(restaurante)
                .build();

        when(restauranteRepository.existsById(1L)).thenReturn(true);
        when(itemCardapioRepository.findAllByRestauranteId(1L)).thenReturn(List.of(item1, item2));

        // Act
        List<ItemCardapioResponse> response = listarItensCardapioPorRestauranteUseCase.listarPorRestaurante(1L);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Pizza Margherita", response.get(0).getNome());
        assertEquals("Refrigerante", response.get(1).getNome());
        verify(restauranteRepository).existsById(1L);
        verify(itemCardapioRepository).findAllByRestauranteId(1L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o restaurante não possui itens de cardápio")
    void deveRetornarListaVaziaQuandoRestauranteNaoPossuiItens() {
        // Arrange
        when(restauranteRepository.existsById(1L)).thenReturn(true);
        when(itemCardapioRepository.findAllByRestauranteId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<ItemCardapioResponse> response = listarItensCardapioPorRestauranteUseCase.listarPorRestaurante(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar RestauranteNaoEncontradoException quando o restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        // Arrange
        when(restauranteRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(RestauranteNaoEncontradoException.class,
                () -> listarItensCardapioPorRestauranteUseCase.listarPorRestaurante(99L));

        verifyNoInteractions(itemCardapioRepository);
    }
}