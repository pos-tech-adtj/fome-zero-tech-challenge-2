package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity;

public class UsuarioJpaMapper {

    public static Usuario toDomain(UsuarioJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .login(entity.getLogin())
                .senha(entity.getSenha())
                .tipoUsuario(entity.getTipoUsuario())
                .dataUltimaAlteracaoSenha(entity.getDataUltimaAlteracaoSenha())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                // TODO - Mapear o endereço do usuário para a entidade correspondente
                .build();
    }

    public static UsuarioJpaEntity toJpaEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioJpaEntity.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .senha(usuario.getSenha())
                .tipoUsuario(usuario.getTipoUsuario())
                .dataUltimaAlteracaoSenha(usuario.getDataUltimaAlteracaoSenha())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                // TODO - Mapear o endereço do usuário para a entidade correspondente
                .build();
    }
}
