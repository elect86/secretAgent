package cz.wa.secretagent.view;

import java.io.Serializable;

/**
 * Settings for renderer.
 *
 * @author Ondrej Milenovsky
 */
public class Settings2D implements Serializable {
    private static final long serialVersionUID = -4654618092028293538L;

    public int screenPosX;
    public int screenPosY;

    public int screenWidth;
    public int screenHeight;

    public boolean texFilter;
    public boolean niceMap;

    public int colorBitDepth;
    public int refreshRateHz;

    public boolean fullScreen;
    public boolean vsync;

    public int fps;
}
