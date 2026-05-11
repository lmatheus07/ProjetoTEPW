package com.biblioteca.biblioteca.dto.emprestimo;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EmprestimoRequestDTO(

        @NotNull(message = "ID do usuário é obrigatório")
        @Positive(message = "ID do usuário deve ser positivo")
        Long usuarioId,

        @NotNull(message = "ID do livro é obrigatório")
        @Positive(message = "ID do livro deve ser positivo")
        Long livroId,

        // Data de devolução prevista — opcional no momento do cadastro
        @Future(message = "Data de devolução deve ser uma data futura")
        LocalDate dataDevolucao

) {}