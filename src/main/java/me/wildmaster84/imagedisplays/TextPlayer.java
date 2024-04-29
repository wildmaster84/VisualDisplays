package me.wildmaster84.imagedisplays;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import me.wildmaster84.imagedisplays.entities.VisualDisplay;
import net.md_5.bungee.api.ChatColor;

public class TextPlayer implements VisualDisplay<String> {
	private Location location;
	private ArrayList<TextDisplay> displays;
	
	public TextPlayer(Location loc) {
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
	public void display(@NotNull String t) {
		// TODO Auto-generated method stub
	}
	
	public void display(@NotNull File file) {
		loadImage(file);
	}
	
	
	private void loadImage(File file) {		
		BufferedImage image;
		StringBuilder sb = new StringBuilder();
		try {
			image = flipImageVertical(ImageIO.read(file));
			double width = image.getWidth();
	        double height = image.getHeight();

	        TextDisplay display = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        	
	        for (double y = height - 1; y >= 0; y--) {
	            for (double x = 0; x < width; x++) {
	            	int rgba = image.getRGB((int)x, (int)y);
	                Color color = new Color(rgba, true);
	                
	                sb.append(ChatColor.of(color)).append(ChatColor.BOLD).append("█");
	                if ((int)x == (int)width-1) {
	                	sb.append("\n");
	                }
	                
	        	}
	        }
	        display.setLineWidth(sb.toString().split("\n")[0].length());
	        
	        // This should be split into separated lines
	        display.setText(sb.toString());
	        //display.setDisplayWidth(1);
	        
            display.setTransformation(new Transformation(new Vector3f(0.0078f*(int)width, 0, 0), new AxisAngle4f(), new Vector3f((0.0615f/(int)width)*(int)width, (0.0615f/(int)width)*(int)height, 0), new AxisAngle4f()));
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
