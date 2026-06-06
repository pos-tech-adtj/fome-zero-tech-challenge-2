package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .login(request.login())
                .senha(request.senha())
                .tipoUsuario(request.tipoUsuario())
                .dataUltimaAlteracaoSenha(now)
                .endereco(EnderecoMapper.toEntity(request.endereco()))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .tipoUsuario(usuario.getTipoUsuario())
                .endereco(EnderecoMapper.toResponse(usuario.getEndereco()))
                .build();
    }

    public static void updateEntity(Usuario usuario, UsuarioUpdateRequest request) {
        Optional.ofNullable(request.nome()).ifPresent(usuario::setNome);
        Optional.ofNullable(request.email()).ifPresent(usuario::setEmail);
        Optional.ofNullable(request.login()).ifPresent(usuario::setLogin);
        Optional.ofNullable(request.tipoUsuario()).ifPresent(usuario::setTipoUsuario);
        Optional.ofNullable(request.endereco()).ifPresent(end -> {
            Endereco endereco = usuario.getEndereco() != null ? usuario.getEndereco() : new Endereco();
            usuario.setEndereco(endereco);
            EnderecoMapper.updateEntity(endereco, end);
        });
        usuario.setUpdatedAt(LocalDateTime.now());
    }
}
