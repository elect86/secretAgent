package cz.wa.secretagent.game.starter;

import cz.wa.secretagent.Constants;
import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;
import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.game.player.Camera;
import secretAgent.view.renderer.Settings2D;

import java.io.Serializable;

/**
 * Info for creating camera viewport and scale. 
 * 
 * @author Ondrej Milenovsky
 */
public class CameraInfo implements Serializable {

    private static final long serialVersionUID = 5148932421077764765L;

    private Settings2D settings2d;
    private Vector2D preferredVisibleTilesNum;

    /**
     * Creates camera at the position with generated viewport and scale.
     * @param pos center position
     * @return new camera
     */
    public Camera createCamera(Vector2D pos) {
        Rectangle2I viewport = createViewport();
        double scale = createScale(viewport);
        Vector2I screenSize = new Vector2I(settings2d.screenWidth, settings2d.screenHeight);
        return new Camera(pos, scale, screenSize, viewport);
    }

    /**
     * @return viewport for camera, it covers almost all the screen except HUD
     */
    public Rectangle2I createViewport() {
        // the original viewport is 320x192
        return new Rectangle2I(0, 0, settings2d.screenWidth,
                (int) FastMath.round(settings2d.screenHeight * 0.9));
    }

    /**
     * @param viewport camera viewport
     * @return scale for camera so it can see predefined number of tiles
     */
    public double createScale(Rectangle2I viewport) {
        double preferredPixels = getPreferredVisibleTilesNum().getX() * getPreferredVisibleTilesNum().getY()
                * Constants.TILE_SIZE.getX() * Constants.TILE_SIZE.getY();
        int visiblePixels = settings2d.screenWidth * settings2d.screenHeight;
        double scale = FastMath.sqrt(visiblePixels / preferredPixels);
        return scale;
    }

    public Settings2D getSettings2d() {
        return settings2d;
    }

    @Required
    public void setSettings2d(Settings2D settings2d) {
        this.settings2d = settings2d;
    }

    public Vector2D getPreferredVisibleTilesNum() {
        return preferredVisibleTilesNum;
    }

    @Required
    public void setPreferredVisibleTilesNum(Vector2D preferredVisibleTilesNum) {
        Validate.isTrue((preferredVisibleTilesNum.getX() > 0) && (preferredVisibleTilesNum.getY() > 0),
                "preferredVisibleTilesNum must be positive");
        this.preferredVisibleTilesNum = preferredVisibleTilesNum;
    }

}
