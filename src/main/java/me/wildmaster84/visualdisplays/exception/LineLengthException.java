package me.wildmaster84.visualdisplays.exception;

import me.wildmaster84.visualdisplays.entities.terminal.Terminal;
import org.jetbrains.annotations.NotNull;

public class LineLengthException extends TerminalException{
    public static LineLengthException of(@NotNull Terminal terminal) {
        return new LineLengthException("String exceeds line length of " + terminal.getLineLength());
    }

    private LineLengthException(String message) {
        super(message);
    }
}
