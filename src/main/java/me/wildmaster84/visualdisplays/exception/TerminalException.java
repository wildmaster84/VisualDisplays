package me.wildmaster84.visualdisplays.exception;

public abstract class TerminalException extends RuntimeException {

    public TerminalException(String message) {
        super(message);
    }
}
