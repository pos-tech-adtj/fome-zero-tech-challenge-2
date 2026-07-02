CREATE SEQUENCE IF NOT EXISTS itens_cardapio_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE itens_cardapio (
      id BIGINT NOT NULL DEFAULT nextval('itens_cardapio_id_seq'),

      nome VARCHAR(150) NOT NULL,
      descricao TEXT,
      preco DECIMAL(10,2) NOT NULL,
      apenas_no_restaurante BOOLEAN NOT NULL DEFAULT false,
      foto_path VARCHAR(500),

      restaurante_id BIGINT NOT NULL,

      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

      CONSTRAINT pk_itens_cardapio PRIMARY KEY (id),

      CONSTRAINT fk_item_cardapio_restaurante
          FOREIGN KEY (restaurante_id)
              REFERENCES restaurantes(id)
              ON DELETE CASCADE
);