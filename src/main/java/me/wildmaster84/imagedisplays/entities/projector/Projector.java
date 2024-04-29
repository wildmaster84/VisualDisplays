package me.wildmaster84.imagedisplays.entities.projector;

import me.wildmaster84.imagedisplays.entities.VisualDisplay;
import org.jetbrains.annotations.Nullable;

import java.awt.Image;

public interface Projector extends VisualDisplay<Image> {
    @Nullable
    Image getDisplay();
}
