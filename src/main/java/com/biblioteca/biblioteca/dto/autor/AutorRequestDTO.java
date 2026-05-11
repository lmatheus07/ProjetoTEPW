package com.biblioteca.biblioteca.dto.autor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AutorRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @Past(message = "Data de nascimento deve ser uma data passada")
        LocalDate nascimento,

        String bio,

        @Size(max = 80, message = "Nacionalidade deve ter no máximo 80 caracteres")
        String nacionalidade

) {}