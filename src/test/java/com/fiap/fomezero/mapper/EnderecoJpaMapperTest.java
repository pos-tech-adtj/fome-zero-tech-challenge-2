package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.infrastructure.persistence.entity.EnderecoJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoJpaMapperTest {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 2, 1, 10, 0);

    @Test
    @DisplayName("toDomain deve mapear todos os campos da entidade JPA")
    void toDomainDeveMapearTodosOsCampos() {
        EnderecoJpaEntity entity = EnderecoJpaEntity.builder()
                .id(1L)
                .rua("Rua das Flores")
                .numero(123)
                .complemento("Apto 45")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01310-100")
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();

        Endereco endereco = EnderecoJpaMapper.toDomain(entity);

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
    @DisplayName("toDomain deve retornar null quando a entidade for null")
    void toDomainDeveRetornarNullQuandoEntidadeForNull() {
        assertNull(EnderecoJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("toJpaEntity deve mapear todos os campos do domínio")
    void toJpaEntityDeveMapearTodosOsCampos() {
        Endereco endereco = Endereco.builder()
                .id(1L)
                .rua("Rua das Flores")
                .numero(123)
                .complemento("Apto 45")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01310-100")
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();

        EnderecoJpaEntity entity = EnderecoJpaMapper.toJpaEntity(endereco);

        assertEquals(1L, entity.getId());
        assertEquals("Rua das Flores", entity.getRua());
        assertEquals(123, entity.getNumero());
        assertEquals("Apto 45", entity.getComplemento());
        assertEquals("Centro", entity.getBairro());
        assertEquals("São Paulo", entity.getCidade());
        assertEquals("SP", entity.getEstado());
        assertEquals("01310-100", entity.getCep());
        assertEquals(CREATED_AT, entity.getCreatedAt());
        assertEquals(UPDATED_AT, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("toJpaEntity deve retornar null quando o domínio for null")
    void toJpaEntityDeveRetornarNullQuandoDominioForNull() {
        assertNull(EnderecoJpaMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new EnderecoJpaMapper());
    }
}