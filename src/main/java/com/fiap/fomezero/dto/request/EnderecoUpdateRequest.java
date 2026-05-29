package com.fiap.fomezero.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização do endereço do usuário. Apenas os campos informados serão atualizados.")
public record EnderecoUpdateRequest(

        @Schema(description = "Nome da rua", example = "Rua das Flores")
        String rua,

        @Schema(description = "Número do imóvel", example = "123")
        Integer numero,

        @Schema(description = "Complemento do endereço", example = "Apto 45")
        String complemento,

        @Schema(description = "Bairro", example = "Centro")
        String bairro,

        @Schema(description = "Cidade", example = "São Paulo")
        String cidade,

        @Schema(description = "Estado (sigla)", example = "SP")
        String estado,

        @Schema(description = "CEP", example = "01310-100")
        String cep

) {}
