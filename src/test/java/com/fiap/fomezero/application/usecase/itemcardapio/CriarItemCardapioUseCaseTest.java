package com.fiap.fomezero.application.usecase.itemcardapio;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.ItemCardapioRepository;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
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
class CriarItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private CriarItemCardapioUseCase criarItemCardapioUseCase;

    private Restaurante restaurante;
    private ItemCardapioCreateRequest request;

    @BeforeEach
    void setUp() {
        restaurante = Restaurante.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .build();

        request = new ItemCardapioCreateRequest(
                "Pizza Margherita",
                "Molho de tomate, mussarela e manjericão",
                BigDecimal.valueOf(49.90),
                false,
                "https://cdn.fomezero.com/itens/pizza-margherita.png",
                1L
        );
    }

    @Test
    @DisplayName("Deve criar item de cardápio quando o restaurante existe")
    void deveCriarItemCardapioQuandoRestauranteExiste() {
        // Arrange
        ItemCardapio itemSalvo = ItemCardapio.builder()
                .id(10L)
                .nome(request.nome())
                .descricao(request.descricao())
                .preco(request.preco())
                .apenasNoRestaurante(request.apenasNoRestaurante())
                .fotoPath(request.fotoPath())
                .restaurante(restaurante)
                .build();

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemSalvo);

        // Act
        ItemCardapioResponse response = criarItemCardapioUseCase.criarItemCardapio(request);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Pizza Margherita", response.getNome());
        assertEquals(1L, response.getRestauranteId());
        assertEquals("Cantina Fiap", response.getRestauranteNome());
        verify(restauranteRepository).findById(1L);
        verify(itemCardapioRepository).save(any(ItemCardapio.class));
    }

    @Test
    @DisplayName("Deve montar a entidade corretamente antes de salvar")
    void deveMontarEntidadeCorretamenteAntesDeSalvar() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(itemCardapioRepository.save(any(ItemCardapio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<ItemCardapio> captor = ArgumentCaptor.forClass(ItemCardapio.class);

        // Act
        criarItemCardapioUseCase.criarItemCardapio(request);

        // Assert
        verify(itemCardapioRepository).save(captor.capture());
        ItemCardapio itemCapturado = captor.getValue();
        assertEquals("Pizza Margherita", itemCapturado.getNome());
        assertEquals(BigDecimal.valueOf(49.90), itemCapturado.getPreco());
        assertFalse(itemCapturado.isApenasNoRestaurante());
        assertEquals(restaurante, itemCapturado.getRestaurante());
    }

    @Test
    @DisplayName("Deve lançar RestauranteNaoEncontradoException quando o restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        // Arrange
        ItemCardapioCreateRequest requestComRestauranteInexistente = new ItemCardapioCreateRequest(
                "Pizza Margherita",
                "Molho de tomate, mussarela e manjericão",
                BigDecimal.valueOf(49.90),
                false,
                "https://cdn.fomezero.com/itens/pizza-margherita.png",
                99L
        );

        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestauranteNaoEncontradoException.class,
                () -> criarItemCardapioUseCase.criarItemCardapio(requestComRestauranteInexistente));

        verify(itemCardapioRepository, never()).save(any());
    }
}