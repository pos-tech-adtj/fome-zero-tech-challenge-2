package com.fiap.fomezero.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 2, 1, 10, 0);

    @Test
    @DisplayName("Construtor padrão deve inicializar createdAt e deixar os demais campos nulos")
    void construtorPadraoDeveInicializarCreatedAt() {
        Endereco endereco = new Endereco();

        assertNotNull(endereco.getCreatedAt());
        assertNull(endereco.getId());
        assertNull(endereco.getRua());
        assertNull(endereco.getUpdatedAt());
    }

    @Test
    @DisplayName("Construtor completo deve preencher todos os campos")
    void construtorCompletoDevePreencherTodosOsCampos() {
        Endereco endereco = new Endereco(1L, "Rua das Flores", 123, "Apto 45", "Centro",
                "São Paulo", "SP", "01310-100", CREATED_AT, UPDATED_AT);

        assertEquals(1L, endereco.getId());
        assertEquals("Rua das Flores", endereco.getRua());
        assertEquals(123, endereco.getNumero());
        assertEquals("Apto 45", endereco.getComplemento());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("01310-100", endereco.getCep());
        assertEquals(CREATED_AT, endereco.getCreatedAt());
        assertEquals(UPDATED_AT, endereco.getUpdatedAt());
    }

    @Test
    @DisplayName("Builder deve construir o endereço com os campos informados")
    void builderDeveConstruirEndereco() {
        Endereco endereco = Endereco.builder()
                .id(1L)
                .rua("Rua A")
                .numero(10)
                .cidade("Campinas")
                .estado("SP")
                .cep("13000-000")
                .createdAt(CREATED_AT)
                .build();

        assertEquals(1L, endereco.getId());
        assertEquals("Rua A", endereco.getRua());
        assertEquals(10, endereco.getNumero());
        assertEquals("Campinas", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("13000-000", endereco.getCep());
        assertEquals(CREATED_AT, endereco.getCreatedAt());
        assertNull(endereco.getComplemento());
    }

    @Test
    @DisplayName("Setters devem alterar todos os campos")
    void settersDevemAlterarTodosOsCampos() {
        Endereco endereco = new Endereco();

        endereco.setId(2L);
        endereco.setRua("Avenida Paulista");
        endereco.setNumero(999);
        endereco.setComplemento("Sala 10");
        endereco.setBairro("Bela Vista");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01310-100");
        endereco.setCreatedAt(CREATED_AT);
        endereco.setUpdatedAt(UPDATED_AT);

        assertEquals(2L, endereco.getId());
        assertEquals("Avenida Paulista", endereco.getRua());
        assertEquals(999, endereco.getNumero());
        assertEquals("Sala 10", endereco.getComplemento());
        assertEquals("Bela Vista", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("01310-100", endereco.getCep());
        assertEquals(CREATED_AT, endereco.getCreatedAt());
        assertEquals(UPDATED_AT, endereco.getUpdatedAt());
    }

    @Test
    @DisplayName("equals e hashCode devem considerar os valores dos campos")
    void equalsEHashCodeDevemConsiderarOsCampos() {
        Endereco a = Endereco.builder().id(1L).rua("Rua A").numero(10).createdAt(CREATED_AT).build();
        Endereco b = Endereco.builder().id(1L).rua("Rua A").numero(10).createdAt(CREATED_AT).build();
        Endereco c = Endereco.builder().id(2L).rua("Rua B").numero(20).createdAt(CREATED_AT).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    @DisplayName("toString deve conter os dados do endereço")
    void toStringDeveConterOsDados() {
        Endereco endereco = Endereco.builder().id(1L).rua("Rua A").build();

        String texto = endereco.toString();

        assertTrue(texto.contains("Rua A"));
        assertTrue(texto.contains("Endereco"));
    }
}