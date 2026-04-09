CREATE TABLE IF NOT EXISTS produtos (
    id         SERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    preco      NUMERIC(10,2) NOT NULL,
    quantidade INT NOT NULL,
    tipo       VARCHAR(10) NOT NULL
    );

CREATE TABLE IF NOT EXISTS pedidos (
    id     SERIAL PRIMARY KEY,
    total  NUMERIC(10,2),
    status VARCHAR(20) DEFAULT 'ABERTO'
    );

CREATE TABLE IF NOT EXISTS itens_pedido (
    id           SERIAL PRIMARY KEY,
    pedido_id    INT REFERENCES pedidos(id),
    produto_id   INT REFERENCES produtos(id),
    quantidade   INT NOT NULL,
    subtotal     NUMERIC(10,2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS fichas (
    id        VARCHAR(36) PRIMARY KEY,
    pedido_id INT REFERENCES pedidos(id),
    usada     BOOLEAN DEFAULT FALSE
    );