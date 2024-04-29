package me.wildmaster84.imagedisplays;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ImageDisplays extends JavaPlugin {
	
	public void onEnable() {
        Location loc = new Location(Bukkit.getWorld("world"), -1, 109, 39);
        new MoviePlayer(loc).display(new File(this.getDataFolder(), "image.png"));
	}

}
