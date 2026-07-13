package com.fiap.fomezero.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para atualização de um item de cardápio. Apenas os campos informados serão atualizados.")
public record ItemCardapioUpdateRequest(

        @Schema(description = "Nome do item de cardápio", example = "Pizza Margherita")
        String nome,

        @Schema(description = "Descrição do item de cardápio", example = "Pizza com molho de tomate, mussarela e manjericão")
        String descricao,

        @Schema(description = "Preço do item de cardápio", example = "49.90")
        @Positive(message = "Preço do item de cardápio deve ser maior que zero")
        BigDecimal preco,

        @Schema(description = "Indica se o item está disponível apenas para consumo no restaurante", example = "false")
        Boolean apenasNoRestaurante,

        @Schema(description = "Caminho ou URL da foto do item de cardápio", example = "https://cdn.fomezero.com/itens/pizza-margherita.png")
        String fotoPath,

        @Schema(description = "ID do restaurante ao qual o item pertence", example = "1")
        Long restauranteId

) {
}
