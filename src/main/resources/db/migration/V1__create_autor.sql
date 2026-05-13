-- V1__create_autor.sql
CREATE TABLE autor (
                       id            BIGSERIAL PRIMARY KEY,
                       nome          VARCHAR(150) NOT NULL,
                       nascimento    DATE,
                       bio           TEXT,
                       nacionalidade VARCHAR(80)
);