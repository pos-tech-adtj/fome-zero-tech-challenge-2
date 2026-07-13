package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.ItemCardapioNaoEncontradoException;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;

    private Restaurante restauranteAtual;
    private ItemCardapio itemCardapio;
    private ItemCardapioUpdateRequest requestSemTrocarRestaurante;

    @BeforeEach
    void setUp() {
        restauranteAtual = Restaurante.builder()
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
                .restaurante(restauranteAtual)
                .build();

        requestSemTrocarRestaurante = new ItemCardapioUpdateRequest(
                "Pizza Margherita Grande",
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    @DisplayName("Deve atualizar item de cardápio sem trocar o restaurante")
    void deveAtualizarItemCardapioSemTrocarRestaurante() {
        // Arrange
        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        // Act
        ItemCardapioResponse response = atualizarItemCardapioUseCase.atualizarItemCardapio(10L, requestSemTrocarRestaurante);

        // Assert
        assertNotNull(response);
        assertEquals("Pizza Margherita Grande", response.getNome());
        assertEquals(restauranteAtual.getId(), response.getRestauranteId());
        verifyNoInteractions(restauranteRepository);
        verify(itemCardapioRepository).save(itemCardapio);
    }

    @Test
    @DisplayName("Deve atualizar item de cardápio associando a um novo restaurante válido")
    void deveAtualizarItemCardapioComNovoRestaurante() {
        // Arrange
        Restaurante novoRestaurante = Restaurante.builder()
                .id(2L)
                .nome("Restaurante Novo")
                .build();

        ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                null, null, null, null, null, 2L
        );

        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));
        when(restauranteRepository.findById(2L)).thenReturn(Optional.of(novoRestaurante));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        // Act
        ItemCardapioResponse response = atualizarItemCardapioUseCase.atualizarItemCardapio(10L, request);

        // Assert
        assertNotNull(response);
        assertEquals(2L, response.getRestauranteId());
        assertEquals("Restaurante Novo", response.getRestauranteNome());
        verify(restauranteRepository).findById(2L);
        verify(itemCardapioRepository).save(itemCardapio);
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos informados, preservando os demais")
    void deveAtualizarApenasCamposInformados() {
        // Arrange
        ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                null,
                "Nova descrição",
                BigDecimal.valueOf(59.90),
                null,
                null,
                null
        );

        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        // Act
        ItemCardapioResponse response = atualizarItemCardapioUseCase.atualizarItemCardapio(10L, request);

        // Assert
        assertEquals("Pizza Margherita", response.getNome()); // não alterado
        assertEquals("Nova descrição", response.getDescricao());
        assertEquals(BigDecimal.valueOf(59.90), response.getPreco());
        assertFalse(response.getApenasNoRestaurante()); // não alterado
    }

    @Test
    @DisplayName("Deve lançar ItemCardapioNaoEncontradoException quando item não existe")
    void deveLancarExcecaoQuandoItemCardapioNaoEncontrado() {
        // Arrange
        when(itemCardapioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemCardapioNaoEncontradoException.class,
                () -> atualizarItemCardapioUseCase.atualizarItemCardapio(99L, requestSemTrocarRestaurante));

        verifyNoInteractions(restauranteRepository);
        verify(itemCardapioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar RestauranteNaoEncontradoException quando novo restaurante não existe")
    void deveLancarExcecaoQuandoNovoRestauranteNaoEncontrado() {
        // Arrange
        ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                null, null, null, null, null, 99L
        );

        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestauranteNaoEncontradoException.class,
                () -> atualizarItemCardapioUseCase.atualizarItemCardapio(10L, request));

        verify(itemCardapioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve buscar restaurante quando restauranteId não é informado")
    void naoDeveBuscarRestauranteQuandoIdNaoInformado() {
        // Arrange
        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioRepository.save(any())).thenReturn(itemCardapio);

        // Act
        atualizarItemCardapioUseCase.atualizarItemCardapio(10L, requestSemTrocarRestaurante);

        // Assert
        verifyNoInteractions(restauranteRepository);
    }

    @Test
    @DisplayName("Deve passar a entidade atualizada para o repositório antes de salvar")
    void deveSalvarEntidadeAtualizada() {
        // Arrange
        ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                "Novo Nome", null, null, true, null, null
        );

        when(itemCardapioRepository.findById(10L)).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        ArgumentCaptor<ItemCardapio> captor = ArgumentCaptor.forClass(ItemCardapio.class);

        // Act
        atualizarItemCardapioUseCase.atualizarItemCardapio(10L, request);

        // Assert
        verify(itemCardapioRepository).save(captor.capture());
        ItemCardapio salvo = captor.getValue();
        assertEquals("Novo Nome", salvo.getNome());
        assertTrue(salvo.isApenasNoRestaurante());
    }
}