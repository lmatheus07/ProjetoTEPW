CREATE TABLE emprestimo (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_emprestimo DATE NOT NULL,
    data_devolucao  DATE,
    status         VARCHAR(30) NOT NULL,
    usuario_id     BIGINT NOT NULL,
    livro_id       BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (livro_id)   REFERENCES livro(id)
);
