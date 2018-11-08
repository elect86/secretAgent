package cz.wa.wautils.image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Utils for images.
 * 
 * @author Ondrej Milenovsky
 */
public class ImageUtils {
    private ImageUtils() {
    }

    /**
     * Makes copy of the input image.
     * @param img input image
     * @return copy
     */
    public static BufferedImage copyImage(BufferedImage img) {
        ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
