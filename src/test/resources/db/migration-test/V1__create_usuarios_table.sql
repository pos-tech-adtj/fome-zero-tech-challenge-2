CREATE SEQUENCE IF NOT EXISTS usuarios_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE usuarios (
      id BIGINT NOT NULL DEFAULT nextval('usuarios_id_seq'),

      nome VARCHAR(150) NOT NULL,
      email VARCHAR(150) NOT NULL UNIQUE,
      login VARCHAR(100) NOT NULL UNIQUE,
      senha VARCHAR(255) NOT NULL,

      tipo_usuario VARCHAR(30) NOT NULL
          CONSTRAINT chk_tipo_usuario CHECK (tipo_usuario IN ('CLIENTE', 'DONO_RESTAURANTE')),

      data_ultima_alteracao_senha TIMESTAMP NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

      CONSTRAINT pk_usuarios PRIMARY KEY (id)
);