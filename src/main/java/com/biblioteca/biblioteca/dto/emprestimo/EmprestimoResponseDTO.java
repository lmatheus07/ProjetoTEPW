package com.biblioteca.biblioteca.dto.emprestimo;

import java.time.LocalDate;

public record EmprestimoResponseDTO(
        Long id,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucao,
        String status,
        Long usuarioId,
        String nomeUsuario,
        Long livroId,
        String tituloLivro
) {}
