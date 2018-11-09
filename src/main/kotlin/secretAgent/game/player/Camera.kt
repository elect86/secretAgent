package secretAgent.game.player

import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Rectangle2I
import cz.wa.wautils.math.Vector2I
import org.apache.commons.lang.Validate
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import kotlin.properties.Delegates

/**
 * Camera looking on the world.
 *
 * @author Ondrej Milenovsky
 */
class Camera {

    /** @return center position in world where is the camera looking    */
    var pos: Vector2D
        set(value) {
            field = value
            worldBounds = null
        }

    /** @return pixel scale, 2.0 means 1 pixel in world will be displayed as 2x2 square     */
    var scale: Double
        set(value) {
            assert(value != 0.0) { "scale is 0" }
            field = value
            worldBounds = null
        }

    /**
     * @param screenSize total screen size in pixels
     */
    var screenSize: Vector2I

    /** @return viewport in screen pixels   */
    private var viewport: Rectangle2I
        set(value) {
            field = value
            worldBounds = null
            // TODO better bounds
            hudBounds = Rectangle2I(0, value.y2, screenSize.x, screenSize.y - value.y2)
        }

    /** @return bounds where the camera can see in the world, null if used HUD camera   */
    var worldBounds: Rectangle2D? = null
        get() {
            if (viewport.width == 0)
                return null
            if (field == null)
                field = computeWorldBounds()
            return field
        }

    var hudBounds: Rectangle2I? = null
        private set
    private var drawingHud: Boolean = false

    /**
     * Creates world camera
     * @param pos center of the camera in world coords
     * @param scale drawn scale
     * @param screenSize screen size
     * @param viewport viewport on screen
     */
    constructor(pos: Vector2D, scale: Double, screenSize: Vector2I, viewport: Rectangle2I) {
        this.pos = pos
        this.scale = scale
        this.screenSize = screenSize
        this.viewport = viewport
        drawingHud = false
    }

    /**
     * Creates camera for HUD
     * @param screenSize
     */
    constructor(screenSize: Vector2I, scale: Double) {
        this.screenSize = screenSize
        this.scale = scale
        pos = Vector2D.ZERO
        viewport = Rectangle2I(0, 0)
        drawingHud = true
    }

    /**
     * Ensures that the camera does not see outside the map.
     * @param mapBounds max visible bounds
     */
    fun limitInMap(mapBounds: Rectangle2D) {
        var wb = worldBounds!!
        // check scale
        if (wb.width > mapBounds.width) {
            scale = mapBounds.width / wb.width
            wb = computeWorldBounds()
        }
        if (wb.height > mapBounds.height) {
            scale = mapBounds.height / wb.height
            wb = computeWorldBounds()
        }
        // check pos
        var moved = false
        // upper left corner
        if (wb.x < mapBounds.x) {
            pos = pos.add(Vector2D(mapBounds.x - wb.x, 0.0))
            moved = true
        }
        if (wb.y < mapBounds.y) {
            pos = pos.add(Vector2D(0.0, mapBounds.y - wb.y))
            moved = true
        }
        if (moved) {
            wb = computeWorldBounds()
        }
        worldBounds = wb
        // lower right corner
        if (wb.x2 > mapBounds.x2) {
            pos = pos.add(Vector2D(mapBounds.x2 - wb.x2, 0.0))
            moved = true
        }
        if (wb.y2 > mapBounds.y2) {
            pos = pos.add(Vector2D(0.0, mapBounds.y2 - wb.y2))
            moved = true
        }
        if (moved) {
            worldBounds = null
        }
    }

    private fun computeWorldBounds(): Rectangle2D {
        var scale = this.scale
        if (drawingHud) {
            scale = 1.0
        }
        val wbw = viewport.width / scale
        val wbh = viewport.height / scale
        return Rectangle2D(pos.x - wbw / 2, pos.y - wbh / 2, wbw, wbh)
    }

    fun getScreenPos(worldPos: Vector2D): Vector2D {
        var scale = this.scale
        if (drawingHud) {
            scale = 1.0
        }
        return worldPos.subtract(pos).scalarMultiply(scale)
                .add(Vector2D(viewport.width / 2.0, viewport.height / 2.0))
    }

}
