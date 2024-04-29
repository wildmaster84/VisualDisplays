package me.wildmaster84.imagedisplays.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Terminal extends VisualDisplay<String> {
    @NotNull
    List<String> getDisplay();

    void append(@NotNull String string);
}
