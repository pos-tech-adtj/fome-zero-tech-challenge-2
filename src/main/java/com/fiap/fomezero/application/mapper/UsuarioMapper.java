package com.fiap.fomezero.application.mapper;

import com.fiap.fomezero.application.dto.request.UsuarioCreateRequest;
import com.fiap.fomezero.application.dto.request.UsuarioUpdateRequest;
import com.fiap.fomezero.application.dto.response.EnderecoResponse;
import com.fiap.fomezero.application.dto.response.UsuarioResponse;
import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioCreateRequest request) {

        Endereco endereco = null;

        if (request.endereco() != null) {
            endereco = Endereco.builder()
                    .rua(request.endereco().rua())
                    .numero(request.endereco().numero())
                    .complemento(request.endereco().complemento())
                    .bairro(request.endereco().bairro())
                    .cidade(request.endereco().cidade())
                    .estado(request.endereco().estado())
                    .cep(request.endereco().cep())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        return Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .login(request.login())
                .senha(request.senha())
                .tipoUsuario(request.tipoUsuario())
                .dataUltimaAlteracaoSenha(LocalDateTime.now())
                .endereco(endereco)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario usuario) {

        EnderecoResponse enderecoResponse = null;

        if (usuario.getEndereco() != null) {
            enderecoResponse = EnderecoResponse.builder()
                    .rua(usuario.getEndereco().getRua())
                    .numero(Integer.parseInt(String.valueOf(usuario.getEndereco().getNumero())))
                    .complemento(usuario.getEndereco().getComplemento())
                    .bairro(usuario.getEndereco().getBairro())
                    .cidade(usuario.getEndereco().getCidade())
                    .estado(usuario.getEndereco().getEstado())
                    .cep(usuario.getEndereco().getCep())
                    .build();
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .tipoUsuario(usuario.getTipoUsuario())
                .endereco(enderecoResponse)
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
            Optional.ofNullable(end.rua()).ifPresent(endereco::setRua);
            Optional.ofNullable(end.numero()).ifPresent(endereco::setNumero);
            Optional.ofNullable(end.complemento()).ifPresent(endereco::setComplemento);
            Optional.ofNullable(end.bairro()).ifPresent(endereco::setBairro);
            Optional.ofNullable(end.cidade()).ifPresent(endereco::setCidade);
            Optional.ofNullable(end.estado()).ifPresent(endereco::setEstado);
            Optional.ofNullable(end.cep()).ifPresent(endereco::setCep);
            endereco.setUpdatedAt(LocalDateTime.now());
        });
        usuario.setUpdatedAt(LocalDateTime.now());
    }
}
