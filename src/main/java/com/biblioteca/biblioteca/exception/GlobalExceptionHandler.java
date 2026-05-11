package com.biblioteca.biblioteca.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── 404 ────────────────────────────────────────────────────────────────
    // Recurso não encontrado (id inexistente no banco)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .status(404)
                        .error("Not Found")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ─── 400 ────────────────────────────────────────────────────────────────
    // Falha nas validações dos DTOs (@NotBlank, @Email, @Past, @Size etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        // Concatena todos os campos inválidos em uma única mensagem
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .status(400)
                        .error("Bad Request")
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ─── 400 ────────────────────────────────────────────────────────────────
    // Tipo incorreto no parâmetro da URL (ex: /api/autores/abc sendo Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String message = String.format(
                "Parâmetro '%s' com valor '%s' é inválido. Tipo esperado: %s.",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .status(400)
                        .error("Bad Request")
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ─── 409 ────────────────────────────────────────────────────────────────
    // Regra de negócio violada — lançada manualmente nos services
    // (ex: ISBN duplicado, CPF já cadastrado, livro já emprestado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .status(409)
                        .error("Conflict")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ─── 409 ────────────────────────────────────────────────────────────────
    // Violação de constraint direto no banco (unique, not null, FK)
    // Captura casos que escaparam da validação do service
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .status(409)
                        .error("Conflict")
                        .message("Operação viola a integridade dos dados. Verifique campos únicos ou referências.")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ─── 500 ────────────────────────────────────────────────────────────────
    // Qualquer erro não mapeado — sempre o último a ser declarado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .status(500)
                        .error("Internal Server Error")
                        .message("Erro inesperado no servidor. Tente novamente mais tarde.")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}