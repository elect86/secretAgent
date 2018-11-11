package secretAgent.game.starter

import cz.wa.secretagent.Constants
import cz.wa.wautils.math.Rectangle2I
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.springframework.beans.factory.annotation.Required
import secretAgent.game.player.Camera
import secretAgent.view.Settings2D
import java.io.Serializable

/**
 * Info for creating camera viewport and scale.
 *
 * @author Ondrej Milenovsky
 */
class CameraInfo : Serializable {

    lateinit var settings2d: Settings2D
    var preferredVisibleTilesNum: Vector2D? = null // TODO lateinit?
        @Required
        set(preferredVisibleTilesNum) {
            assert(preferredVisibleTilesNum!!.x > 0 && preferredVisibleTilesNum.y > 0){"preferredVisibleTilesNum must be positive"}
            field = preferredVisibleTilesNum
        }

    /**
     * Creates camera at the position with generated viewport and scale.
     * @param pos center position
     * @return new camera
     */
    fun createCamera(pos: Vector2D): Camera {
        val viewport = createViewport()
        val scale = createScale(viewport)
        val screenSize = Vector2I(settings2d.screenWidth, settings2d.screenHeight)
        return Camera(pos, scale, screenSize, viewport)
    }

    /**
     * @return viewport for camera, it covers almost all the screen except HUD
     */
    fun createViewport(): Rectangle2I {
        // the original viewport is 320x192
        return Rectangle2I(0, 0, settings2d.screenWidth,
                FastMath.round(settings2d.screenHeight * 0.9).toInt())
    }

    /**
     * @param viewport camera viewport
     * @return scale for camera so it can see predefined number of tiles
     */
    fun createScale(viewport: Rectangle2I): Double {
        val preferredPixels = (preferredVisibleTilesNum!!.x * preferredVisibleTilesNum!!.y
                * Constants.TILE_SIZE.x * Constants.TILE_SIZE.y)
        val visiblePixels = settings2d.screenWidth * settings2d.screenHeight
        return FastMath.sqrt(visiblePixels / preferredPixels)
    }

    companion object {
        private const val serialVersionUID = 5148932421077764765L
    }
}