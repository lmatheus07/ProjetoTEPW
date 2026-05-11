package com.biblioteca.biblioteca.dto.autor;

import java.time.LocalDate;

public record AutorResponseDTO(
        Long id,
        String nome,
        LocalDate nascimento,
        String bio,
        String nacionalidade
) {}
