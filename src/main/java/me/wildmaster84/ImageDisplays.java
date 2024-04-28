package me.wildmaster84;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;

import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class ImageDisplays extends JavaPlugin {
	
	public void onEnable() {
		Location loc = new Location(Bukkit.getWorld("world"), -1, 109, 39);
		
		loadFrame(loc, new File(this.getDataFolder(), "image.png"));
	}
	
	
	
	public void loadFrame(Location loc, File imagePath) {
        try {
            // Load the image
            BufferedImage rawImage = ImageIO.read(imagePath);
            // Get image width and height
            double width = rawImage.getWidth();
            double height = rawImage.getHeight();
            BufferedImage image = flipImageVertical(rawImage);
            
            // Size of a pixel
            Vector3f pixleSize;
            // Access pixels and extract color information
            for (double y = height - 1; y >= 0; y--) { // Reverse the iteration order of y
                for (double x = 0; x < width; x++) {
                	if (!shouldCutPixels(x, y)) {
                		int rgba = image.getRGB((int)x, (int)y);
                        Color color = new Color(rgba, true);
                        if (color.getAlpha() != 0) {
                        	double locX = loc.getX();                    
                            double locY = loc.getY();
                            
                            // Aligns the pixels
                            if (x != 0) locX = locX+x-(0.9844*x);
                            if (y != 0) locY = locY+y-(0.9844*y);
                            
                            // Sets the 0-0 for X and Y
                            if (x == 0) locX = locX+x;
                            if (y == 0) locY = locY+y;
                            
                            Location loc2 = new Location(loc.getWorld(), locX, locY, loc.getZ());
                            TextDisplay display;
                            // Convert RGB to Color object
                            if (Bukkit.getWorld("world").getNearbyEntities(loc2, 0.01, 0.01, 0.01).size() != 0) {
                            	display = (TextDisplay) Bukkit.getWorld("world").getNearbyEntities(loc2, 0.01, 0.01, 0.01).iterator().next();
                            } else {
                            	display = (TextDisplay) Bukkit.getWorld("world").spawnEntity(loc2, EntityType.TEXT_DISPLAY);
                            }
                            
                            display.setText("");
                            
                            //pixleSize = new Vector3f(1.248f, 1.248f, 0);
                            pixleSize = new Vector3f(0.624f, 0.624f, 0);
                            // Sets pixel size
                            display.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), pixleSize, new AxisAngle4f()));
                            
                            // Sets the pixel color
                            display.setBackgroundColor(org.bukkit.Color.fromARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
                        }
                	}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

	private boolean shouldCutPixels(double x, double y) {
	    // Define the region where pixels should be compressed
	    double minX = 0;  // Example: minimum x-coordinate of the region
	    double minY = 0;  // Example: minimum y-coordinate of the region
	    double maxX = 720;  // Example: maximum x-coordinate of the region
	    double maxY = 480;  // Example: maximum y-coordinate of the region
	    
	    // Check if the pixel lies within the specified region
	    if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
	        return false; // Skip compression for pixels in the specified region
	    } else {
	        return true;  // Compress pixels outside the specified region
	    }
	}

}
