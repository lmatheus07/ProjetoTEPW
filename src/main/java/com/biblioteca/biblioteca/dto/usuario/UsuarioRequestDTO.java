package com.biblioteca.biblioteca.dto.usuario;

import jakarta.validation.constraints.*;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(
                regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF deve estar no formato 000.000.000-00"
        )
        String cpf,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone

) {}