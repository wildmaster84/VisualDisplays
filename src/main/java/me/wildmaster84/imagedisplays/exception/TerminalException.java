package me.wildmaster84.imagedisplays.exception;

public abstract class TerminalException extends RuntimeException {

    public TerminalException(String message) {
        super(message);
    }
}
