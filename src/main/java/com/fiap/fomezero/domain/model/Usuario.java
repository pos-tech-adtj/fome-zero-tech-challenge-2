package com.fiap.fomezero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;
    private LocalDateTime dataUltimaAlteracaoSenha;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Endereco endereco;

    public Usuario() {
        this.createdAt = LocalDateTime.now();
    }
}