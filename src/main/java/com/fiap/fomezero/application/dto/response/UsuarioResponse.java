package com.fiap.fomezero.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.fomezero.domain.model.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados do usuário retornados pela API")
public class UsuarioResponse {

    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nome;

    @Schema(description = "Email do usuário", example = "joao@email.com")
    private String email;

    @Schema(description = "Login de acesso", example = "joao.silva")
    private String login;

    @Schema(description = "Tipo do usuário", example = "CLIENTE")
    private TipoUsuario tipoUsuario;

    @Schema(description = "Endereço do usuário")
    private EnderecoResponse endereco;
}
