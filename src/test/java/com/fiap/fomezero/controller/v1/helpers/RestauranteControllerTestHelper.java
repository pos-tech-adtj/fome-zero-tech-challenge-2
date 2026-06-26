package com.fiap.fomezero.controller.v1.helpers;

import com.fiap.fomezero.application.dto.request.EnderecoRequest;
import com.fiap.fomezero.application.dto.request.EnderecoUpdateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.EnderecoResponse;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;

public class RestauranteControllerTestHelper {

    private RestauranteControllerTestHelper() {
    }

    public static Usuario buildDono() {
        return Usuario.builder()
                .id(1L)
                .nome("Dono Teste")
                .tipoUsuario(TipoUsuario.DONO_RESTAURANTE)
                .build();
    }

    public static Usuario buildCliente() {
        return Usuario.builder()
                .id(2L)
                .nome("Cliente Teste")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();
    }

    public static RestauranteCreateRequest buildCreateRequest(Long donoId) {
        return new RestauranteCreateRequest(
                "Sukiya",
                buildEnderecoRequest(),
                "Japonesa",
                "11h as 23h",
                donoId
        );
    }

    public static RestauranteUpdateRequest buildUpdateRequest(Long donoId) {
        return new RestauranteUpdateRequest(
                "Sukiya Atualizado",
                buildEnderecoUpdateRequest(),
                "Japonesa",
                "12h as 23h",
                donoId
        );
    }

    public static RestauranteResponse buildResponse(Long donoId) {
        return RestauranteResponse.builder()
                .id(1L)
                .nome("Sukiya")
                .tipoCozinha("Japonesa")
                .horarioFuncionamento("11h as 23h")
                .donoId(donoId)
                .endereco(buildEnderecoResponse())
                .build();
    }

    public static EnderecoRequest buildEnderecoRequest() {
        return new EnderecoRequest(
                "Rua dos Testes",
                100,
                "Sala 1",
                "Centro",
                "São Paulo",
                "SP",
                "01310-100"
        );
    }

    public static EnderecoUpdateRequest buildEnderecoUpdateRequest() {
        return new EnderecoUpdateRequest(
                "Rua dos Testes",
                100,
                "Sala 1",
                "Centro",
                "São Paulo",
                "SP",
                "01310-100"
        );
    }

    public static EnderecoResponse buildEnderecoResponse() {
        return EnderecoResponse.builder()
                .rua("Rua dos Testes")
                .numero(100)
                .complemento("Sala 1")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01310-100")
                .build();
    }
}
