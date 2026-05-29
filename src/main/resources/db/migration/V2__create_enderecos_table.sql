-- Sequence
CREATE SEQUENCE enderecos_id_seq
    START WITH 1
    INCREMENT BY 1;

-- Tabela enderecos
CREATE TABLE enderecos (
    id BIGINT NOT NULL DEFAULT nextval('enderecos_id_seq'),

    rua VARCHAR(150) NOT NULL,
    numero BIGINT NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    cep VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_enderecos PRIMARY KEY (id)
);