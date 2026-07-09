package com.fiap.fomezero.infrastructure.web.controller.helpers;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;

import java.math.BigDecimal;

public class ItemCardapioControllerTestHelper {

    private ItemCardapioControllerTestHelper() {
    }

    public static ItemCardapioCreateRequest buildCreateRequest(Long restauranteId) {
        return new ItemCardapioCreateRequest(
                "Pizza Margherita",
                "Molho de tomate, mussarela e manjericão",
                BigDecimal.valueOf(49.90),
                false,
                "https://cdn.fomezero.com/itens/pizza-margherita.png",
                restauranteId
        );
    }

    public static ItemCardapioUpdateRequest buildUpdateRequest() {
        return new ItemCardapioUpdateRequest(
                "Pizza Margherita Grande",
                null,
                null,
                null,
                null,
                null
        );
    }

    public static ItemCardapioUpdateRequest buildUpdateRequestComRestaurante(Long restauranteId) {
        return new ItemCardapioUpdateRequest(
                null,
                null,
                null,
                null,
                null,
                restauranteId
        );
    }

    public static ItemCardapioResponse buildResponse(Long restauranteId) {
        return ItemCardapioResponse.builder()
                .id(1L)
                .nome("Pizza Margherita")
                .descricao("Molho de tomate, mussarela e manjericão")
                .preco(BigDecimal.valueOf(49.90))
                .apenasNoRestaurante(false)
                .fotoPath("https://cdn.fomezero.com/itens/pizza-margherita.png")
                .restauranteId(restauranteId)
                .restauranteNome("Cantina Fiap")
                .build();
    }
}