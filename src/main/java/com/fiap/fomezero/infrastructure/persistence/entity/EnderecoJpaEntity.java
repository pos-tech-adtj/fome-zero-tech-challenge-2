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
@Table(name = "enderecos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enderecos_seq")
    @SequenceGenerator(name = "enderecos_seq", sequenceName = "enderecos_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 150)
    private String rua;

    @Column(nullable = false)
    private Integer numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(nullable = false, length = 20)
    private String cep;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
