package com.biblioteca.biblioteca.dto.categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao,
        String codigo
) {}
