package cz.wa.secretagent;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Global constants. 
 * 
 * @author Ondrej Milenovsky
 */
public class Constants {
    public static final String ENCODING = "UTF-8";
    public static final Vector2D TILE_SIZE = new Vector2D(16, 16);
    public static final double TILE_LENGTH = (TILE_SIZE.getX() + TILE_SIZE.getY()) / 2.0;

    private Constants() {
    }
}
