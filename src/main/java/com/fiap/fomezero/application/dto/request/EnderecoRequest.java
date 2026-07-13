package com.fiap.fomezero.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados do endereço do usuário")
public record EnderecoRequest(

        @Schema(description = "Nome da rua", example = "Rua das Flores")
        @NotBlank(message = "Nome da rua é obrigatório")
        String rua,

        @Schema(description = "Número do imóvel", example = "123")
        @NotNull(message = "Número do imóvel é obrigatório")
        @Positive
        Integer numero,

        @Schema(description = "Complemento do endereço", example = "Apto 45")
        String complemento,

        @Schema(description = "Bairro", example = "Centro")
        String bairro,

        @Schema(description = "Cidade", example = "São Paulo")
        @NotBlank(message = " Cidade é obrigatório")
        String cidade,

        @Schema(description = "Estado (sigla)", example = "SP")
        @NotBlank(message = " Estado é obrigatório")
        String estado,

        @Schema(description = "CEP", example = "01310-100")
        @NotBlank(message = "CEP é obrigatório")
        String cep

) {}
