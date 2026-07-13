package com.fiap.fomezero.infrastructure.web.controller.helpers;

import com.fiap.fomezero.application.dto.request.LoginRequest;
import com.fiap.fomezero.application.dto.response.LoginResponse;
import com.fiap.fomezero.domain.model.TipoUsuario;

public class AuthControllerTestHelper {

    private AuthControllerTestHelper() {
    }

    public static LoginRequest buildLoginRequest() {
        return new LoginRequest("joao.silva", "Senha@123");
    }

    public static LoginResponse buildLoginResponse() {
        return LoginResponse.builder()
                .id(1L)
                .nome("João da Silva")
                .email("joao@email.com")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .autenticado(true)
                .token("token-jwt-fake")
                .build();
    }
}