package com.fiap.fomezero.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardapioTest {

    private Restaurante novoRestaurante() {
        return Restaurante.builder().id(10L).nome("Cantina Fiap").build();
    }

    @Test
    @DisplayName("Construtor padrão deve criar item com campos nulos e apenasNoRestaurante false")
    void construtorPadraoDeveCriarItemVazio() {
        ItemCardapio item = new ItemCardapio();

        assertNull(item.getId());
        assertNull(item.getNome());
        assertNull(item.getPreco());
        assertNull(item.getRestaurante());
        assertFalse(item.isApenasNoRestaurante());
    }

    @Test
    @DisplayName("Construtor completo deve preencher todos os campos")
    void construtorCompletoDevePreencherTodosOsCampos() {
        Restaurante restaurante = novoRestaurante();

        ItemCardapio item = new ItemCardapio(1L, "Pizza Margherita", "Molho de tomate e mussarela",
                new BigDecimal("49.90"), true, "/fotos/pizza.png", restaurante);

        assertEquals(1L, item.getId());
        assertEquals("Pizza Margherita", item.getNome());
        assertEquals("Molho de tomate e mussarela", item.getDescricao());
        assertEquals(new BigDecimal("49.90"), item.getPreco());
        assertTrue(item.isApenasNoRestaurante());
        assertEquals("/fotos/pizza.png", item.getFotoPath());
        assertEquals(restaurante, item.getRestaurante());
    }

    @Test
    @DisplayName("Builder deve construir o item com os campos informados")
    void builderDeveConstruirItem() {
        ItemCardapio item = ItemCardapio.builder()
                .id(1L)
                .nome("Lasanha")
                .descricao("Lasanha à bolonhesa")
                .preco(new BigDecimal("39.90"))
                .apenasNoRestaurante(false)
                .fotoPath("/fotos/lasanha.png")
                .restaurante(novoRestaurante())
                .build();

        assertEquals(1L, item.getId());
        assertEquals("Lasanha", item.getNome());
        assertEquals("Lasanha à bolonhesa", item.getDescricao());
        assertEquals(new BigDecimal("39.90"), item.getPreco());
        assertFalse(item.isApenasNoRestaurante());
        assertEquals("/fotos/lasanha.png", item.getFotoPath());
        assertEquals(10L, item.getRestaurante().getId());
    }

    @Test
    @DisplayName("Setters devem alterar todos os campos")
    void settersDevemAlterarTodosOsCampos() {
        ItemCardapio item = new ItemCardapio();
        Restaurante restaurante = novoRestaurante();

        item.setId(2L);
        item.setNome("Pizza Calabresa");
        item.setDescricao("Calabresa e cebola");
        item.setPreco(new BigDecimal("59.90"));
        item.setApenasNoRestaurante(true);
        item.setFotoPath("/fotos/calabresa.png");
        item.setRestaurante(restaurante);

        assertEquals(2L, item.getId());
        assertEquals("Pizza Calabresa", item.getNome());
        assertEquals("Calabresa e cebola", item.getDescricao());
        assertEquals(new BigDecimal("59.90"), item.getPreco());
        assertTrue(item.isApenasNoRestaurante());
        assertEquals("/fotos/calabresa.png", item.getFotoPath());
        assertEquals(restaurante, item.getRestaurante());
    }

    @Test
    @DisplayName("equals e hashCode devem considerar os valores dos campos")
    void equalsEHashCodeDevemConsiderarOsCampos() {
        ItemCardapio a = ItemCardapio.builder().id(1L).nome("Pizza").preco(new BigDecimal("49.90")).build();
        ItemCardapio b = ItemCardapio.builder().id(1L).nome("Pizza").preco(new BigDecimal("49.90")).build();
        ItemCardapio c = ItemCardapio.builder().id(2L).nome("Lasanha").preco(new BigDecimal("39.90")).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    @DisplayName("toString deve conter os dados do item")
    void toStringDeveConterOsDados() {
        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza Margherita").build();

        String texto = item.toString();

        assertTrue(texto.contains("Pizza Margherita"));
        assertTrue(texto.contains("ItemCardapio"));
    }
}