package br.com.joga_together.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreateRequestDto(
        @Min(3)
        @Max(15)
        String username,
        @Min(8)
        @Max(14)
        String password,
        @CPF
        String cpf,
        @Email
        String email,
        LocalDate bornDate
) {
}
