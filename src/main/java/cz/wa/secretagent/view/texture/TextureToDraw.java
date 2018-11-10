//package cz.wa.secretagent.view.texture;
//
//import java.awt.image.BufferedImage;
//
//import org.apache.commons.lang.Validate;
//import org.apache.commons.math3.util.FastMath;
//
//import cz.wa.secretagent.utils.lwjgl.Texture;
//import cz.wa.wautils.math.Rectangle2D;
//
///**
// * Texture with bounds, can be a cut of bigger texture.
// *
// * @author Ondrej Milenovsky
// */
//public class TextureToDraw {
//    private final Texture texture;
//    private final Rectangle2D bounds;
//
//    public TextureToDraw(Texture texture, Rectangle2D bounds) {
//        Validate.notNull(texture, "texture is null");
//        Validate.notNull(bounds, "bounds are null");
//        this.texture = texture;
//        this.bounds = bounds;
//    }
//
//    public Texture getTexture() {
//        return texture;
//    }
//
//    /**
//     * @return rectangle in the texture
//     */
//    public Rectangle2D getTileBounds() {
//        return bounds;
//    }
//
//    /**
//     * @return if the texture has at least 1 transparent pixel, no caching so should not be used often,
//     *          if the origImage is no more, returns true
//     */
//    public boolean isTransparent() {
//        BufferedImage img = texture.getOrigImage();
//        if (img == null) {
//            return true;
//        }
//        int x1 = (int) bounds.getX();
//        int x2 = (int) FastMath.ceil(bounds.getX2());
//        int y1 = (int) bounds.getY();
//        int y2 = (int) FastMath.ceil(bounds.getY2());
//        for (int y = y1; y < y2; y++) {
//            for (int x = x1; x < x2; x++) {
//                if (((img.getRGB(x, y) >> 24) & 0xff) != 0xff) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//}
