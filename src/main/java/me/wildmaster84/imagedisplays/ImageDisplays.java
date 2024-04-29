package me.wildmaster84.imagedisplays;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ImageDisplays extends JavaPlugin {
	MoviePlayer moviePlayer;
	TextPlayer textPlayer;
	
	@Override
	public void onEnable() {
        Location loc = new Location(Bukkit.getWorld("world"), -1, 109, 39);
        //moviePlayer = new MoviePlayer(loc);
        //moviePlayer.display(new File(this.getDataFolder(), "image.png"));
        //textPlayer = new TextPlayer(loc);
        //textPlayer.display(new File(this.getDataFolder(), "image.png"));
	}
	
	@Override
	public void onDisable() {
		//moviePlayer.clear();
		//textPlayer.clear();
	}

}
