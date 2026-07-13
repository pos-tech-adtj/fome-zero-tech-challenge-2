package com.fiap.fomezero.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Schema(description = "Dados para atualização de um restaurante. Apenas os campos informados serão atualizados.")
public record RestauranteUpdateRequest (

        @Schema(description = "Nome do restaurante", example = "")
        String nome,

        @Schema(description = "Endereço do restaurante")
        @Valid
        EnderecoUpdateRequest endereco,

        @Schema(description = "Tipo de Cozinha", example = "")
        String tipoCozinha,

        @Schema(description = "Horario de funcionamento)", example = "")
        String horarioFuncionamento,

        @Schema(description = "ID de usuário do dono do restaurante", example = "")
        Long donoId

){
}
