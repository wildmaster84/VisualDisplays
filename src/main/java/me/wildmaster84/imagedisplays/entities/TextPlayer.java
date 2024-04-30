package me.wildmaster84.imagedisplays.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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

import me.wildmaster84.imagedisplays.ImageDisplays;
import me.wildmaster84.imagedisplays.entities.projector.Projector;
import net.md_5.bungee.api.ChatColor;

public class TextPlayer implements Projector {
	private Location location;
	private ArrayList<TextDisplay> displays;
	int currentFrame = 0;
	List<String> pixels;
	
	public TextPlayer(Location loc) {
		location = loc;
		displays = new ArrayList<>();
		pixels = new ArrayList<>();
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		for (TextDisplay textDisplay : displays) {
			textDisplay.remove();
			displays.remove(textDisplay);
		}
	}

	@Override
	public void display(@NotNull Image t) {
		// TODO Auto-generated method stub
	}
	
	
	// Ensures the image isnt a GIF
	// No point in looping a static image
	public void display(@NotNull File file, boolean loop) {
		if (getFileExtension(file.getPath()).equals("gif")) {
			playGIF(file, loop);
		} else {
			loadImage(file, 64, 64);
		}
		
	}
	
	// Ensures the GIF plays regardless
	public void display(@NotNull File file) {
		if (getFileExtension(file.getPath()).equals("gif")) {
			display(file, false);
		} else {
			loadImage(file, 64, 64);
		}
		
	}
	
	
	private void loadImage(File file, int scaleX, int scaleY) {		
		BufferedImage image;
		pixels.clear();
		try {
			image = flipImageVertical(ImageIO.read(file));
			double width = image.getWidth();
	        double height = image.getHeight();

	        TextDisplay display;
	        if (getEntityAtLocation(location) == null) {
	        	display = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
	        } else {
	        	display = (TextDisplay) getEntityAtLocation(location);
	        }
        	
	        for (double y = height - 1; y >= 0; y--) {
	            for (double x = 0; x < width; x++) {
	            	int rgba = image.getRGB((int)x, (int)y);
	                Color color = new Color(rgba, true);
	                if (pixels.size() > 69678*4) break;
	                
	                pixels.add(ChatColor.of(color) + "" + ChatColor.BOLD + "█" + ((int)x == (int)width-1 && (int)y != 0 ? "\n" : ""));
	                
	        	}
	        }
	        display.setLineWidth(String.join("", pixels).split("\n")[0].length());
	        
	        // This should be split into separated lines
	        display.setText(String.join("", pixels).toString());
	        
            display.setTransformation(new Transformation(new Vector3f(0.0078f*(int)scaleX, 0, 0), new AxisAngle4f(), new Vector3f((0.0615f/(int)width)*(int)scaleX, (0.0615f/(int)height)*(int)scaleY, 0), new AxisAngle4f()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void playGIF(File file, boolean loop) {
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
			
			Bukkit.getScheduler().runTaskTimer(ImageDisplays.instance, (task) -> {
				if (currentFrame < frames) {
					frame(reader, currentFrame);
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
	
	private void frame(ImageReader reader, int frameindex) {		
		BufferedImage image;
		pixels.clear();
		try {
			image = flipImageVertical(reader.read(frameindex));
			double width = image.getWidth();
	        double height = image.getHeight();
	        TextDisplay display;
	        if (getEntityAtLocation(location) == null) {
	        	display = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
	        } else {
	        	display = (TextDisplay) getEntityAtLocation(location);
	        }
        	
	        for (double y = height-1; y >= 0; y--) {
	            for (double x = 0; x < width; x++) {
	            	int rgba = image.getRGB((int)x, (int)y);
	                Color color = new Color(rgba, true);
	                
	                if (pixels.size() > 69678*4) break;
	                
	                pixels.add(ChatColor.of(color) + "" + ChatColor.BOLD + "█" + ((int)x == (int)width-1 && (int)y != 0 ? "\n" : ""));
	                
	        	}
	        }
	        display.setLineWidth(String.join("", pixels).split("\n")[0].length());
	        
	        // This should be split into separated lines
	        display.setText(String.join("", pixels).toString());
	        
            display.setTransformation(new Transformation(new Vector3f(0.0078f*(int)width, 0, 0), new AxisAngle4f(), new Vector3f((0.0615f/(int)width)*(int)width, (0.0615f/(int)width)*(int)height, 0), new AxisAngle4f()));
		} catch (IOException e) {}
		
	}
	
	private static BufferedImage flipImageVertical(BufferedImage image) {
	    // Create a new BufferedImage for the flipped image
	    BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	
	    // Create AffineTransform for flipping vertically
	    AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
	    tx.translate(0, -image.getHeight());
	
	    // Create Graphics2D object to draw the flipped image
	    Graphics2D g2d = flippedImage.createGraphics();
	    g2d.drawImage(image, tx, null);
	    g2d.dispose();
	
	    return flippedImage;
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
        return null;
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
