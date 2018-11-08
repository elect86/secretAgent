package cz.wa.secretagent.utils.lwjgl;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/**
 * Loads texture from file and uses in LWJGL. GL must be initialized before loading!
 * 
 * Code found on Stack overflow and modified.
 */
public class TextureLoader {
    private static final int BYTES_PER_PIXEL = 4; //3 for RGB, 4 for RGBA
    /** If the color is set, then all pixels, that have alpha=0 are set to this color.
     *  If texture filter is on, then the color makes difference even if its alpha=0.
     *  */
    private static Color bgColor;

    private TextureLoader() {
    }

    public static Texture loadTexture(File file, boolean texFilter) throws IOException {
        //org.newdawn.slick.opengl.TextureLoader.getTexture("png", new FileInputStream(file));
        BufferedImage img = ImageIO.read(file);
        return useTexture(img, texFilter);
    }

    public static Texture useTexture(BufferedImage image, boolean texFilter) {

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight()
                * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                if (((pixel >> 24) & 0xFF) == 0) {
                    pixel = getBgColor().getRGB();
                }
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
                buffer.put((byte) (pixel & 0xFF)); // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using 
        // whatever OpenGL method you want, for example:

        int textureId = glGenTextures(); //Generate texture ID

        glBindTexture(GL_TEXTURE_2D, textureId); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);

        //Setup texture scaling filtering
        if (texFilter) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        }

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA,
                GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return new Texture(textureId, image);
    }

    public static void disposeTexture(int textureId) {
        glDeleteTextures(textureId);
    }

    public static Color getBgColor() {
        return bgColor;
    }

    public static void setBgColor(Color bgColor) {
        TextureLoader.bgColor = bgColor;
    }

}
