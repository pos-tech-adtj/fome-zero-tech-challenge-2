package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @InjectMocks
    private DeletarItemCardapioUseCase deletarItemCardapioUseCase;

    @Test
    @DisplayName("Deve deletar o item de cardápio quando o id existe")
    void deveDeletarItemCardapioQuandoIdExiste() {
        // Arrange
        when(itemCardapioRepository.existsById(10L)).thenReturn(true);

        // Act
        deletarItemCardapioUseCase.deletarItemCardapio(10L);

        // Assert
        verify(itemCardapioRepository).existsById(10L);
        verify(itemCardapioRepository).deleteById(10L);
    }

    @Test
    @DisplayName("Deve lançar ItemCardapioNaoEncontradoException quando o id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(itemCardapioRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ItemCardapioNaoEncontradoException.class,
                () -> deletarItemCardapioUseCase.deletarItemCardapio(99L));

        verify(itemCardapioRepository, never()).deleteById(any());
    }
}