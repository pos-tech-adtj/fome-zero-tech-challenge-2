package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.application.mapper.RestauranteMapper;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuscarRestaurantePorIdUseCase {

    private final RestauranteRepository restauranteRepository;

    public RestauranteResponse buscarPorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(RestauranteNaoEncontradoException::new);
        return RestauranteMapper.toResponse(restaurante);
    }

}
