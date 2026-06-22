package com.fiap.fomezero.application.usecase.auth;

import com.fiap.fomezero.application.dto.request.LoginRequest;
import com.fiap.fomezero.application.dto.response.LoginResponse;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.port.TokenPort;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.CredenciaisInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenPort tokenPort;

    @InjectMocks
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    private LoginRequest loginRequest;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("usuario@email.com", "senha123");

        usuario = Usuario.builder()
                .id(1L)
                .login("usuario@email.com")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso e retornar token")
    void deveAutenticarUsuarioComSucesso() {
        // Arrange
        String tokenEsperado = "jwt-token-gerado";

        when(usuarioRepository.findByLogin(loginRequest.login()))
                .thenReturn(Optional.of(usuario));
        when(tokenPort.gerarToken(eq(usuario.getId()), anyList(), eq(usuario.getLogin())))
                .thenReturn(tokenEsperado);

        // Act
        LoginResponse response = autenticarUsuarioUseCase.autenticarUsuario(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(tokenEsperado, response.getToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.senha())
        );
        verify(usuarioRepository).findByLogin(loginRequest.login());
        verify(tokenPort).gerarToken(usuario.getId(), List.of("ROLE_CLIENTE"), usuario.getLogin());
    }

    @Test
    @DisplayName("Deve lançar exceção quando credenciais são inválidas no AuthenticationManager")
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        // Arrange
        doThrow(new BadCredentialsException("Credenciais inválidas"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        assertThrows(BadCredentialsException.class,
                () -> autenticarUsuarioUseCase.autenticarUsuario(loginRequest));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(usuarioRepository);
        verifyNoInteractions(tokenPort);
    }

    @Test
    @DisplayName("Deve lançar CredenciaisInvalidasException quando usuário não encontrado no repositório")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.findByLogin(loginRequest.login()))
                .thenReturn(Optional.empty());

        // Act
        assertThrows(CredenciaisInvalidasException.class,
                () -> autenticarUsuarioUseCase.autenticarUsuario(loginRequest));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository).findByLogin(loginRequest.login());
        verifyNoInteractions(tokenPort);
    }

    @Test
    @DisplayName("Deve gerar role correta para cada TipoUsuario")
    void deveGerarRoleCorretaParaCadaTipoUsuario() {
        // Arrange
        for (TipoUsuario tipo : TipoUsuario.values()) {
            Usuario usuarioComTipo = Usuario.builder()
                    .id(2L)
                    .login("usuario@email.com")
                    .tipoUsuario(tipo)
                    .build();

            List<String> rolesEsperadas = List.of("ROLE_" + tipo.name());

            when(usuarioRepository.findByLogin(loginRequest.login()))
                    .thenReturn(Optional.of(usuarioComTipo));
            when(tokenPort.gerarToken(usuarioComTipo.getId(), rolesEsperadas, usuarioComTipo.getLogin()))
                    .thenReturn("token-" + tipo.name());

            // Act
            LoginResponse response = autenticarUsuarioUseCase.autenticarUsuario(loginRequest);

            // Assert
            assertNotNull(response);
            verify(tokenPort).gerarToken(usuarioComTipo.getId(), rolesEsperadas, usuarioComTipo.getLogin());

            reset(usuarioRepository, tokenPort, authenticationManager);
        }
    }

    @Test
    @DisplayName("Deve chamar authenticate com UsernamePasswordAuthenticationToken correto")
    void deveChamarAuthenticateComTokenCorreto() {
        // Arrange
        when(usuarioRepository.findByLogin(loginRequest.login()))
                .thenReturn(Optional.of(usuario));
        when(tokenPort.gerarToken(any(), any(), any()))
                .thenReturn("token");

        // Act
        autenticarUsuarioUseCase.autenticarUsuario(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(argThat(token ->
                token instanceof UsernamePasswordAuthenticationToken
                        && token.getPrincipal().equals(loginRequest.login())
                        && token.getCredentials().equals(loginRequest.senha())
        ));
    }
}