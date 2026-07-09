package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarItemCardapioPorIdUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @InjectMocks
    private BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        Restaurante restaurante = Restaurante.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .build();

        itemCardapio = ItemCardapio.builder()
                .id(10L)
                .nome("Pizza Margherita")
                .descricao("Molho de tomate, mussarela e manjericão")
                .preco(BigDecimal.valueOf(49.90))
                .apenasNoRestaurante(false)
                .fotoPath("https://cdn.fomezero.com/itens/pizza-margherita.png")
                .restaurante(restaurante)
                .build();
    }

    @Test
    @DisplayName("Deve retornar o item de cardápio quando o id existe")
    void deveRetornarItemCardapioQuandoIdExiste() {
        // Arrange
        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));

        // Act
        ItemCardapioResponse response = buscarItemCardapioPorIdUseCase.buscarPorId(10L);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Pizza Margherita", response.getNome());
        assertEquals(1L, response.getRestauranteId());
        assertEquals("Cantina Fiap", response.getRestauranteNome());
        verify(itemCardapioRepository).findById(10L);
    }

    @Test
    @DisplayName("Deve lançar ItemCardapioNaoEncontradoException quando o id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(itemCardapioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemCardapioNaoEncontradoException.class,
                () -> buscarItemCardapioPorIdUseCase.buscarPorId(99L));

        verify(itemCardapioRepository).findById(99L);
    }
}