package com.fiap.fomezero.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados do endereço do usuário")
public class EnderecoResponse {

    @Schema(description = "Nome da rua", example = "Rua das Flores")
    private String rua;

    @Schema(description = "Número do imóvel", example = "123")
    private Integer numero;

    @Schema(description = "Complemento", example = "Apto 45")
    private String complemento;

    @Schema(description = "Bairro", example = "Centro")
    private String bairro;

    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado (sigla)", example = "SP")
    private String estado;

    @Schema(description = "CEP", example = "01310-100")
    private String cep;
}
