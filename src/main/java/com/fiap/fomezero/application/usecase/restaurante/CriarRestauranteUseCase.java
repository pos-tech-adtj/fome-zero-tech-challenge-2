package com.fiap.fomezero.application.usecase.restaurante;

import com.fiap.fomezero.application.dto.request.RestauranteCreateRequest;
import com.fiap.fomezero.application.dto.response.RestauranteResponse;
import com.fiap.fomezero.application.mapper.RestauranteMapper;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.domain.repository.RestauranteRepository;
import com.fiap.fomezero.domain.repository.UsuarioRepository;
import com.fiap.fomezero.exception.UsuarioNaoEDonoException;
import com.fiap.fomezero.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public RestauranteResponse criarRestaurante(RestauranteCreateRequest request) {
        Usuario usuario = usuarioRepository.findById(request.donoId())
                .orElseThrow(UsuarioNaoEncontradoException::new);

        if (usuario.getTipoUsuario() != TipoUsuario.DONO_RESTAURANTE) {
            throw new UsuarioNaoEDonoException();
        }
        Restaurante restaurante = RestauranteMapper.toEntity(request, usuario);
        restauranteRepository.save(restaurante);
        return RestauranteMapper.toResponse(restaurante);
    }
}
