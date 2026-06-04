package com.fiap.fomezero.domain.port;

import java.util.List;

public interface TokenPort {

    String gerarToken(Long idUsuario, List<String> roles, String login);

    String validarToken(String token);
}
