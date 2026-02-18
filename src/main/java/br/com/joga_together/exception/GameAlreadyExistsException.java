package br.com.joga_together.exception;

public class GameAlreadyExistsException extends RuntimeException{
    public GameAlreadyExistsException(String message) {
        super(message);
    }
}
