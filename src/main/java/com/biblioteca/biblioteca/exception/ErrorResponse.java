package com.biblioteca.biblioteca.exception;

@Builder
public record ErrorResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {}
