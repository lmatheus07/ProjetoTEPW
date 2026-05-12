package com.biblioteca.biblioteca.dto.livro;

import jakarta.validation.constraints.*;

public record LivroRequestDTO(

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "ISBN é obrigatório")
        @Size(max = 20, message = "ISBN deve ter no máximo 20 caracteres")
        String isbn,

        @Min(value = 1000, message = "Ano de publicação inválido")
        @Max(value = 2100, message = "Ano de publicação inválido")
        Integer anoPublicacao,

        @Size(max = 500, message = "URL do PDF deve ter no máximo 500 caracteres")
        String pdfUrl,

        @NotNull(message = "ID do autor é obrigatório")
        @Positive(message = "ID do autor deve ser positivo")
        Long autorId,

        @NotNull(message = "ID da categoria é obrigatória")
        @Positive(message = "ID da categoria deve ser positivo")
        Long categoriaId

) {}