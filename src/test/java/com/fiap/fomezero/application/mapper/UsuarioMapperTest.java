package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.EnderecoRequest;
import com.fiap.fomezero.application.dto.request.EnderecoUpdateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private Usuario novoUsuario() {
        return Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .senha("senha-encoded")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .endereco(Endereco.builder().rua("Rua A").numero(10).cidade("São Paulo").estado("SP").cep("01000-000").build())
                .build();
    }

    @Test
    @DisplayName("toEntity deve mapear request completo, incluindo endereço e datas")
    void toEntityDeveMapearRequestCompleto() {
        EnderecoRequest enderecoRequest = new EnderecoRequest("Rua A", 10, null, "Centro", "São Paulo", "SP", "01000-000");
        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "João Silva", "joao@email.com", "joao.silva", "senha123", TipoUsuario.CLIENTE, enderecoRequest);

        Usuario usuario = UsuarioMapper.toEntity(request);

        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("joao.silva", usuario.getLogin());
        assertEquals("senha123", usuario.getSenha());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
        assertNotNull(usuario.getDataUltimaAlteracaoSenha());
        assertNotNull(usuario.getCreatedAt());
        assertNotNull(usuario.getUpdatedAt());
        assertNotNull(usuario.getEndereco());
        assertEquals("Rua A", usuario.getEndereco().getRua());
    }

    @Test
    @DisplayName("toEntity deve aceitar request sem endereço")
    void toEntityDeveAceitarRequestSemEndereco() {
        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "João Silva", "joao@email.com", "joao.silva", "senha123", TipoUsuario.DONO_RESTAURANTE, null);

        Usuario usuario = UsuarioMapper.toEntity(request);

        assertNull(usuario.getEndereco());
        assertEquals(TipoUsuario.DONO_RESTAURANTE, usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("toResponse deve mapear usuário com endereço")
    void toResponseDeveMapearUsuarioComEndereco() {
        UsuarioResponse response = UsuarioMapper.toResponse(novoUsuario());

        assertEquals(1L, response.getId());
        assertEquals("João Silva", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
        assertEquals("joao.silva", response.getLogin());
        assertEquals(TipoUsuario.CLIENTE, response.getTipoUsuario());
        assertNotNull(response.getEndereco());
        assertEquals("Rua A", response.getEndereco().getRua());
    }

    @Test
    @DisplayName("toResponse deve mapear usuário sem endereço")
    void toResponseDeveMapearUsuarioSemEndereco() {
        Usuario usuario = novoUsuario();
        usuario.setEndereco(null);

        UsuarioResponse response = UsuarioMapper.toResponse(usuario);

        assertNull(response.getEndereco());
    }

    @Test
    @DisplayName("updateEntity deve atualizar todos os campos informados")
    void updateEntityDeveAtualizarCamposInformados() {
        Usuario usuario = novoUsuario();
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(
                "Maria Souza", "maria@email.com", "maria.souza", TipoUsuario.DONO_RESTAURANTE,
                new EnderecoUpdateRequest("Rua B", 20, null, null, null, null, null));

        UsuarioMapper.updateEntity(usuario, request);

        assertEquals("Maria Souza", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("maria.souza", usuario.getLogin());
        assertEquals(TipoUsuario.DONO_RESTAURANTE, usuario.getTipoUsuario());
        assertEquals("Rua B", usuario.getEndereco().getRua());
        assertEquals(20, usuario.getEndereco().getNumero());
        assertNotNull(usuario.getUpdatedAt());
    }

    @Test
    @DisplayName("updateEntity deve ignorar campos nulos")
    void updateEntityDeveIgnorarCamposNulos() {
        Usuario usuario = novoUsuario();
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, null, null, null, null);

        UsuarioMapper.updateEntity(usuario, request);

        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("joao.silva", usuario.getLogin());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
        assertEquals("Rua A", usuario.getEndereco().getRua());
    }

    @Test
    @DisplayName("updateEntity deve criar um novo endereço quando o usuário ainda não possui um")
    void updateEntityDeveCriarEnderecoQuandoUsuarioNaoPossui() {
        Usuario usuario = novoUsuario();
        usuario.setEndereco(null);
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, null, null, null,
                new EnderecoUpdateRequest("Rua Nova", 55, null, null, "Campinas", "SP", "13000-000"));

        UsuarioMapper.updateEntity(usuario, request);

        assertNotNull(usuario.getEndereco());
        assertEquals("Rua Nova", usuario.getEndereco().getRua());
        assertEquals(55, usuario.getEndereco().getNumero());
        assertEquals("Campinas", usuario.getEndereco().getCidade());
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new UsuarioMapper());
    }
}