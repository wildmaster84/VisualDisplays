package me.wildmaster84.imagedisplays;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;

public class ImageDisplays extends JavaPlugin {
	
	public void onEnable() {
        Location loc = new Location(Bukkit.getWorld("world"), -1, 109, 39);
        loadImage(loc, new File(this.getDataFolder(), "image.png"));
	}
	
	
	
	public void loadImage(Location loc, File imagePath) {
        try {
            // Load the image
            BufferedImage image = ImageIO.read(imagePath);

            // Get image width and height
            double width = image.getWidth();
            double height = image.getHeight();

            // Access pixels and extract color information
            for (double y = 0.0; y < height; y++) {
                for (double x = 0.0; x < width; x++) {
                    // Get RGB value of the pixel
                    int rgba = image.getRGB((int)x, (int)y);
                    
                    double locX = loc.getX();
                    
                    // offsets the image to hardcoded height
                    double locY = loc.getY()+0.569;
                    
                    // Aligns the pixels
                    if (x != 0) locX = locX+x-(0.9751*x);
                    if (y != 0) locY = locY-y+(0.9751*y);
                    
                    // Sets the 0-0 for X and Y
                    if (x == 0) locX = locX+x;
                    if (y == 0) locY = locY+y;
                    
                    Location loc2 = new Location(loc.getWorld(), ((double)locX), ((double)locY), loc.getZ());
                    
                    // Convert RGB to Color object
                    Color color = new Color(rgba, true); // true to include alpha
                    TextDisplay display = (TextDisplay) Bukkit.getWorld("world").spawnEntity(loc2, EntityType.TEXT_DISPLAY);
                    // Makes a 1x1 pixel
                    display.setText("");
                    
                    
                    // Sets the pixel color
                    display.setBackgroundColor(org.bukkit.Color.fromARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
