-- V3__create_usuario.sql
CREATE TABLE usuario (
                         id            BIGSERIAL PRIMARY KEY,
                         nome          VARCHAR(150) NOT NULL,
                         email         VARCHAR(150) NOT NULL UNIQUE,
                         cpf           VARCHAR(14)  NOT NULL UNIQUE,
                         telefone      VARCHAR(20),
                         data_cadastro DATE NOT NULL
);