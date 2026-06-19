package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.request.RestauranteUpdateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.application.mapper.RestauranteMapper;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.RestauranteNaoEncontradoException;
import com.fiap.fomezero.exception.UsuarioNaoEDonoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AtualizarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public RestauranteResponse atualizarRestaurante(Long id, RestauranteUpdateRequest request) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(RestauranteNaoEncontradoException::new);
        Usuario dono = restaurante.getDono();

        if (!dono.getId().equals(request.donoId())) {
            dono = usuarioRepository.findById(request.donoId())
                    .orElseThrow(UsuarioNaoEncontradoException::new);
            if (dono.getTipoUsuario() != TipoUsuario.DONO_RESTAURANTE) {
                throw new UsuarioNaoEDonoException();
            }
        }

        RestauranteMapper.updateEntity(restaurante, request, dono);
        return RestauranteMapper.toResponse(restauranteRepository.save(restaurante));
    }
}
