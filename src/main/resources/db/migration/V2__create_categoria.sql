-- V2__create_categoria.sql
CREATE TABLE categoria (
                           id        BIGSERIAL PRIMARY KEY,
                           nome      VARCHAR(100) NOT NULL,
                           descricao VARCHAR(255),
                           codigo    VARCHAR(20) NOT NULL UNIQUE
);