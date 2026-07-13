package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.EnderecoRequest;
import com.fiap.fomezero.application.dto.request.EnderecoUpdateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteMapperTest {

    private Usuario novoDono(Long id) {
        return Usuario.builder()
                .id(id)
                .nome("Dono " + id)
                .email("dono" + id + "@email.com")
                .login("dono" + id)
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();
    }

    private Restaurante novoRestaurante() {
        return Restaurante.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("09:00 - 22:00")
                .dono(novoDono(1L))
                .endereco(Endereco.builder().rua("Rua A").numero(10).cidade("São Paulo").estado("SP").cep("01000-000").build())
                .build();
    }

    @Test
    @DisplayName("toEntity deve mapear request e associar o dono")
    void toEntityDeveMapearRequestEAssociarDono() {
        EnderecoRequest enderecoRequest = new EnderecoRequest("Rua A", 10, null, "Centro", "São Paulo", "SP", "01000-000");
        RestauranteCreateRequest request = new RestauranteCreateRequest(
                "Cantina Fiap", enderecoRequest, "Italiana", "09:00 - 22:00", 1L);
        Usuario dono = novoDono(1L);

        Restaurante restaurante = RestauranteMapper.toEntity(request, dono);

        assertEquals("Cantina Fiap", restaurante.getNome());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals("09:00 - 22:00", restaurante.getHorarioFuncionamento());
        assertEquals(dono, restaurante.getDono());
        assertNotNull(restaurante.getEndereco());
        assertEquals("Rua A", restaurante.getEndereco().getRua());
    }

    @Test
    @DisplayName("toEntity deve aceitar request sem endereço")
    void toEntityDeveAceitarRequestSemEndereco() {
        RestauranteCreateRequest request = new RestauranteCreateRequest(
                "Cantina Fiap", null, "Italiana", "09:00 - 22:00", 1L);

        Restaurante restaurante = RestauranteMapper.toEntity(request, novoDono(1L));

        assertNull(restaurante.getEndereco());
    }

    @Test
    @DisplayName("toResponse deve mapear restaurante com endereço e dono")
    void toResponseDeveMapearRestaurante() {
        RestauranteResponse response = RestauranteMapper.toResponse(novoRestaurante());

        assertEquals(1L, response.getId());
        assertEquals("Cantina Fiap", response.getNome());
        assertEquals("Italiana", response.getTipoCozinha());
        assertEquals("09:00 - 22:00", response.getHorarioFuncionamento());
        assertEquals(1L, response.getDonoId());
        assertNotNull(response.getEndereco());
        assertEquals("Rua A", response.getEndereco().getRua());
    }

    @Test
    @DisplayName("toResponse deve mapear restaurante sem endereço")
    void toResponseDeveMapearRestauranteSemEndereco() {
        Restaurante restaurante = novoRestaurante();
        restaurante.setEndereco(null);

        RestauranteResponse response = RestauranteMapper.toResponse(restaurante);

        assertNull(response.getEndereco());
        assertEquals(1L, response.getDonoId());
    }

    @Test
    @DisplayName("updateEntity deve atualizar todos os campos informados, inclusive o dono")
    void updateEntityDeveAtualizarCamposInformados() {
        Restaurante restaurante = novoRestaurante();
        Usuario novoDono = novoDono(2L);
        RestauranteUpdateRequest request = new RestauranteUpdateRequest(
                "Cantina Nova",
                new EnderecoUpdateRequest("Rua B", 20, null, null, null, null, null),
                "Japonesa",
                "10:00 - 23:00",
                2L);

        RestauranteMapper.updateEntity(restaurante, request, novoDono);

        assertEquals("Cantina Nova", restaurante.getNome());
        assertEquals("Japonesa", restaurante.getTipoCozinha());
        assertEquals("10:00 - 23:00", restaurante.getHorarioFuncionamento());
        assertEquals("Rua B", restaurante.getEndereco().getRua());
        assertEquals(2L, restaurante.getDono().getId());
        assertNotNull(restaurante.getUpdatedAt());
    }

    @Test
    @DisplayName("updateEntity deve ignorar campos nulos e manter o dono atual")
    void updateEntityDeveIgnorarCamposNulos() {
        Restaurante restaurante = novoRestaurante();
        RestauranteUpdateRequest request = new RestauranteUpdateRequest(null, null, null, null, null);

        RestauranteMapper.updateEntity(restaurante, request, null);

        assertEquals("Cantina Fiap", restaurante.getNome());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals("09:00 - 22:00", restaurante.getHorarioFuncionamento());
        assertEquals(1L, restaurante.getDono().getId());
        assertEquals("Rua A", restaurante.getEndereco().getRua());
    }

    @Test
    @DisplayName("updateEntity deve criar um novo endereço quando o restaurante ainda não possui um")
    void updateEntityDeveCriarEnderecoQuandoRestauranteNaoPossui() {
        Restaurante restaurante = novoRestaurante();
        restaurante.setEndereco(null);
        RestauranteUpdateRequest request = new RestauranteUpdateRequest(
                null,
                new EnderecoUpdateRequest("Rua Nova", 55, null, null, "Campinas", "SP", "13000-000"),
                null, null, null);

        RestauranteMapper.updateEntity(restaurante, request, null);

        assertNotNull(restaurante.getEndereco());
        assertEquals("Rua Nova", restaurante.getEndereco().getRua());
        assertEquals("Campinas", restaurante.getEndereco().getCidade());
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new RestauranteMapper());
    }
}