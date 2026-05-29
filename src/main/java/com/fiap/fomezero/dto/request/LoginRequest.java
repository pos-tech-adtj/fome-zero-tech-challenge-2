package com.fiap.fomezero.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais de login")
public record LoginRequest(

        @Schema(description = "Login de acesso", example = "joao.silva")
        @NotBlank(message = "Login é obrigatório") String login,

        @Schema(description = "Senha de acesso", example = "Senha@123")
        @NotBlank(message = "Senha é obrigatória") String senha
) {
}
