package com.fiap.fomezero.infrastructure.persistence.entity;

import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "usuarios")
public class UsuarioJpaEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_seq")
    @SequenceGenerator(name = "usuarios_seq", sequenceName = "usuarios_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    public UsuarioJpaEntity() {}

    // Construtor: Recebe um Usuario puro e transforma em Entidade de Banco
    public UsuarioJpaEntity(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.senha = usuario.getSenha();
        this.tipoUsuario = usuario.getTipoUsuario();
    }

    // Método: Transforma a Entidade de Banco de volta em um Usuario puro
    public Usuario toDomain() {
        return new Usuario(this.id, this.login, this.senha, this.tipoUsuario);
    }

    // --- Métodos obrigatórios do Spring Security (UserDetails) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(TipoUsuario.values())
                .filter(role -> role.ordinal() <= tipoUsuario.ordinal())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.login; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}