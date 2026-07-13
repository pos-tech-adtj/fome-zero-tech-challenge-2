package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.ItemCardapio;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.infrastructure.persistence.entity.ItemCardapioJpaEntity;
import com.fiap.fomezero.infrastructure.persistence.entity.RestauranteJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardapioJpaMapperTest {

    @Test
    @DisplayName("toDomain deve mapear item com restaurante")
    void toDomainDeveMapearItemComRestaurante() {
        ItemCardapioJpaEntity entity = ItemCardapioJpaEntity.builder()
                .id(1L)
                .nome("Pizza Margherita")
                .descricao("Molho de tomate e mussarela")
                .preco(new BigDecimal("49.90"))
                .apenasNoRestaurante(true)
                .fotoPath("/fotos/pizza.png")
                .restaurante(RestauranteJpaEntity.builder().id(10L).nome("Cantina Fiap").build())
                .build();

        ItemCardapio item = ItemCardapioJpaMapper.toDomain(entity);

        assertEquals(1L, item.getId());
        assertEquals("Pizza Margherita", item.getNome());
        assertEquals("Molho de tomate e mussarela", item.getDescricao());
        assertEquals(new BigDecimal("49.90"), item.getPreco());
        assertTrue(item.isApenasNoRestaurante());
        assertEquals("/fotos/pizza.png", item.getFotoPath());
        assertNotNull(item.getRestaurante());
        assertEquals(10L, item.getRestaurante().getId());
        assertEquals("Cantina Fiap", item.getRestaurante().getNome());
    }

    @Test
    @DisplayName("toDomain deve mapear item sem restaurante")
    void toDomainDeveMapearItemSemRestaurante() {
        ItemCardapioJpaEntity entity = ItemCardapioJpaEntity.builder().id(1L).nome("Pizza").build();

        ItemCardapio item = ItemCardapioJpaMapper.toDomain(entity);

        assertNull(item.getRestaurante());
        assertFalse(item.isApenasNoRestaurante());
    }

    @Test
    @DisplayName("toDomain deve retornar null quando a entidade for null")
    void toDomainDeveRetornarNullQuandoEntidadeForNull() {
        assertNull(ItemCardapioJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("toJpaEntity deve mapear item com restaurante")
    void toJpaEntityDeveMapearItemComRestaurante() {
        ItemCardapio item = ItemCardapio.builder()
                .id(1L)
                .nome("Pizza Margherita")
                .descricao("Molho de tomate e mussarela")
                .preco(new BigDecimal("49.90"))
                .apenasNoRestaurante(true)
                .fotoPath("/fotos/pizza.png")
                .restaurante(Restaurante.builder().id(10L).nome("Cantina Fiap").build())
                .build();

        ItemCardapioJpaEntity entity = ItemCardapioJpaMapper.toJpaEntity(item);

        assertEquals(1L, entity.getId());
        assertEquals("Pizza Margherita", entity.getNome());
        assertEquals("Molho de tomate e mussarela", entity.getDescricao());
        assertEquals(new BigDecimal("49.90"), entity.getPreco());
        assertTrue(entity.isApenasNoRestaurante());
        assertEquals("/fotos/pizza.png", entity.getFotoPath());
        assertNotNull(entity.getRestaurante());
        assertEquals(10L, entity.getRestaurante().getId());
    }

    @Test
    @DisplayName("toJpaEntity deve mapear item sem restaurante")
    void toJpaEntityDeveMapearItemSemRestaurante() {
        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").build();

        ItemCardapioJpaEntity entity = ItemCardapioJpaMapper.toJpaEntity(item);

        assertNull(entity.getRestaurante());
    }

    @Test
    @DisplayName("toJpaEntity deve retornar null quando o domínio for null")
    void toJpaEntityDeveRetornarNullQuandoDominioForNull() {
        assertNull(ItemCardapioJpaMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new ItemCardapioJpaMapper());
    }
}