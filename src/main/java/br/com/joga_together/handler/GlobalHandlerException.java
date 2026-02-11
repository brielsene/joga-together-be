package br.com.joga_together.handler;

import br.com.joga_together.dto.ErrorResponseDto;
import br.com.joga_together.exception.GameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(GameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto>handlerExceptionGameAlreadyExistsException(GameAlreadyExistsException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
