package com.fiap.fomezero.infrastructure.web.controller;

import com.fiap.fomezero.application.dto.request.ItemCardapioCreateRequest;
import com.fiap.fomezero.application.dto.request.ItemCardapioUpdateRequest;
import com.fiap.fomezero.application.dto.response.ItemCardapioResponse;
import com.fiap.fomezero.application.usecase.itemcardapio.AtualizarItemCardapioUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.BuscarItemCardapioPorIdUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.CriarItemCardapioUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.DeletarItemCardapioUseCase;
import com.fiap.fomezero.application.usecase.itemcardapio.ListarItensCardapioPorRestauranteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Itens de Cardápio", description = "Gerenciamento de itens de cardápio dos restaurantes")
@RestController
@RequestMapping("/v1/itens-cardapio")
@RequiredArgsConstructor
public class ItemCardapioController {

    private final CriarItemCardapioUseCase criarItemCardapioUseCase;
    private final ListarItensCardapioPorRestauranteUseCase listarItensCardapioPorRestauranteUseCase;
    private final BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;
    private final AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;
    private final DeletarItemCardapioUseCase deletarItemCardapioUseCase;

    @Operation(summary = "Criar item de cardápio", description = "Cadastra um novo item de cardápio para um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item de cardápio criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ItemCardapioResponse> criarItemCardapio(
            @Valid @RequestBody ItemCardapioCreateRequest request) {
        ItemCardapioResponse itemCardapio = criarItemCardapioUseCase.criarItemCardapio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCardapio);
    }

    @Operation(summary = "Listar itens de cardápio por restaurante", description = "Retorna os itens de cardápio de um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de itens de cardápio retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ItemCardapioResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content)
    })
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ItemCardapioResponse>> listarPorRestaurante(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long restauranteId) {
        List<ItemCardapioResponse> itensCardapio = listarItensCardapioPorRestauranteUseCase
                .listarPorRestaurante(restauranteId);
        return ResponseEntity.ok(itensCardapio);
    }

    @Operation(summary = "Buscar item de cardápio por ID", description = "Retorna os dados de um item de cardápio pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item de cardápio encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item de cardápio não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapioResponse> buscarPorId(
            @Parameter(description = "ID do item de cardápio", required = true) @PathVariable Long id) {
        ItemCardapioResponse itemCardapio = buscarItemCardapioPorIdUseCase.buscarPorId(id);
        return ResponseEntity.ok(itemCardapio);
    }

    @Operation(summary = "Atualizar item de cardápio", description = "Atualiza os dados de um item de cardápio existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item de cardápio atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item de cardápio ou restaurante não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapioResponse> atualizarItemCardapio(
            @Parameter(description = "ID do item de cardápio", required = true) @PathVariable Long id,
            @Valid @RequestBody ItemCardapioUpdateRequest request) {
        ItemCardapioResponse itemCardapio = atualizarItemCardapioUseCase.atualizarItemCardapio(id, request);
        return ResponseEntity.ok(itemCardapio);
    }

    @Operation(summary = "Deletar item de cardápio", description = "Remove permanentemente um item de cardápio pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item de cardápio removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item de cardápio não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItemCardapio(
            @Parameter(description = "ID do item de cardápio", required = true) @PathVariable Long id) {
        deletarItemCardapioUseCase.deletarItemCardapio(id);
        return ResponseEntity.noContent().build();
    }
}
