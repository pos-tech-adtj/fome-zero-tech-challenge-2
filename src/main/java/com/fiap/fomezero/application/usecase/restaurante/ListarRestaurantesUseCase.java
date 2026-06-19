package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.application.mapper.RestauranteMapper;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarRestaurantesUseCase {

    private final RestauranteRepository restauranteRepository;

    public List<RestauranteResponse> listarRestaurantes() {
        return restauranteRepository.findAll().stream()
                .map(RestauranteMapper::toResponse)
                .toList();
    }
}
