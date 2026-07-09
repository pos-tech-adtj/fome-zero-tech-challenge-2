package com.fiap.fomezero.infrastructure.web.controller.usuario;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioSenhaRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.infrastructure.web.controller.helpers.UsuarioControllerTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class UsuarioControllerIT {

    private static final String BASE_URL = "/v1/usuarios";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Usuario salvarUsuario(String nome, String email, String login) {
        return usuarioRepository.save(Usuario.builder()
                .nome(nome)
                .email(email)
                .login(login)
                .senha(passwordEncoder.encode("Senha@123"))
                .tipoUsuario(TipoUsuario.CLIENTE)
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .build());
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 201 ao criar usuário válido")
    void criarUsuario_dadosValidos_retorna201() throws Exception {
        UsuarioCreateRequest request = UsuarioControllerTestHelper.buildCreateRequest();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.login").value("joao.silva"));
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 409 quando email já está cadastrado")
    void criarUsuario_emailJaCadastrado_retorna409() throws Exception {
        salvarUsuario("Usuário Existente", "joao@email.com", "outro.login");

        UsuarioCreateRequest request = UsuarioControllerTestHelper.buildCreateRequest();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Email já cadastrado"));
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 409 quando login já está cadastrado")
    void criarUsuario_loginJaCadastrado_retorna409() throws Exception {
        salvarUsuario("Usuário Existente", "outro@email.com", "joao.silva");

        UsuarioCreateRequest request = UsuarioControllerTestHelper.buildCreateRequest();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Login já cadastrado"));
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 400 quando campos obrigatórios estão ausentes")
    void criarUsuario_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("DELETE /v1/usuarios/{id} deve retornar 204 ao deletar usuário existente")
    void deletarUsuario_existente_retorna204() throws Exception {
        Usuario usuario = salvarUsuario("A Ser Deletado", "deletar@email.com", "deletar");

        mockMvc.perform(delete(BASE_URL + "/{id}", usuario.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/{id}", usuario.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /v1/usuarios/{id} deve retornar 404 quando usuário não existe")
    void deletarUsuario_inexistente_retorna404() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 200 ao atualizar usuário existente")
    void atualizarUsuario_dadosValidos_retorna200() throws Exception {
        Usuario usuario = salvarUsuario("Nome Original", "original@email.com", "original");
        UsuarioUpdateRequest update = UsuarioControllerTestHelper.buildUpdateRequest();

        mockMvc.perform(put(BASE_URL + "/{id}", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João da Silva Junior"));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 404 quando usuário não existe")
    void atualizarUsuario_inexistente_retorna404() throws Exception {
        UsuarioUpdateRequest update = UsuarioControllerTestHelper.buildUpdateRequest();

        mockMvc.perform(put(BASE_URL + "/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 409 quando novo email já está cadastrado por outro usuário")
    void atualizarUsuario_emailJaCadastradoPorOutroUsuario_retorna409() throws Exception {
        salvarUsuario("Outro Usuário", "emailexistente@email.com", "outro.login");
        Usuario usuario = salvarUsuario("Nome Original", "original@email.com", "original");

        UsuarioUpdateRequest update = new UsuarioUpdateRequest(
                null,
                "emailexistente@email.com",
                null,
                null,
                null
        );

        mockMvc.perform(put(BASE_URL + "/{id}", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Email já cadastrado"));
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar 200 quando usuário existe")
    void buscarUsuarioPorId_existente_retorna200() throws Exception {
        Usuario usuario = salvarUsuario("Maria Souza", "maria@email.com", "maria.souza");

        mockMvc.perform(get(BASE_URL + "/{id}", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuario.getId()))
                .andExpect(jsonPath("$.nome").value("Maria Souza"));
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar 404 quando usuário não existe")
    void buscarUsuarioPorId_inexistente_retorna404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("GET /v1/usuarios deve retornar 200 com lista de usuários")
    void listarUsuarios_retorna200ComLista() throws Exception {
        salvarUsuario("Usuário Um", "um@email.com", "um");
        salvarUsuario("Usuário Dois", "dois@email.com", "dois");

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].nome", hasItems("Usuário Um", "Usuário Dois")));
    }

    @Test
    @DisplayName("GET /v1/usuarios deve retornar 200 com lista vazia quando não há usuários")
    void listarUsuarios_semUsuarios_retornaListaVazia() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /v1/usuarios/nome/{nome} deve retornar 200 com usuários encontrados")
    void buscarUsuariosPorNome_existente_retorna200() throws Exception {
        salvarUsuario("Carlos Pereira", "carlos@email.com", "carlos");

        mockMvc.perform(get(BASE_URL + "/nome/{nome}", "Carlos Pereira"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nome", hasItem("Carlos Pereira")));
    }

    @Test
    @DisplayName("GET /v1/usuarios/nome/{nome} deve retornar 200 com lista vazia quando nenhum usuário é encontrado")
    void buscarUsuariosPorNome_inexistente_retornaListaVazia() throws Exception {
        mockMvc.perform(get(BASE_URL + "/nome/{nome}", "Nome Inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 204 ao alterar senha com sucesso")
    void alterarSenha_dadosValidos_retorna204() throws Exception {
        Usuario usuario = salvarUsuario("Senha Teste", "senha@email.com", "senha.teste");
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequest();

        mockMvc.perform(patch(BASE_URL + "/{id}/senha", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 404 quando usuário não existe")
    void alterarSenha_usuarioInexistente_retorna404() throws Exception {
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequest();

        mockMvc.perform(patch(BASE_URL + "/{id}/senha", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 422 quando senha atual é inválida")
    void alterarSenha_senhaAtualInvalida_retorna422() throws Exception {
        Usuario usuario = salvarUsuario("Senha Teste", "senha2@email.com", "senha.teste2");
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequestComSenhaAtualInvalida();

        mockMvc.perform(patch(BASE_URL + "/{id}/senha", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Senha atual inválida"));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 422 quando nova senha é igual à atual")
    void alterarSenha_novaSenhaIgualAtual_retorna422() throws Exception {
        Usuario usuario = salvarUsuario("Senha Teste", "senha3@email.com", "senha.teste3");
        UsuarioSenhaRequest request = UsuarioControllerTestHelper.buildSenhaRequestComNovaSenhaIgualAtual();

        mockMvc.perform(patch(BASE_URL + "/{id}/senha", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Nova senha igual à atual"));
    }

    @Test
    @DisplayName("PATCH /v1/usuarios/{id}/senha deve retornar 400 quando campos obrigatórios estão ausentes")
    void alterarSenha_camposObrigatoriosAusentes_retorna400() throws Exception {
        Usuario usuario = salvarUsuario("Senha Teste", "senha4@email.com", "senha.teste4");

        mockMvc.perform(patch(BASE_URL + "/{id}/senha", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").exists());
    }
}