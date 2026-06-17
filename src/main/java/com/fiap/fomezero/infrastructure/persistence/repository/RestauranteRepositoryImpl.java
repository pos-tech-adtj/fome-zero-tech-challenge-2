package com.fiap.fomezero.infrastructure.persistence.repository;

import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.mapper.RestauranteJpaMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestauranteRepositoryImpl implements RestauranteRepository {

    private RestauranteJpaRepository restauranteJpaRepository;

    @Override
    public Restaurante save(Restaurante restaurante) {
        return RestauranteJpaMapper.toDomain(
                restauranteJpaRepository.save(RestauranteJpaMapper.toJpaEntity(restaurante)));
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return restauranteJpaRepository.findById(id)
                .map(RestauranteJpaMapper::toDomain);
    }

    @Override
    public List<Restaurante> findAll() {
        return restauranteJpaRepository.findAll().stream()
                .map(RestauranteJpaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        restauranteJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return restauranteJpaRepository.existsById(id);
    }
}
