package com.fiap.fomezero.dto.request;

import com.fiap.fomezero.domain.model.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Dados para atualização de um usuário. Apenas os campos informados serão atualizados.")
public record UsuarioUpdateRequest(

        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String nome,

        @Schema(description = "Email do usuário", example = "joao@email.com")
        @Email
        String email,

        @Schema(description = "Login de acesso do usuário", example = "joao.silva")
        String login,

        @Schema(description = "Tipo do usuário", example = "CLIENTE")
        TipoUsuario tipoUsuario,

        EnderecoUpdateRequest endereco
) {
}
