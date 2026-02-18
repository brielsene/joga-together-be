package br.com.joga_together.dto;

public record ConfirmCodeDto(
        String email,
        String code
) {
}
