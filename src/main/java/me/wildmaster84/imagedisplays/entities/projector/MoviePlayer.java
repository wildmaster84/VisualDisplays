package me.wildmaster84.imagedisplays.entities.projector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class MoviePlayer implements Projector {
	private Image pojectorImage;
	private Location location;
	private ArrayList<TextDisplay> displays;
	
	public MoviePlayer(Location loc) {
		location = loc;
		displays = new ArrayList<>();
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
		pojectorImage = t;
		
	}
	
	public void display(@NotNull File file) {
		// TODO Auto-generated method stub
		try {
			pojectorImage = ImageIO.read(file);
			loadImage();
		} catch (IOException e) {
			Bukkit.getLogger().warning("File not found: " + file.getPath());
		}
		
		if (file.toPath().endsWith("gif")) {
			loadImage();
		} else {
			playGIF();
		}
		
	}
	

	@Override
	public @Nullable Image getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void loadImage() {		
		BufferedImage image = flipImageVertical((BufferedImage) pojectorImage);
		double width = image.getWidth();
        double height = image.getHeight();
        
        Vector3f pixleSize;
        for (double y = height - 1; y >= 0; y--) {
            for (double x = 0; x < width; x++) {
            	int rgba = image.getRGB((int)x, (int)y);
                Color color = new Color(rgba, true);
                if (color.getAlpha() != 0) {
                	double locX = location.getX();                    
                    double locY = location.getY();
                    
                    if (x != 0) locX = locX+x-(0.9844*x);
                    if (y != 0) locY = locY+y-(0.9844*y);
                    
                    if (x == 0) locX = locX+x;
                    if (y == 0) locY = locY+y;
                    
                    Location loc2 = new Location(location.getWorld(), locX, locY, location.getZ());
                    TextDisplay display = (TextDisplay) loc2.getWorld().spawnEntity(loc2, EntityType.TEXT_DISPLAY);
                	displays.add(display);
                    
                    display.setText("");                    
                    pixleSize = new Vector3f(0.624f, 0.624f, 0);
                    display.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), pixleSize, new AxisAngle4f()));
                    display.setBackgroundColor(org.bukkit.Color.fromARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
                    
                }
        	}
        }
	}
	
	private void playGIF() {
		
		ImageReader reader = ImageIO.getImageReaders(pojectorImage).next();
		reader.setInput(pojectorImage);
		
		try {
			int frames = reader.getNumImages(true);
			Bukkit.getLogger().info("Loaded Frames: " + frames);
			
			for (int i = 0; i < frames; i++) {
                BufferedImage frame = flipImageVertical(reader.read(i));
                double width = frame.getWidth();
                double height = frame.getHeight();
                
                Vector3f pixleSize;
                for (double y = height - 1; y >= 0; y--) {
                    for (double x = 0; x < width; x++) {
                    	int rgba = frame.getRGB((int)x, (int)y);
                        Color color = new Color(rgba, true);
                        if (color.getAlpha() != 0) {
                        	double locX = location.getX();                    
                            double locY = location.getY();
                            
                            if (x != 0) locX = locX+x-(0.9844*x);
                            if (y != 0) locY = locY+y-(0.9844*y);
                            
                            if (x == 0) locX = locX+x;
                            if (y == 0) locY = locY+y;
                            
                            Location loc2 = new Location(location.getWorld(), locX, locY, location.getZ());
                            TextDisplay display = (TextDisplay) loc2.getWorld().spawnEntity(loc2, EntityType.TEXT_DISPLAY);
                        	displays.add(display);
                            
                            display.setText("");                    
                            pixleSize = new Vector3f(0.624f, 0.624f, 0);
                            display.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), pixleSize, new AxisAngle4f()));
                            display.setBackgroundColor(org.bukkit.Color.fromARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
                            
                        }
                	}
                }
                
            }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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


}
