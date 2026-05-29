package com.fiap.fomezero.dto.request;

import com.fiap.fomezero.domain.model.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo usuário")
public record UsuarioCreateRequest(

        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        @NotBlank(message = "Nome completo do usuário é obrigatório")
        String nome,

        @Schema(description = "Email do usuário", example = "joao@email.com")
        @NotBlank(message = "Email do usuário é obrigatório")
        @Email
        String email,

        @Schema(description = "Login de acesso do usuário", example = "joao.silva")
        @NotBlank(message = "Login de acesso do usuário é obrigatório")
        String login,

        @Schema(description = "Senha de acesso (mínimo 8 caracteres)", example = "Senha@123")
        @NotBlank(message = "Senha de acesso é obrigatório")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,

        @Schema(description = "Tipo do usuário", example = "CLIENTE")
        @NotNull(message = "Tipo do usuário é obrigatório")
        TipoUsuario tipoUsuario,

        @Valid
        EnderecoRequest endereco
) {
}
