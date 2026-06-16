package com.fiap.fomezero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurantes_seq")
    @SequenceGenerator(name = "restaurantes_seq", sequenceName = "restaurantes_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "tipo_cozinha", length = 100)
    private String tipoCozinha;

    @Column(name = "horario_funcionamento", length = 100)
    private String horarioFuncionamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dono_id", referencedColumnName = "id", nullable = false)
    private UsuarioJpaEntity dono;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private EnderecoJpaEntity endereco;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
