package com.fiap.fomezero.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criação de um novo restaurante.")
public record RestauranteCreateRequest(

        @Schema(description = "Nome do restaurante", example = "")
        @NotBlank(message = "Nome do restaurante é obrigatório")
        String nome,

        @Schema(description = "Endereço do restaurante")
        @Valid
        EnderecoRequest endereco,

        @Schema(description = "Tipo de Cozinha", example = "")
        @NotBlank(message = "Tipo de Cozinha é obrigatório")
        String tipoCozinha,

        @Schema(description = "Horario de funcionamento)", example = "")
        @NotBlank(message = "Horario de funcionamento é obrigatório")
        String horarioFuncionamento,

        @Schema(description = "ID de usuário do dono do restaurante", example = "")
        @NotNull(message = "ID de usuário do dono do restaurante é obrigatório")
        Long donoId

) {
}
