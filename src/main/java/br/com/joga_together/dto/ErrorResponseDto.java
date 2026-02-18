package br.com.joga_together.dto;

public record ErrorResponseDto(
        String timestamp,
        int status,
        String error,
        String message
) {
}
