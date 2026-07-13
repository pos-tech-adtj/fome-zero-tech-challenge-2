package com.fiap.fomezero.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioTest {

    @Test
    @DisplayName("Deve conter exatamente os tipos CLIENTE e DONO_RESTAURANTE")
    void deveConterOsTiposEsperados() {
        TipoUsuario[] tipos = TipoUsuario.values();

        assertEquals(2, tipos.length);
        assertEquals(TipoUsuario.CLIENTE, tipos[0]);
        assertEquals(TipoUsuario.DONO_RESTAURANTE, tipos[1]);
    }

    @Test
    @DisplayName("valueOf deve resolver o enum a partir do nome")
    void valueOfDeveResolverOEnum() {
        assertEquals(TipoUsuario.CLIENTE, TipoUsuario.valueOf("CLIENTE"));
        assertEquals(TipoUsuario.DONO_RESTAURANTE, TipoUsuario.valueOf("DONO_RESTAURANTE"));
    }

    @Test
    @DisplayName("valueOf deve lançar exceção para um valor inexistente")
    void valueOfDeveLancarExcecaoParaValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> TipoUsuario.valueOf("ADMIN"));
    }

    @Test
    @DisplayName("name deve devolver a constante em texto")
    void nameDeveDevolverAConstante() {
        assertEquals("CLIENTE", TipoUsuario.CLIENTE.name());
        assertEquals("DONO_RESTAURANTE", TipoUsuario.DONO_RESTAURANTE.name());
    }
}