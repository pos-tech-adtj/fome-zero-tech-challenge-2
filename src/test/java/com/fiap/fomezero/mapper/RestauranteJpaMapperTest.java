package com.fiap.fomezero.mapper;

import com.fiap.fomezero.domain.model.Endereco;
import com.fiap.fomezero.domain.model.Restaurante;
import com.fiap.fomezero.domain.model.TipoUsuario;
import com.fiap.fomezero.domain.model.Usuario;
import com.fiap.fomezero.infrastructure.persistence.entity.EnderecoJpaEntity;
import com.fiap.fomezero.infrastructure.persistence.entity.RestauranteJpaEntity;
import com.fiap.fomezero.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteJpaMapperTest {

    @Test
    @DisplayName("toDomain deve mapear restaurante com dono e endereço")
    void toDomainDeveMapearRestauranteCompleto() {
        RestauranteJpaEntity entity = RestauranteJpaEntity.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("09:00 - 22:00")
                .dono(UsuarioJpaEntity.builder().id(2L).nome("Dono").tipoUsuario(TipoUsuario.DONO_RESTAURANTE).build())
                .endereco(EnderecoJpaEntity.builder().id(3L).rua("Rua A").build())
                .build();

        Restaurante restaurante = RestauranteJpaMapper.toDomain(entity);

        assertEquals(1L, restaurante.getId());
        assertEquals("Cantina Fiap", restaurante.getNome());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals("09:00 - 22:00", restaurante.getHorarioFuncionamento());
        assertNotNull(restaurante.getDono());
        assertEquals(2L, restaurante.getDono().getId());
        assertNotNull(restaurante.getEndereco());
        assertEquals("Rua A", restaurante.getEndereco().getRua());
    }

    @Test
    @DisplayName("toDomain deve mapear restaurante sem dono e sem endereço")
    void toDomainDeveMapearRestauranteSemDonoESemEndereco() {
        RestauranteJpaEntity entity = RestauranteJpaEntity.builder().id(1L).nome("Cantina Fiap").build();

        Restaurante restaurante = RestauranteJpaMapper.toDomain(entity);

        assertNull(restaurante.getDono());
        assertNull(restaurante.getEndereco());
    }

    @Test
    @DisplayName("toDomain deve retornar null quando a entidade for null")
    void toDomainDeveRetornarNullQuandoEntidadeForNull() {
        assertNull(RestauranteJpaMapper.toDomain(null));
    }

    @Test
    @DisplayName("toJpaEntity deve mapear restaurante com dono e endereço")
    void toJpaEntityDeveMapearRestauranteCompleto() {
        Restaurante restaurante = Restaurante.builder()
                .id(1L)
                .nome("Cantina Fiap")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("09:00 - 22:00")
                .dono(Usuario.builder().id(2L).nome("Dono").tipoUsuario(TipoUsuario.DONO_RESTAURANTE).build())
                .endereco(Endereco.builder().id(3L).rua("Rua A").build())
                .build();

        RestauranteJpaEntity entity = RestauranteJpaMapper.toJpaEntity(restaurante);

        assertEquals(1L, entity.getId());
        assertEquals("Cantina Fiap", entity.getNome());
        assertEquals("Italiana", entity.getTipoCozinha());
        assertEquals("09:00 - 22:00", entity.getHorarioFuncionamento());
        assertNotNull(entity.getDono());
        assertEquals(2L, entity.getDono().getId());
        assertNotNull(entity.getEndereco());
        assertEquals("Rua A", entity.getEndereco().getRua());
    }

    @Test
    @DisplayName("toJpaEntity deve mapear restaurante sem dono e sem endereço")
    void toJpaEntityDeveMapearRestauranteSemDonoESemEndereco() {
        Restaurante restaurante = Restaurante.builder().id(1L).nome("Cantina Fiap").build();

        RestauranteJpaEntity entity = RestauranteJpaMapper.toJpaEntity(restaurante);

        assertNull(entity.getDono());
        assertNull(entity.getEndereco());
    }

    @Test
    @DisplayName("toJpaEntity deve retornar null quando o domínio for null")
    void toJpaEntityDeveRetornarNullQuandoDominioForNull() {
        assertNull(RestauranteJpaMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Deve permitir instanciar o mapper (cobertura do construtor implícito)")
    void deveInstanciarMapper() {
        assertNotNull(new RestauranteJpaMapper());
    }
}