package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeletarRestauranteUseCase {

    private RestauranteRepository restauranteRepository;

    private void deletarRestaurante(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new RestauranteNaoEncontradoException();
        }
        restauranteRepository.deleteById(id);
    }
}
