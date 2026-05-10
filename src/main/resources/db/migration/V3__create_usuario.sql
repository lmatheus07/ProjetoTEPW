CREATE TABLE usuario (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome             VARCHAR(150) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    cpf              VARCHAR(14)  NOT NULL UNIQUE,
    telefone         VARCHAR(20),
    data_cadastro    DATE NOT NULL
);
