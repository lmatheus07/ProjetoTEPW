CREATE TABLE autor (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    nascimento DATE,
    bio        TEXT,
    nacionalidade VARCHAR(80)
);
