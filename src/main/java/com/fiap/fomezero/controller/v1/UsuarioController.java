package com.fiap.fomezero.controller.v1;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Usuários", description = "Gerenciamento de usuários da plataforma de sistema de restaurantes")
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;

    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    private final BuscarUsuarioUseCase buscarUsuarioUseCase;

    private final AlterarSenhaUseCase alterarSenhaUseCase;

    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário na plataforma")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email ou login já cadastrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioResponse usuario = criarUsuarioUseCase.criarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @Operation(summary = "Deletar usuário", description = "Remove permanentemente um usuário pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id) {
        deletarUsuarioUseCase.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email ou login já cadastrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request) {
        UsuarioResponse usuario = atualizarUsuarioUseCase.atualizarUsuario(id, request);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id) {
        UsuarioResponse usuario = buscarUsuarioUseCase.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Listar usuários", description = "Retorna a lista de todos os usuários cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class))))
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = buscarUsuarioUseCase.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuários por nome", description = "Retorna a lista de usuários com o nome exato informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o nome informado", content = @Content)
    })
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<UsuarioResponse>> buscarUsuariosPorNome(
            @Parameter(description = "Nome do usuário", required = true) @PathVariable String nome) {
        List<UsuarioResponse> usuarios = buscarUsuarioUseCase.buscarUsuariosPorNome(nome);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Alterar senha", description = "Altera a senha de um usuário informando a senha atual e a nova senha")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "422", description = "Senha atual inválida ou nova senha igual à atual", content = @Content)
    })
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id,
            @Valid @RequestBody UsuarioSenhaRequest request) {
        alterarSenhaUseCase.alterarSenha(id, request);
        return ResponseEntity.noContent().build();
    }
}
