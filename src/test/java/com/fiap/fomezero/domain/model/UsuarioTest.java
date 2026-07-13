package com.fiap.fomezero.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 2, 1, 10, 0);
    private static final LocalDateTime ULTIMA_SENHA = LocalDateTime.of(2024, 3, 1, 10, 0);

    private Endereco novoEndereco() {
        return Endereco.builder().id(5L).rua("Rua A").numero(10).build();
    }

    @Test
    @DisplayName("Construtor padrão deve inicializar createdAt")
    void construtorPadraoDeveInicializarCreatedAt() {
        Usuario usuario = new Usuario();

        assertNotNull(usuario.getCreatedAt());
        assertNull(usuario.getId());
        assertNull(usuario.getNome());
        assertNull(usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Construtor completo deve preencher todos os campos")
    void construtorCompletoDevePreencherTodosOsCampos() {
        Endereco endereco = novoEndereco();

        Usuario usuario = new Usuario(1L, "João Silva", "joao@email.com", "joao.silva", "senha-encoded",
                TipoUsuario.CLIENTE, ULTIMA_SENHA, CREATED_AT, UPDATED_AT, endereco);

        assertEquals(1L, usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("joao.silva", usuario.getLogin());
        assertEquals("senha-encoded", usuario.getSenha());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
        assertEquals(ULTIMA_SENHA, usuario.getDataUltimaAlteracaoSenha());
        assertEquals(CREATED_AT, usuario.getCreatedAt());
        assertEquals(UPDATED_AT, usuario.getUpdatedAt());
        assertEquals(endereco, usuario.getEndereco());
    }

    @Test
    @DisplayName("Builder deve construir o usuário com os campos informados")
    void builderDeveConstruirUsuario() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Maria Souza")
                .email("maria@email.com")
                .login("maria.souza")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .endereco(novoEndereco())
                .build();

        assertEquals(1L, usuario.getId());
        assertEquals("Maria Souza", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("maria.souza", usuario.getLogin());
        assertEquals("senha123", usuario.getSenha());
        assertEquals(TipoUsuario.DONO_RESTAURANTE, usuario.getTipoUsuario());
        assertNotNull(usuario.getEndereco());
        assertNull(usuario.getCreatedAt());
    }

    @Test
    @DisplayName("Setters devem alterar todos os campos")
    void settersDevemAlterarTodosOsCampos() {
        Usuario usuario = new Usuario();
        Endereco endereco = novoEndereco();

        usuario.setId(2L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao.silva");
        usuario.setSenha("nova-senha");
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setDataUltimaAlteracaoSenha(ULTIMA_SENHA);
        usuario.setCreatedAt(CREATED_AT);
        usuario.setUpdatedAt(UPDATED_AT);
        usuario.setEndereco(endereco);

        assertEquals(2L, usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("joao.silva", usuario.getLogin());
        assertEquals("nova-senha", usuario.getSenha());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
        assertEquals(ULTIMA_SENHA, usuario.getDataUltimaAlteracaoSenha());
        assertEquals(CREATED_AT, usuario.getCreatedAt());
        assertEquals(UPDATED_AT, usuario.getUpdatedAt());
        assertEquals(endereco, usuario.getEndereco());
    }

    @Test
    @DisplayName("equals e hashCode devem considerar os valores dos campos")
    void equalsEHashCodeDevemConsiderarOsCampos() {
        Usuario a = Usuario.builder().id(1L).nome("João").login("joao").tipoUsuario(TipoUsuario.CLIENTE).build();
        Usuario b = Usuario.builder().id(1L).nome("João").login("joao").tipoUsuario(TipoUsuario.CLIENTE).build();
        Usuario c = Usuario.builder().id(2L).nome("Maria").login("maria").tipoUsuario(TipoUsuario.DONO_RESTAURANTE).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    @DisplayName("toString deve conter os dados do usuário")
    void toStringDeveConterOsDados() {
        Usuario usuario = Usuario.builder().id(1L).nome("João Silva").build();

        String texto = usuario.toString();

        assertTrue(texto.contains("João Silva"));
        assertTrue(texto.contains("Usuario"));
    }
}