package cz.wa.secretagent.utils.lwjgl;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

/**
 * Buffered texture for LWJGL. 
 * 
 * @author Ondrej Milenovsky
 */
public class BufferedTexture {

    private final int width;
    private final int height;

    private int frameBufferId;
    private int textureId;
    private int renderBufferId;

    public BufferedTexture(int width, int height) {
        Validate.isTrue((width > 0) && (height > 0), "width and height must be > 0");
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        if (!isSupported()) {
            throw new IllegalStateException("The graphics driver must suppoert framebuffer object");
        }

        // create a new framebuffer
        frameBufferId = glGenFramebuffersEXT();
        // and a new texture used as a color buffer
        textureId = glGenTextures();
        // And finally a new depthbuffer
        renderBufferId = glGenRenderbuffersEXT();

        // switch to the new framebuffer
        begin();

        // initialize color texture
        // Bind the colorbuffer texture
        glBindTexture(GL_TEXTURE_2D, textureId);
        // make it nearest
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // Create the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (ByteBuffer) null);
        // attach it to the framebuffer
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, textureId, 0);

        // initialize depth renderbuffer
        // bind the depth renderbuffer
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, renderBufferId);
        // get the data space for it
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height);
        // bind it to the renderbuffer
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
                renderBufferId);

        // Switch back to normal rendering
        end();
    }

    /**
     * Begins rendering on this buffer.
     */
    public void begin() {
        Texture.unbind();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBufferId);
    }

    /**
     * Ends rendering on this buffer.
     */
    public void end() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Draws the buffered texture on screen.
     * @param pos upper left corner
     * @param size size
     */
    public void draw(Vector2D pos, Vector2D size) {
        glBindTexture(GL_TEXTURE_2D, textureId);

        glBegin(GL_QUADS);
        glVertex2d(pos.getX(), pos.getY());
        glVertex2d(pos.getX() + size.getX(), pos.getY());
        glVertex2d(pos.getX() + size.getX(), pos.getY() + size.getY());
        glVertex2d(pos.getX(), pos.getY() + size.getY());
        glEnd();

        Texture.unbind();
    }

    public void dispose() {
        glDeleteTextures(textureId);
        glDeleteFramebuffersEXT(frameBufferId);
        glDeleteRenderbuffersEXT(renderBufferId);
    }

    public static boolean isSupported() {
        return GLContext.getCapabilities().GL_EXT_framebuffer_object;
    }
}
