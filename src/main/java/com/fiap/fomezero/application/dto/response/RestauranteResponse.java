package com.fiap.fomezero.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados do restaurante retornados pela API")
public class RestauranteResponse {

    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "")
    private String nome;

    @Schema(description = "Endereço do restaurante")
    private EnderecoResponse endereco;

    @Schema(description = "Tipo de Cozinha", example = "")
    private String tipoCozinha;

    @Schema(description = "Horario de funcionamento)", example = "")
    private String horarioFuncionamento;

    @Schema(description = "Nome do dono do restaurante", example = "")
    private String donoNome;

    @Schema(description = "ID de usuário do dono do restaurante", example = "")
    private Long donoId;
}
