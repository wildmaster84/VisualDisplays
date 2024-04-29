package me.wildmaster84.imagedisplays.util;

import net.md_5.bungee.api.ChatColor;

public class ColorConversor {
    private static ColorConversor instance;

    public static ColorConversor getInstance() {
        if (instance == null) {
            instance = new ColorConversor();
        }
        return instance;
    }

    public java.awt.Color toAWTColor(org.bukkit.Color color){
        return new java.awt.Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha());
    }

    public org.bukkit.Color toBukkitColor(java.awt.Color color){
        return org.bukkit.Color.fromRGB(
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

    public ChatColor toChatColor(org.bukkit.Color color){
        return ChatColor.of(toAWTColor(color));
    }

    public ChatColor toChatColor(java.awt.Color color){
        return ChatColor.of(color);
    }
}
