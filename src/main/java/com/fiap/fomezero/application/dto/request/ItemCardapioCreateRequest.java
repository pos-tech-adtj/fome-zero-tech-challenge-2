package com.fiap.fomezero.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para criação de um novo item de cardápio.")
public record ItemCardapioCreateRequest(

        @Schema(description = "Nome do item de cardápio", example = "Pizza Margherita")
        @NotBlank(message = "Nome do item de cardápio é obrigatório")
        String nome,

        @Schema(description = "Descrição do item de cardápio", example = "Pizza com molho de tomate, mussarela e manjericão")
        String descricao,

        @Schema(description = "Preço do item de cardápio", example = "49.90")
        @NotNull(message = "Preço do item de cardápio é obrigatório")
        @Positive(message = "Preço do item de cardápio deve ser maior que zero")
        BigDecimal preco,

        @Schema(description = "Indica se o item está disponível apenas para consumo no restaurante", example = "false")
        @NotNull(message = "Disponibilidade apenas no restaurante é obrigatória")
        Boolean apenasNoRestaurante,

        @Schema(description = "Caminho ou URL da foto do item de cardápio", example = "https://cdn.fomezero.com/itens/pizza-margherita.png")
        String fotoPath,

        @Schema(description = "ID do restaurante ao qual o item pertence", example = "1")
        @NotNull(message = "ID do restaurante é obrigatório")
        Long restauranteId

) {
}
