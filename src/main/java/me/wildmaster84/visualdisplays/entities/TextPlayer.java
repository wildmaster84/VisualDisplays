package me.wildmaster84.visualdisplays.entities;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import me.wildmaster84.visualdisplays.VisualDisplays;
import me.wildmaster84.visualdisplays.entities.projector.Projector;
import net.md_5.bungee.api.ChatColor;

public class TextPlayer implements Projector {
	private Location location;
	private ArrayList<TextDisplay> displays;
	int currentFrame = 0;
	private List<String> pixels;
	private Image storedImage;
	
	public TextPlayer(Location loc) {
		location = loc;
		displays = new ArrayList<>();
		pixels = new ArrayList<>();
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		for (TextDisplay textDisplay : displays) {
			if (textDisplay.isValid()) textDisplay.remove();
			
			displays.remove(textDisplay);
		}
	}

	@Override
	public void display(@NotNull Image t) {
		storedImage = t;
		// TODO Auto-generated method stub
	}
	
	
	// Ensures the image isnt a GIF
	// No point in looping a static image
	public void display(@NotNull File file, int x, int y, boolean loop) {
		if (getFileExtension(file.getPath()).equals("gif")) {
			playGIF(file, x, y, loop);
		} else {
			loadImage(file, x, y);
		}
		
	}
	
	// Ensures the GIF plays regardless
	public void display(@NotNull File file, int x, int y) {
		if (getFileExtension(file.getPath()).equals("gif")) {
			display(file, x, y, false);
		} else {
			loadImage(file, x, y);
		}
		
	}
	
	
	private void loadImage(File file, int scaleX, int scaleY) {		
		BufferedImage image;
		pixels.clear();
		try {
			image = ImageIO.read(file);
			storedImage = image;
			double width = image.getWidth();
	        double height = image.getHeight();
	        double entityY = 0.0;
	        for (double y = height - 1; y >= 0; y--) {
	            for (double x = 0; x < width; x++) {
	            	int rgba = image.getRGB((int)x, (int)y);
	                Color color = new Color(rgba, true);
	                if (pixels.size() > 69678*4) break;
	                
	                pixels.add(ChatColor.of(color) + 
	                		"" + 
	                		ChatColor.BOLD + 
	                		"\u2588"
	                );
	                
	                if ((int)x == (int)width-1) {
	                	
	                	Location loc = new Location(location.getWorld(), location.getX(), location.getY() + (entityY/(int)height)*(int)scaleY, location.getZ());
	                	entityY = entityY + 0.0155;
	                	TextDisplay display = (TextDisplay) getEntityAtLocation(loc);
	                	display.setLineWidth(String.join("", pixels).split("\n")[0].length());
	        	        display.setBackgroundColor(org.bukkit.Color.BLACK);
	        	        display.setText(String.join("", pixels).toString());
	        	        
	        	        // clears the cache for new pixels
	        	        pixels.clear();
	        	        
	        	        display.setDisplayWidth(0.0001f);
	        	        display.setDisplayHeight(0.0001f);
	                    display.setTransformation(new Transformation(
	                    		new Vector3f(0.0078f*(int)scaleX, 0, 0), 
	                    		new AxisAngle4f(), 
	                    		new Vector3f((0.0615f/(int)width)*(int)scaleX, (0.0615f/(int)height)*(int)scaleY, 0), 
	                    		new AxisAngle4f()
	                    ));
	                    displays.add(display);
	                    
	                }
	                
	        	}
	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void playGIF(File file, int x, int y, boolean loop) {
		try {
			FileImageInputStream inputStream = new FileImageInputStream(file);
			if (!ImageIO.getImageReaders(inputStream).hasNext()) {
				Bukkit.getLogger().info("No frames were found!");
				return;
			}
			ImageReader reader = ImageIO.getImageReaders(inputStream).next();
			reader.setInput(inputStream);
			int frames = reader.getNumImages(true);
			Bukkit.getLogger().info("Loaded Frames: " + frames);

			
			Bukkit.getScheduler().runTaskTimer(VisualDisplays.instance, (task) -> {
				if (currentFrame < frames) {
					frame(reader, currentFrame, x, y);
					currentFrame++;
				}
				if (currentFrame >= frames) {
					if (!loop) {
						task.cancel();
						return;
					} else {
						currentFrame = 0;
					}
				}
			}, 1L, 1L);
			
		} catch (IOException e) {}
		
	}
	
	private void frame(ImageReader reader, int frameindex, int scaleX, int scaleY) {		
		BufferedImage image;
		pixels.clear();
		try {
			image = reader.read(frameindex);
			double width = image.getWidth();
	        double height = image.getHeight();
	        double entityY = 0.0;
	        for (double y = height - 1; y >= 0; y--) {
	            for (double x = 0; x < width; x++) {
	            	int rgba = image.getRGB((int)x, (int)y);
	                Color color = new Color(rgba, true);
	                
	                
	                // This prevents writing excessive pixels to maliciously crash a player
	                if (pixels.size() > 69678*4) break;
	                
	                pixels.add(ChatColor.of(color) + "" + ChatColor.BOLD + "\u2588");
	                
	                if ((int)x == (int)width-1) {
	                	// We make a new location to preserve the original one
	                	Location loc = new Location(location.getWorld(), location.getX(), location.getY() + entityY*height, location.getZ());
	                	entityY = entityY + 0.000245;
	                	TextDisplay display = (TextDisplay) getEntityAtLocation(loc);
	                	display.setLineWidth(String.join("", pixels).split("\n")[0].length());
	        	        display.setBackgroundColor(org.bukkit.Color.BLACK);
	        	        display.setText(String.join("", pixels).toString());
	        	        
	        	        pixels.clear();
	        	        
	        	        display.setDisplayWidth(0.0001f);
	        	        display.setDisplayHeight(0.0001f);
	        	        
	                    display.setTransformation(new Transformation(
	                    		new Vector3f(0.0078f*(int)width, 0, 0), 
	                    		new AxisAngle4f(), 
	                    		new Vector3f((0.0615f/(int)width)*(int)width, (0.0615f/(int)height)*(int)height, 0), 
	                    		new AxisAngle4f()
	                    ));
	                    displays.add(display);
	                    
	                }
	                
	        	}
	        }
	        
        	
	        
		} catch (IOException e) {}
		
	}
	
	public Entity getEntityAtLocation(Location location) {
		World world = location.getWorld();

        // Use getNearbyEntities to get a list of entities near the specified location
        Collection<Entity> entities = world.getNearbyEntities(location, 0.1, 0.1, 0.1);

        
        // Iterate through the list to find the entity at the exact location
        for (Entity entity : entities) {
            if (entity.getLocation().getX() == location.getX() &&
                entity.getLocation().getY() == location.getY() &&
                entity.getLocation().getZ() == location.getZ()) {
                return entity;
            }
        }
        return location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
    }
	
	private static String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf(".");
        if (lastIndex == -1) {
            return ""; // No file extension found
        }
        return filePath.substring(lastIndex + 1);
    }
	
	

	@Override
	public @Nullable Image getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
