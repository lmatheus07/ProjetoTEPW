CREATE TABLE livro (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo           VARCHAR(200) NOT NULL,
    isbn             VARCHAR(20)  NOT NULL UNIQUE,
    ano_publicacao   INT,
    pdf_url          VARCHAR(500),
    autor_id         BIGINT NOT NULL,
    categoria_id     BIGINT NOT NULL,
    FOREIGN KEY (autor_id)     REFERENCES autor(id),
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);
