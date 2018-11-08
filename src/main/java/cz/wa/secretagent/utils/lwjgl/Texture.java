package cz.wa.secretagent.utils.lwjgl;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;

/**
 * Loaded texture.
 * The texture holds reference to buffered image, it should be cleared to save memory.
 * 
 * @author Ondrej Milenovsky
 */
public class Texture {
    private static Texture lastBind = null;

    private final int id;
    private final int width;
    private final int height;

    private BufferedImage origImage;;

    public Texture(int id, BufferedImage origImage) {
        this.id = id;
        this.width = origImage.getWidth();
        this.height = origImage.getHeight();
        this.origImage = origImage;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind() {
        if (lastBind != this) {
            lastBind = this;
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public void dispose() {
        clearOrigImage();
        TextureLoader.disposeTexture(id);
    }

    public BufferedImage getOrigImage() {
        return origImage;
    }

    public void clearOrigImage() {
        origImage = null;
    }

    public static void unbind() {
        lastBind = null;
        glDisable(GL_TEXTURE_2D);
    }

}
