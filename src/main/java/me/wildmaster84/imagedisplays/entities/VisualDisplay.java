package me.wildmaster84.imagedisplays.entities;

import org.jetbrains.annotations.NotNull;

public interface VisualDisplay<T> {
    void clear();
    void display(@NotNull T t);
}
