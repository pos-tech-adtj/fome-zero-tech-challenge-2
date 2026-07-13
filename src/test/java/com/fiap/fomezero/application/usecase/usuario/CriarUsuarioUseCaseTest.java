package com.fiap.fomezero.application.usecase.usuario;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.EmailJaCadastradoException;
import com.fiap.fomezero.exception.LoginJaCadastradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CriarUsuarioUseCase criarUsuarioUseCase;

    private UsuarioCreateRequest request;
    private Usuario usuarioSalvo;

    @BeforeEach
    void setUp() {
        request = new UsuarioCreateRequest("João Silva", "joao@email.com", "joao.silva", "senha123", TipoUsuario.CLIENTE, null);

        usuarioSalvo = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .login("joao.silva")
                .senha("senha-encoded")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .build();
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(usuarioRepository.existsByLogin(request.login())).thenReturn(false);
        when(passwordEncoder.encode(request.senha())).thenReturn("senha-encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        UsuarioResponse response = criarUsuarioUseCase.criarUsuario(request);

        // Assert
        assertNotNull(response);
        assertEquals(usuarioSalvo.getId(), response.getId());
        assertEquals(usuarioSalvo.getNome(), response.getNome());
        verify(passwordEncoder).encode(request.senha());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar EmailJaCadastradoException quando email já existe")
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);

        // Act
        assertThrows(EmailJaCadastradoException.class,
                () -> criarUsuarioUseCase.criarUsuario(request));

        // Assert
        verify(usuarioRepository, never()).existsByLogin(any());
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Deve lançar LoginJaCadastradoException quando login já existe")
    void deveLancarExcecaoQuandoLoginJaCadastrado() {
        // Arrange
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(usuarioRepository.existsByLogin(request.login())).thenReturn(true);

        // Act
        assertThrows(LoginJaCadastradoException.class,
                () -> criarUsuarioUseCase.criarUsuario(request));

        // Assert
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Deve validar email antes do login")
    void deveValidarEmailAntesDoLogin() {
        // Arrange
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);

        // Act
        assertThrows(EmailJaCadastradoException.class,
                () -> criarUsuarioUseCase.criarUsuario(request));

        // Assert
        verify(usuarioRepository, never()).existsByLogin(any());
    }

    @Test
    @DisplayName("Deve salvar usuário com senha encodada e não com senha em texto puro")
    void deveSalvarUsuarioComSenhaEncodada() {
        // Arrange
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(usuarioRepository.existsByLogin(request.login())).thenReturn(false);
        when(passwordEncoder.encode(request.senha())).thenReturn("senha-encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        criarUsuarioUseCase.criarUsuario(request);

        // Assert
        verify(usuarioRepository).save(argThat(u ->
                u.getSenha().equals("senha-encoded") &&
                        !u.getSenha().equals(request.senha())
        ));
    }
}