package com.fiap.fomezero.infrastructure.web.controller.helpers;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.TipoUsuario;

public class UsuarioControllerTestHelper {

    private UsuarioControllerTestHelper() {
    }

    public static UsuarioCreateRequest buildCreateRequest() {
        return new UsuarioCreateRequest(
                "João da Silva",
                "joao@email.com",
                "joao.silva",
                "Senha@123",
                TipoUsuario.CLIENTE,
                null
        );
    }

    public static UsuarioUpdateRequest buildUpdateRequest() {
        return new UsuarioUpdateRequest(
                "João da Silva Junior",
                null,
                null,
                null,
                null
        );
    }

    public static UsuarioSenhaRequest buildSenhaRequest() {
        return new UsuarioSenhaRequest("Senha@123", "NovaSenha@456");
    }

    public static UsuarioSenhaRequest buildSenhaRequestComSenhaAtualInvalida() {
        return new UsuarioSenhaRequest("SenhaErrada", "NovaSenha@456");
    }

    public static UsuarioSenhaRequest buildSenhaRequestComNovaSenhaIgualAtual() {
        return new UsuarioSenhaRequest("Senha@123", "Senha@123");
    }

    public static UsuarioResponse buildResponse() {
        return UsuarioResponse.builder()
                .id(1L)
                .nome("João da Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();
    }
}