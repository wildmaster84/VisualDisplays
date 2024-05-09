package me.wildmaster84.visualdisplays.entities.terminal;

import me.wildmaster84.visualdisplays.entities.MovingEntityController;
import me.wildmaster84.visualdisplays.util.ColorConversor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

public record DisplayTerminal(@NotNull TextDisplay getRenderer,
                              @NotNull Queue<String> getDisplay,
                              int getLineLength,
                              int getMaxLines,
                              @NotNull Color getBackgroundColor,
                              @NotNull Color getDefaultTextColor)
implements Terminal, MovingEntityController {

    public void remove(){
        if (getRenderer.isValid())
            getRenderer.remove();
    }

    public boolean isValid() {
        return getRenderer.isValid();
    }

    @Override
    public void modifyLast(@NotNull Function<String, String> function){
        Terminal.super.modifyLast(function); // calls the default implementation
        updateRenderer(); // updates the renderer
    }

    public void clear() {
        getDisplay.clear();
        updateRenderer();
    }

    public void display(@NotNull String string) {
        Objects.requireNonNull(string, "'string' cannot be null");
        int length = string.length();
        string = ChatColor.stripColor(string); // removes color codes
        if (length > getLineLength){
            // splits the string into multiple lines and displays them
            String[] results = string.split("(?<=\\G.{" + getLineLength + "})");
            for (String result : results)
                display(result);
            return;
        }
        if (getDisplay.size() >= getMaxLines){
            getDisplay.poll();
            getDisplay.add(string);
        }
        else
            getDisplay.add(string);
        updateRenderer();
    }

    private String space(int remaining){
        return ColorConversor.getInstance().toChatColor(getBackgroundColor) +
                "■".repeat(Math.max(0, remaining));
    }

    private void updateRenderer(){
        if (!getRenderer.isValid())
            return; // renderer was removed
        getRenderer.setText(null); // Clears the getRenderer
        Queue<String> display = getDisplay;
        if (display.isEmpty())
            return; // if getDisplay is empty, getRenderer is already cleared
        StringBuilder builder = new StringBuilder();
        for (String string : display) {
            int remaining = getLineLength - string.length();
            if (remaining > 0)
                string = string + space(remaining);
            builder.append(ColorConversor.getInstance().toChatColor(getDefaultTextColor)).append(string).append("\n");
        }
        String text = builder.toString();
        text = text.substring(0, text.length() - 1); // removes the last \n
        getRenderer.setText(text);
    }

    @NotNull
    public Location getLocation() {
        return getRenderer.getLocation();
    }

    public void teleport(@NotNull Location target) {
        getRenderer.teleport(target);
    }
}
