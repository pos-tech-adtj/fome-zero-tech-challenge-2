package com.fiap.fomezero.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados do item de cardápio retornados pela API")
public class ItemCardapioResponse {

    @Schema(description = "ID único do item de cardápio", example = "1")
    private Long id;

    @Schema(description = "Nome do item de cardápio", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição do item de cardápio", example = "Pizza com molho de tomate, mussarela e manjericão")
    private String descricao;

    @Schema(description = "Preço do item de cardápio", example = "49.90")
    private BigDecimal preco;

    @Schema(description = "Indica se o item está disponível apenas para consumo no restaurante", example = "false")
    private Boolean apenasNoRestaurante;

    @Schema(description = "Caminho ou URL da foto do item de cardápio", example = "https://cdn.fomezero.com/itens/pizza-margherita.png")
    private String fotoPath;

    @Schema(description = "ID do restaurante ao qual o item pertence", example = "1")
    private Long restauranteId;

    @Schema(description = "Nome do restaurante ao qual o item pertence", example = "Cantina Fiap")
    private String restauranteNome;
}
