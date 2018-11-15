package secretAgent.world

import cz.wa.secretagent.world.EntityMap
import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Vector2I

/**
 * World class containing all current entities, background and items.
 *
 * @author Ondrej Milenovsky
 */
class SamWorld
/**
 * Creates paused world.
 * @param size map size
 * @param island true if it is island map
 */
(size: Vector2I, backgroundTile: Tile?, val levelId: Int) {

    val levelMap = LevelMap(size, backgroundTile)
    val entityMap: EntityMap = EntityMap()
    var isRunning: Boolean = false

    val isIsland: Boolean
        get() = levelId == 0

    val isEmpty: Boolean
        get() = levelMap.size == Vector2I.ZERO

    /**
     * @return visible bounds of the world
     */
    val bounds: Rectangle2D
        get() {
            val tileSize = levelMap.tileSize
            val width = levelMap.size.x * tileSize.x
            val height = levelMap.size.y * tileSize.y
            return Rectangle2D(-tileSize.x / 2.0, -tileSize.y / 2.0, width, height)
        }

    fun addSimTimeMs(timeMs: Long) = levelMap.addTime(timeMs)
}
