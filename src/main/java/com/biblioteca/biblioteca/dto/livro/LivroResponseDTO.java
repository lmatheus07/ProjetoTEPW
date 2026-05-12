package com.biblioteca.biblioteca.dto.livro;

public record LivroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        String pdfUrl,
        Long autorId,
        String nomeAutor,
        Long categoriaId,
        String nomeCategoria
) {}