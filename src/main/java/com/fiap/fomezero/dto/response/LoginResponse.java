package com.fiap.fomezero.dto.response;

import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Dados retornados após autenticação bem-sucedida")
public class LoginResponse {

    @Schema(description = "ID do usuário autenticado", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário autenticado", example = "João da Silva")
    private String nome;

    @Schema(description = "Email do usuário autenticado", example = "joao@email.com")
    private String email;

    @Schema(description = "Tipo do usuário", example = "CLIENTE")
    private TipoUsuario tipoUsuario;

    @Schema(description = "Indica se o usuário foi autenticado com sucesso", example = "true")
    private boolean autenticado;

    public static LoginResponse from(Usuario user) {
        return LoginResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .tipoUsuario(user.getTipoUsuario())
                .autenticado(true)
                .build();
    }
}
