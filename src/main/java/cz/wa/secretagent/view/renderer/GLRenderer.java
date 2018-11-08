package cz.wa.secretagent.view.renderer;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.PNGDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.utils.lwjgl.FullScreenSwitcher;
import cz.wa.secretagent.view.Settings2D;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * Renders the world using LWJGL.
 * 
 * @author Ondrej Milenovsky
 */
public class GLRenderer implements SAMRenderer, Serializable {
    private static final long serialVersionUID = -5530236746180863704L;

    private static final Logger logger = LoggerFactory.getLogger(GLRenderer.class);

    private transient boolean initialized = false;

    private Settings2D settings;
    private FullScreenSwitcher fullScreenSwitcher;
    private GeneralRenderer generalRenderer;

    private transient Color lastBgColor;

    public GLRenderer() {
    }

    public GeneralRenderer getGeneralRenderer() {
        return generalRenderer;
    }

    @Required
    public void setGeneralRenderer(GeneralRenderer generalRenderer) {
        this.generalRenderer = generalRenderer;
    }

    public Settings2D getSettings() {
        return settings;
    }

    @Required
    public void setSettings(Settings2D settings) {
        this.settings = settings;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void init() {
        try {
            Display.setLocation(settings.screenPosX, settings.screenPosY);
            if (settings.fullScreen) {
                fullScreenSwitcher.setFullScreen();
            } else {
                Display.setDisplayMode(new DisplayMode(settings.screenWidth, settings.screenHeight));
            }
            Display.setVSyncEnabled(settings.vsync);
            Display.setTitle("Secret Agent");
            ByteBuffer[] icons = new ByteBuffer[] { loadIcon(ClassLoader.getSystemResource("icon16.png")),
                    loadIcon(ClassLoader.getSystemResource("icon32.png")), };
            if (icons != null) {
                Display.setIcon(icons);
            }
            Display.create();
            initialized = true;
        } catch (LWJGLException e) {
            logger.error("Cannot init display", e);
            return;
        } catch (UnsatisfiedLinkError e) {
            logger.error("Missing LWJGL binares, see 'start_help.txt' for instructions", e);
            return;
        }

        initGL();
        generalRenderer.init();
    }

    private static ByteBuffer loadIcon(URL url) {
        InputStream is = null;
        try {
            is = url.openStream();
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
            decoder.decode(bb, decoder.getWidth() * 4, PNGDecoder.RGBA);
            bb.flip();
            return bb;
        } catch (IOException e) {
            logger.error("Loading icon " + url, e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glClearDepth(0.1f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        // enable transparent textures
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (settings.texFilter) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        }

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, settings.screenWidth, settings.screenHeight, 0, -1, 1001);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    @Override
    public void draw(WorldHolder world) {
        if (!initialized) {
            return;
        }
        try {
            drawFrameImpl(world);
        } catch (Throwable e) {
            logger.error("", e);
            if ((e.getMessage() != null)
                    && e.getMessage().equals("No OpenGL context found in the current thread.")) {
                initialized = false;
            }
        }
    }

    @Override
    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    private void drawFrameImpl(WorldHolder world) {
        clearGL(world.getGraphicsInfo().getBgColor());
        generalRenderer.render(world);
        swapBuffers();
    }

    private void clearGL(Color bgColor) {
        if (!bgColor.equals(lastBgColor)) {
            glClearColor(bgColor.getRed() / 255f, bgColor.getGreen() / 255f, bgColor.getBlue() / 255f,
                    bgColor.getAlpha() / 255f);
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void swapBuffers() {
        Display.update();
        Display.sync(settings.fps);
    }

    @Override
    public void dispose() {
        generalRenderer.dispose();
        Display.destroy();
    }

    public FullScreenSwitcher getFullScreenSwitcher() {
        return fullScreenSwitcher;
    }

    @Required
    public void setFullScreenSwitcher(FullScreenSwitcher fullScreenSwitcher) {
        this.fullScreenSwitcher = fullScreenSwitcher;
    }

}
