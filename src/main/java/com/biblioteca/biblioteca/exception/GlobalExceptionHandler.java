package com.biblioteca.biblioteca.exception;

@RestControllerAdvice  // intercepta exceções de todos os controllers
public class GlobalExceptionHandler {

    // Recurso não encontrado → 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    // Violação de constraint do banco (ex: ISBN duplicado) → 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.builder()
                .status(409).error("Conflict")
                .message("Dado duplicado ou violação de integridade.")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    // Erros de validação dos DTOs (@NotBlank, @Email etc.) → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .status(400).error("Bad Request")
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    // Qualquer outro erro não mapeado → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.builder()
                .status(500).error("Internal Server Error")
                .message("Erro inesperado. Contate o suporte.")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
}
