package com.fiap.fomezero.infrastructure.web.controller.auth;

import com.fiap.fomezero.application.dto.request.LoginRequest;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.infrastructure.web.controller.helpers.AuthControllerTestHelper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class AuthControllerIT {

    private static final String BASE_URL = "/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.save(Usuario.builder()
                .nome("João da Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .senha(passwordEncoder.encode("Senha@123"))
                .tipoUsuario(TipoUsuario.CLIENTE)
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .build());
    }

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 200 com credenciais válidas")
    void login_credenciaisValidas_retorna200() throws Exception {
        LoginRequest request = AuthControllerTestHelper.buildLoginRequest();

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.autenticado").value(true))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 401 quando a senha está incorreta")
    void login_senhaIncorreta_retorna401() throws Exception {
        LoginRequest request = new LoginRequest("joao.silva", "senhaErrada");

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Credenciais inválidas"));
    }

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 401 quando o login não existe")
    void login_loginInexistente_retorna401() throws Exception {
        LoginRequest request = new LoginRequest("usuario.inexistente", "Senha@123");

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Credenciais inválidas"));
    }

    @Test
    @DisplayName("POST /v1/auth/login deve retornar 400 quando campos obrigatórios estão ausentes")
    void login_camposObrigatoriosAusentes_retorna400() throws Exception {
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").exists());
    }
}