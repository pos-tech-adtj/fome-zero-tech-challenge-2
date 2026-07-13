package com.fiap.fomezero.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 2, 1, 10, 0);

    private Usuario novoDono() {
        return Usuario.builder().id(2L).nome("Dono").tipoUsuario(TipoUsuario.DONO_RESTAURANTE).build();
    }

    private Endereco novoEndereco() {
        return Endereco.builder().id(3L).rua("Rua A").numero(10).build();
    }

    @Test
    @DisplayName("Construtor padrão deve inicializar createdAt")
    void construtorPadraoDeveInicializarCreatedAt() {
        Restaurante restaurante = new Restaurante();

        assertNotNull(restaurante.getCreatedAt());
        assertNull(restaurante.getId());
        assertNull(restaurante.getNome());
        assertNull(restaurante.getDono());
        assertNull(restaurante.getEndereco());
    }

    @Test
    @DisplayName("Construtor completo deve preencher todos os campos")
    void construtorCompletoDevePreencherTodosOsCampos() {
        Endereco endereco = novoEndereco();
        Usuario dono = novoDono();

        Restaurante restaurante = new Restaurante(1L, "Cantina Fiap", endereco, "Italiana",
                "09:00 - 22:00", dono, CREATED_AT, UPDATED_AT);

        assertEquals(1L, restaurante.getId());
        assertEquals("Cantina Fiap", restaurante.getNome());
        assertEquals(endereco, restaurante.getEndereco());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals("09:00 - 22:00", restaurante.getHorarioFuncionamento());
        assertEquals(dono, restaurante.getDono());
        assertEquals(CREATED_AT, restaurante.getCreatedAt());
        assertEquals(UPDATED_AT, restaurante.getUpdatedAt());
    }

    @Test
    @DisplayName("Builder deve construir o restaurante com os campos informados")
    void builderDeveConstruirRestaurante() {
        Restaurante restaurante = Restaurante.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .tipoCozinha("Japonesa")
                .horarioFuncionamento("10:00 - 23:00")
                .dono(novoDono())
                .endereco(novoEndereco())
                .build();

        assertEquals(1L, restaurante.getId());
        assertEquals("Cantina Fiap", restaurante.getNome());
        assertEquals("Japonesa", restaurante.getTipoCozinha());
        assertEquals("10:00 - 23:00", restaurante.getHorarioFuncionamento());
        assertEquals(2L, restaurante.getDono().getId());
        assertEquals("Rua A", restaurante.getEndereco().getRua());
    }

    @Test
    @DisplayName("Setters devem alterar todos os campos")
    void settersDevemAlterarTodosOsCampos() {
        Restaurante restaurante = new Restaurante();
        Endereco endereco = novoEndereco();
        Usuario dono = novoDono();

        restaurante.setId(9L);
        restaurante.setNome("Novo Nome");
        restaurante.setEndereco(endereco);
        restaurante.setTipoCozinha("Mexicana");
        restaurante.setHorarioFuncionamento("11:00 - 20:00");
        restaurante.setDono(dono);
        restaurante.setCreatedAt(CREATED_AT);
        restaurante.setUpdatedAt(UPDATED_AT);

        assertEquals(9L, restaurante.getId());
        assertEquals("Novo Nome", restaurante.getNome());
        assertEquals(endereco, restaurante.getEndereco());
        assertEquals("Mexicana", restaurante.getTipoCozinha());
        assertEquals("11:00 - 20:00", restaurante.getHorarioFuncionamento());
        assertEquals(dono, restaurante.getDono());
        assertEquals(CREATED_AT, restaurante.getCreatedAt());
        assertEquals(UPDATED_AT, restaurante.getUpdatedAt());
    }

    @Test
    @DisplayName("equals e hashCode devem considerar os valores dos campos")
    void equalsEHashCodeDevemConsiderarOsCampos() {
        Restaurante a = Restaurante.builder().id(1L).nome("Cantina").tipoCozinha("Italiana").build();
        Restaurante b = Restaurante.builder().id(1L).nome("Cantina").tipoCozinha("Italiana").build();
        Restaurante c = Restaurante.builder().id(2L).nome("Outro").tipoCozinha("Japonesa").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    @DisplayName("toString deve conter os dados do restaurante")
    void toStringDeveConterOsDados() {
        Restaurante restaurante = Restaurante.builder().id(1L).nome("Cantina Fiap").build();

        String texto = restaurante.toString();

        assertTrue(texto.contains("Cantina Fiap"));
        assertTrue(texto.contains("Restaurante"));
    }
}