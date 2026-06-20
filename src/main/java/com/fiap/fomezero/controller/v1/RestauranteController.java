package com.fiap.fomezero.controller.v1;

import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.application.usecase.restaurante.*;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurantes", description = "Gerenciamento de restaurante da plataforma de sistema de restaurantes")
@RestController
@RequestMapping("/v1/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    private final BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    private final CriarRestauranteUseCase criarRestauranteUseCase;

    private final DeletarRestauranteUseCase deletarRestauranteUseCase;

    private final ListarRestaurantesUseCase listarRestaurantesUseCase;

    @Operation(summary = "Criar restaurante", description = "Cadastra um novo restaurante na plataforma")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou usuário não possui perfil de dono de restaurante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RestauranteResponse> criarRestaurante(@Valid @RequestBody RestauranteCreateRequest request) {
        RestauranteResponse restaurante = criarRestauranteUseCase.criarRestaurante(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }

    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Restaurante já cadastrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizarRestaurante(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id,
            @Valid @RequestBody RestauranteUpdateRequest request) {
        RestauranteResponse restaurante = atualizarRestauranteUseCase.atualizarRestaurante(id, request);
        return ResponseEntity.ok(restaurante);
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados de um restaurante pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarRestaurantePorId(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id) {
        RestauranteResponse restaurante = buscarRestaurantePorIdUseCase.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }

    @Operation(summary = "Listar restaurante", description = "Retorna a lista de todos os restaurantes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = RestauranteResponse.class))))
    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> listarRestaurantes() {
        List<RestauranteResponse> restaurante = listarRestaurantesUseCase.listarRestaurantes();
        return ResponseEntity.ok(restaurante);
    }

    @Operation(summary = "Deletar usuário", description = "Remove permanentemente um usuário pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id) {
        deletarRestauranteUseCase.deletarRestaurante(id);
        return ResponseEntity.noContent().build();
    }
}
