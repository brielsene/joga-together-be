package br.com.joga_together.exception;

public class CodeInvalidOrExpireException extends RuntimeException {
    public CodeInvalidOrExpireException(String message) {
        super(message);
    }
}
