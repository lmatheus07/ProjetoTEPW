
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// DTO separado só para atualizar o status do empréstimo
// Evita que o cliente mande todos os campos só para mudar o status
public record EmprestimoAtualizarStatusDTO(

        @NotBlank(message = "Status é obrigatório")
        @Pattern(
                regexp = "ATIVO|DEVOLVIDO|ATRASADO",
                message = "Status deve ser: ATIVO, DEVOLVIDO ou ATRASADO"
        )
        String status

) {}