package me.wildmaster84.imagedisplays.entities.terminal;

import me.wildmaster84.imagedisplays.entities.VisualDisplay;
import me.wildmaster84.imagedisplays.exception.LineLengthException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

public interface Terminal extends VisualDisplay<String> {
    /**
     * Gets the lines to display
     * @return the lines to display
     */
    @NotNull
    Queue<String> getDisplay();

    /**
     * Appends a string to the last line
     * @param string the string to append
     */
    default void append(@NotNull String string){
        Objects.requireNonNull(string, "'string' cannot be null");
        modifyLast(last -> last + string);
    }

    /**
     * Modifies the last line
     * @param function the function to modify the last line
     */
    default void modifyLast(@NotNull Function<String, String> function){
        String last = getDisplay().poll();
        last = function.apply(last);
        int length = last.length();
        if (length > getLineLength())
            throw LineLengthException.of(this);
        getDisplay().add(last);
    }

    /**
     * Gets the maximum length per line
     * @return the maximum length per line
     */
    int getLineLength();

    /**
     * Gets the maximum lines that can be displayed
     * @return the maximum lines that can be displayed
     */
    int getMaxLines();
}
