package secretAgent.game.utils

import cz.wa.secretagent.world.map.Tile
import cz.wa.secretagent.world.map.TileType
import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.renderer.TileId

/**
 * Class from sensor about a tile.
 *
 * @author Ondrej Milenovsky
 */
class TileSensing(val type: TileType, pos: Vector2I, tileSize: Vector2D) {
    /**
     * @return moved bounds of the tile
     */
    val bounds: Rectangle2D

    init {
        val sizeX = tileSize.x
        val sizeY = tileSize.y
        bounds = Rectangle2D(-sizeX / 2.0, -sizeY / 2.0, sizeX, sizeY).move(Vector2D(pos.x * sizeX, pos.y * sizeY))
    }
}

/**
 * Tile with position and tile id.
 *
 * @author Ondrej Milenovsky
 */
class TileWithPosition(val tileId: TileId, val tile: Tile, val pos: Vector2I, tileSize: Vector2D) {
    val bounds: Rectangle2D

    init {
        val sizeX = tileSize.x
        val sizeY = tileSize.y
        this.bounds = Rectangle2D(-sizeX / 2.0, -sizeY / 2.0, sizeX, sizeY).move(Vector2D(pos.x * sizeX, pos.y * sizeY))
    }
}
