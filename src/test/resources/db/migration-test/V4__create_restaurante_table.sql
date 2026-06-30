CREATE SEQUENCE IF NOT EXISTS restaurantes_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE restaurantes (
      id BIGINT NOT NULL DEFAULT nextval('restaurantes_id_seq'),

      nome VARCHAR(150) NOT NULL,
      tipo_cozinha VARCHAR(100),
      horario_funcionamento VARCHAR(100),

      dono_id BIGINT NOT NULL,
      endereco_id BIGINT,

      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

      CONSTRAINT pk_restaurantes PRIMARY KEY (id),

      CONSTRAINT fk_restaurante_dono
          FOREIGN KEY (dono_id)
              REFERENCES usuarios(id)
              ON DELETE RESTRICT,

      CONSTRAINT fk_restaurante_endereco
          FOREIGN KEY (endereco_id)
              REFERENCES enderecos(id)
              ON DELETE SET NULL
);