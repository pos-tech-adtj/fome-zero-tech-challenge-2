package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.EnderecoRequest;
import com.fiap.fomezero.application.dto.request.EnderecoUpdateRequest;
import com.fiap.fomezero.application.dto.response.EnderecoResponse;
import com.fiap.fomezero.domain.model.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoMapperTest {

    private EnderecoRequest novoRequest() {
        return new EnderecoRequest("Rua das Flores", 123, "Apto 45", "Centro", "São Paulo", "SP", "01310-100");
    }

    private Endereco novoEndereco() {
        return Endereco.builder()
                .id(1L)
                .rua("Rua das Flores")
                .numero(123)
                .complemento("Apto 45")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01310-100")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("toEntity deve mapear todos os campos do request")
    void toEntityDeveMapearTodosOsCampos() {
        EnderecoRequest request = novoRequest();

        Endereco endereco = EnderecoMapper.toEntity(request);

        assertNotNull(endereco);
        assertEquals("Rua das Flores", endereco.getRua());
        assertEquals(123, endereco.getNumero());
        assertEquals("Apto 45", endereco.getComplemento());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("01310-100", endereco.getCep());
        assertNotNull(endereco.getCreatedAt());
        assertNotNull(endereco.getUpdatedAt());
    }

    @Test
    @DisplayName("toEntity deve retornar null quando request for null")
    void toEntityDeveRetornarNullQuandoRequestForNull() {
        assertNull(EnderecoMapper.toEntity(null));
    }

    @Test
    @DisplayName("toResponse deve mapear todos os campos do domínio")
    void toResponseDeveMapearTodosOsCampos() {
        Endereco endereco = novoEndereco();

        EnderecoResponse response = EnderecoMapper.toResponse(endereco);

        assertNotNull(response);
        assertEquals("Rua das Flores", response.getRua());
        assertEquals(123, response.getNumero());
        assertEquals("Apto 45", response.getComplemento());
        assertEquals("Centro", response.getBairro());
        assertEquals("São Paulo", response.getCidade());
        assertEquals("SP", response.getEstado());
        assertEquals("01310-100", response.getCep());
    }

    @Test
    @DisplayName("toResponse deve retornar null quando endereço for null")
    void toResponseDeveRetornarNullQuandoEnderecoForNull() {
        assertNull(EnderecoMapper.toResponse(null));
    }

    @Test
    @DisplayName("updateEntity deve atualizar todos os campos informados")
    void updateEntityDeveAtualizarTodosOsCamposInformados() {
        Endereco endereco = novoEndereco();
        EnderecoUpdateRequest request = new EnderecoUpdateRequest(
                "Avenida Paulista", 999, "Sala 10", "Bela Vista", "Campinas", "RJ", "13000-000");

        EnderecoMapper.updateEntity(endereco, request);

        assertEquals("Avenida Paulista", endereco.getRua());
        assertEquals(999, endereco.getNumero());
        assertEquals("Sala 10", endereco.getComplemento());
        assertEquals("Bela Vista", endereco.getBairro());
        assertEquals("Campinas", endereco.getCidade());
        assertEquals("RJ", endereco.getEstado());
        assertEquals("13000-000", endereco.getCep());
        assertNotNull(endereco.getUpdatedAt());
    }

    @Test
    @DisplayName("updateEntity deve ignorar campos nulos e atualizar apenas os preenchidos")
    void updateEntityDeveIgnorarCamposNulos() {
        Endereco endereco = novoEndereco();
        EnderecoUpdateRequest request = new EnderecoUpdateRequest(
                "Avenida Paulista", null, null, null, null, null, null);

        EnderecoMapper.updateEntity(endereco, request);

        assertEquals("Avenida Paulista", endereco.getRua());
        assertEquals(123, endereco.getNumero());
        assertEquals("Apto 45", endereco.getComplemento());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("01310-100", endereco.getCep());
    }

    @Test
    @DisplayName("updateEntity não deve alterar o endereço quando request for null")
    void updateEntityNaoDeveAlterarQuandoRequestForNull() {
        Endereco endereco = novoEndereco();
        LocalDateTime updatedAtOriginal = endereco.getUpdatedAt();

        EnderecoMapper.updateEntity(endereco, null);

        assertEquals("Rua das Flores", endereco.getRua());
        assertEquals(updatedAtOriginal, endereco.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new EnderecoMapper());
    }
}