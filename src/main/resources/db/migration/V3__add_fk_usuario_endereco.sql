ALTER TABLE usuarios
    ADD COLUMN endereco_id BIGINT;

ALTER TABLE usuarios
    ADD CONSTRAINT fk_usuario_endereco
        FOREIGN KEY (endereco_id)
            REFERENCES enderecos(id)
            ON DELETE SET NULL;

-- Relacionamento 1:1 definido na entidade Usuario.java
ALTER TABLE usuarios
    ADD CONSTRAINT uk_usuario_endereco
        UNIQUE (endereco_id);
