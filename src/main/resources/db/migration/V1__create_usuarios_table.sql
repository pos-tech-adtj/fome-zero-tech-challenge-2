-- Sequence
CREATE SEQUENCE usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Enum tipo de usuario
CREATE TYPE tipo_usuario_enum AS ENUM (
    'CLIENTE',
    'DONO_RESTAURANTE'
);

-- Tabela usuarios
CREATE TABLE usuarios (
    id BIGINT NOT NULL DEFAULT nextval('usuarios_id_seq'),

    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,

    tipo_usuario tipo_usuario_enum NOT NULL,

    data_ultima_alteracao_senha TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuarios PRIMARY KEY (id)
);