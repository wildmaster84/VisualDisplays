package me.wildmaster84.visualdisplays.entities;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface MovingEntityController extends EntityController {
    @NotNull
    Location getLocation();

    void teleport(@NotNull Location target);
}
