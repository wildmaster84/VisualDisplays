package me.wildmaster84.visualdisplays.entities;

import org.jetbrains.annotations.NotNull;

public interface VisualDisplay<T> {
    void clear();
    void display(@NotNull T t);
}
