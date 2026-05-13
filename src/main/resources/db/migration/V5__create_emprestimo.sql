-- V5__create_emprestimo.sql
CREATE TABLE emprestimo (
                            id               BIGSERIAL PRIMARY KEY,
                            data_emprestimo  DATE NOT NULL,
                            data_devolucao   DATE,
                            status           VARCHAR(30) NOT NULL,
                            usuario_id       BIGINT NOT NULL,
                            livro_id         BIGINT NOT NULL,
                            FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                            FOREIGN KEY (livro_id)   REFERENCES livro(id)
);