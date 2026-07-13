package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.infrastructure.persistence.entity.EnderecoJpaEntity;
import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioJpaMapperTest {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 2, 1, 10, 0);
    private static final LocalDateTime ULTIMA_SENHA = LocalDateTime.of(2024, 3, 1, 10, 0);

    @Test
    @DisplayName("toDomain deve mapear todos os campos, incluindo o endereço")
    void toDomainDeveMapearTodosOsCampos() {
        UsuarioJpaEntity entity = UsuarioJpaEntity.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .senha("senha-encoded")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .dataUltimaAlteracaoSenha(ULTIMA_SENHA)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .endereco(EnderecoJpaEntity.builder().id(5L).rua("Rua A").numero(10).build())
                .build();

        Usuario usuario = UsuarioJpaMapper.toDomain(entity);

        assertEquals(1L, usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("joao.silva", usuario.getLogin());
        assertEquals("senha-encoded", usuario.getSenha());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
        assertEquals(ULTIMA_SENHA, usuario.getDataUltimaAlteracaoSenha());
        assertEquals(CREATED_AT, usuario.getCreatedAt());
        assertEquals(UPDATED_AT, usuario.getUpdatedAt());
        assertNotNull(usuario.getEndereco());
        assertEquals(5L, usuario.getEndereco().getId());
        assertEquals("Rua A", usuario.getEndereco().getRua());
    }

    @Test
    @DisplayName("toDomain deve mapear usuário sem endereço")
    void toDomainDeveMapearUsuarioSemEndereco() {
        UsuarioJpaEntity entity = UsuarioJpaEntity.builder()
                .id(1L)
                .nome("João Silva")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();

        Usuario usuario = UsuarioJpaMapper.toDomain(entity);

        assertNull(usuario.getEndereco());
        assertEquals(TipoUsuario.DONO_RESTAURANTE, usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("toDomain deve retornar null quando a entidade for null")
    void toDomainDeveRetornarNullQuandoEntidadeForNull() {
        assertNull(UsuarioJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("toJpaEntity deve mapear todos os campos, incluindo o endereço")
    void toJpaEntityDeveMapearTodosOsCampos() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .senha("senha-encoded")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .dataUltimaAlteracaoSenha(ULTIMA_SENHA)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .endereco(Endereco.builder().id(5L).rua("Rua A").numero(10).build())
                .build();

        UsuarioJpaEntity entity = UsuarioJpaMapper.toJpaEntity(usuario);

        assertEquals(1L, entity.getId());
        assertEquals("João Silva", entity.getNome());
        assertEquals("joao@email.com", entity.getEmail());
        assertEquals("joao.silva", entity.getLogin());
        assertEquals("senha-encoded", entity.getSenha());
        assertEquals(TipoUsuario.CLIENTE, entity.getTipoUsuario());
        assertEquals(ULTIMA_SENHA, entity.getDataUltimaAlteracaoSenha());
        assertEquals(CREATED_AT, entity.getCreatedAt());
        assertEquals(UPDATED_AT, entity.getUpdatedAt());
        assertNotNull(entity.getEndereco());
        assertEquals("Rua A", entity.getEndereco().getRua());
    }

    @Test
    @DisplayName("toJpaEntity deve mapear usuário sem endereço")
    void toJpaEntityDeveMapearUsuarioSemEndereco() {
        Usuario usuario = Usuario.builder().id(1L).nome("João Silva").build();

        UsuarioJpaEntity entity = UsuarioJpaMapper.toJpaEntity(usuario);

        assertNull(entity.getEndereco());
    }

    @Test
    @DisplayName("toJpaEntity deve retornar null quando o domínio for null")
    void toJpaEntityDeveRetornarNullQuandoDominioForNull() {
        assertNull(UsuarioJpaMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new UsuarioJpaMapper());
    }
}