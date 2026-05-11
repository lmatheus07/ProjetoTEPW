package com.biblioteca.biblioteca.dto.usuario;

import java.time.LocalDate;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String telefone,
        LocalDate dataCadastro
) {}