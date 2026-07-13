package com.fiap.fomezero.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para alteração de senha do usuário")
public record UsuarioSenhaRequest(

        @Schema(description = "Senha atual do usuário", example = "Senha@123")
        @NotBlank(message = "Senha atual é obrigatória")
        String senhaAtual,

        @Schema(description = "Nova senha (mínimo 8 caracteres)", example = "NovaSenha@456")
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres")
        String novaSenha
) {
}
