//package cz.wa.secretagent.utils.lwjgl;
//
//import java.io.Serializable;
//
//import org.lwjgl.LWJGLException;
//import org.lwjgl.input.Mouse;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.opengl.DisplayMode;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//import secretAgent.view.Settings2D;
//
///**
// * Switches to full screen.
// *
// * @author Ondrej Milenovsky
// */
//public class FullScreenSwitcher implements Serializable {
//    private static final long serialVersionUID = -2717407019091038128L;
//
//    private static final Logger logger = LoggerFactory.getLogger(FullScreenSwitcher.class);
//
//    private Settings2D settings;
//
//    private transient boolean mouseGrabbed;
//
//    public Settings2D getSettings() {
//        return settings;
//    }
//
//    @Required
//    public void setSettings(Settings2D settings) {
//        this.settings = settings;
//    }
//
//    /**
//     * Returns if is currently full screen, never throws exceptions.
//     * @return if full screen, false on fail
//     */
//    public boolean isFullScreen() {
//        try {
//            return Display.isFullscreen();
//        } catch (RuntimeException e) {
//            logger.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Sets full screen on/off, never throws exceptions.
//     * @param b true - full screen, false - windowed
//     * @return success
//     */
//    public boolean setFullScreen(boolean b) {
//        if (b) {
//            return setFullScreen();
//        } else {
//            return setWindowed();
//        }
//    }
//
//    /**
//     * Switches to windowed mode, never throws exceptions.
//     * @return success
//     */
//    public boolean setWindowed() {
//        if (!isFullScreen()) {
//            return true;
//        }
//        try {
//            Display.setFullscreen(false);
//            Display.setDisplayMode(new DisplayMode(settings.screenWidth, settings.screenHeight));
//            Mouse.setGrabbed(mouseGrabbed);
//            return true;
//        } catch (LWJGLException e) {
//            logger.error("Cannot switch to window", e);
//            return false;
//        }
//    }
//
//    /**
//     * Switches to full screen and grabs mouse, never throws exceptions.
//     * @return success
//     */
//    public boolean setFullScreen() {
//        if (isFullScreen()) {
//            return true;
//        }
//        int width;
//        int height;
//        int frequency = 60;
//        int colorDepth = 32;
//        if (settings != null) {
//            width = settings.screenWidth;
//            height = settings.screenHeight;
//            frequency = settings.refreshRateHz;
//            colorDepth = settings.colorBitDepth;
//        } else {
//            width = Display.getWidth();
//            height = Display.getHeight();
//        }
//        mouseGrabbed = Mouse.isGrabbed();
//
//        try {
//            DisplayMode[] modes = Display.getAvailableDisplayModes();
//            for (int i = 0; i < modes.length; i++) {
//                if ((modes[i].getWidth() == width) && (modes[i].getHeight() == height)
//                        && (modes[i].getBitsPerPixel() == colorDepth)
//                        && (modes[i].getFrequency() == frequency) && modes[i].isFullscreenCapable()) {
//                    Display.setDisplayMode(modes[i]);
//                    Display.setFullscreen(true);
//                    Mouse.setGrabbed(true);
//                    return true;
//                }
//            }
//        } catch (LWJGLException e) {
//            logger.error("Cannot switch to full screen", e);
//            return false;
//        }
//        logger.warn("No full screen mode available for " + width + " x " + height + " x " + colorDepth + " @"
//                + frequency + "Hz");
//        return false;
//    }
//}
