CREATE TABLE categoria (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    codigo    VARCHAR(20) NOT NULL UNIQUE
);
