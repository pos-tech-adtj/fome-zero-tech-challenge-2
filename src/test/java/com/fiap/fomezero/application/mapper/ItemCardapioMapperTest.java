package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardapioMapperTest {

    private Restaurante novoRestaurante(Long id, String nome) {
        return Restaurante.builder().id(id).nome(nome).build();
    }

    private ItemCardapio novoItem() {
        return ItemCardapio.builder()
                .id(1L)
                .nome("Pizza Margherita")
                .descricao("Molho de tomate, mussarela e manjericão")
                .preco(new BigDecimal("49.90"))
                .apenasNoRestaurante(false)
                .fotoPath("/fotos/pizza.png")
                .restaurante(novoRestaurante(10L, "Cantina Fiap"))
                .build();
    }

    @Test
    @DisplayName("toEntity deve mapear o request e associar o restaurante")
    void toEntityDeveMapearRequest() {
        ItemCardapioCreateRequest request = new ItemCardapioCreateRequest(
                "Pizza Margherita", "Molho de tomate", new BigDecimal("49.90"), true, "/fotos/pizza.png", 10L);
        Restaurante restaurante = novoRestaurante(10L, "Cantina Fiap");

        ItemCardapio item = ItemCardapioMapper.toEntity(request, restaurante);

        assertEquals("Pizza Margherita", item.getNome());
        assertEquals("Molho de tomate", item.getDescricao());
        assertEquals(new BigDecimal("49.90"), item.getPreco());
        assertTrue(item.isApenasNoRestaurante());
        assertEquals("/fotos/pizza.png", item.getFotoPath());
        assertEquals(restaurante, item.getRestaurante());
    }

    @Test
    @DisplayName("toEntity deve tratar apenasNoRestaurante nulo como false")
    void toEntityDeveTratarApenasNoRestauranteNuloComoFalse() {
        ItemCardapioCreateRequest request = new ItemCardapioCreateRequest(
                "Pizza", null, new BigDecimal("10.00"), null, null, 10L);

        ItemCardapio item = ItemCardapioMapper.toEntity(request, novoRestaurante(10L, "Cantina Fiap"));

        assertFalse(item.isApenasNoRestaurante());
        assertNull(item.getDescricao());
        assertNull(item.getFotoPath());
    }

    @Test
    @DisplayName("toResponse deve mapear o item com dados do restaurante")
    void toResponseDeveMapearItemComRestaurante() {
        ItemCardapioResponse response = ItemCardapioMapper.toResponse(novoItem());

        assertEquals(1L, response.getId());
        assertEquals("Pizza Margherita", response.getNome());
        assertEquals("Molho de tomate, mussarela e manjericão", response.getDescricao());
        assertEquals(new BigDecimal("49.90"), response.getPreco());
        assertFalse(response.getApenasNoRestaurante());
        assertEquals("/fotos/pizza.png", response.getFotoPath());
        assertEquals(10L, response.getRestauranteId());
        assertEquals("Cantina Fiap", response.getRestauranteNome());
    }

    @Test
    @DisplayName("toResponse deve retornar restauranteId e restauranteNome nulos quando não houver restaurante")
    void toResponseDeveTratarRestauranteNulo() {
        ItemCardapio item = novoItem();
        item.setRestaurante(null);

        ItemCardapioResponse response = ItemCardapioMapper.toResponse(item);

        assertNull(response.getRestauranteId());
        assertNull(response.getRestauranteNome());
        assertEquals("Pizza Margherita", response.getNome());
    }

    @Test
    @DisplayName("updateEntity deve atualizar todos os campos informados, inclusive o restaurante")
    void updateEntityDeveAtualizarCamposInformados() {
        ItemCardapio item = novoItem();
        Restaurante novoRestaurante = novoRestaurante(20L, "Outro Restaurante");
        ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                "Pizza Calabresa", "Nova descrição", new BigDecimal("59.90"), true, "/fotos/calabresa.png", 20L);

        ItemCardapioMapper.updateEntity(item, request, novoRestaurante);

        assertEquals("Pizza Calabresa", item.getNome());
        assertEquals("Nova descrição", item.getDescricao());
        assertEquals(new BigDecimal("59.90"), item.getPreco());
        assertTrue(item.isApenasNoRestaurante());
        assertEquals("/fotos/calabresa.png", item.getFotoPath());
        assertEquals(20L, item.getRestaurante().getId());
    }

    @Test
    @DisplayName("updateEntity deve ignorar campos nulos e manter o restaurante atual")
    void updateEntityDeveIgnorarCamposNulos() {
        ItemCardapio item = novoItem();
        ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(null, null, null, null, null, null);

        ItemCardapioMapper.updateEntity(item, request, null);

        assertEquals("Pizza Margherita", item.getNome());
        assertEquals("Molho de tomate, mussarela e manjericão", item.getDescricao());
        assertEquals(new BigDecimal("49.90"), item.getPreco());
        assertFalse(item.isApenasNoRestaurante());
        assertEquals("/fotos/pizza.png", item.getFotoPath());
        assertEquals(10L, item.getRestaurante().getId());
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new ItemCardapioMapper());
    }
}