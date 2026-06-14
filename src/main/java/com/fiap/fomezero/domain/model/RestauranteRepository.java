package com.fiap.fomezero.domain.repository;

import com.fiap.fomezero.domain.model.Restaurante;
import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {

    // Salva um novo restaurante ou atualiza um existente e retorna o objeto salvo
    Restaurante save(Restaurante restaurante);

    // Busca um restaurante pelo ID. Retorna um Optional para evitar o erro NullPointerException caso não exista
    Optional<Restaurante> findById(Long id);

    // Retorna uma lista contendo todos os restaurantes cadastrados
    List<Restaurante> findAll();

    // Remove o restaurante do sistema com base no ID fornecido
    void deleteById(Long id);

    // Verifica rapidamente se um restaurante existe, retornando verdadeiro ou falso
    boolean existsById(Long id);
}