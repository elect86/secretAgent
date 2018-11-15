package secretAgent.world

import cz.wa.secretagent.Constants
import cz.wa.wautils.collection.Array2D
import cz.wa.wautils.collection.Array2DImpl
import cz.wa.wautils.math.Rectangle2I
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.io.map.orig.generator.ObjectGenerator
import secretAgent.view.renderer.TileId
import java.util.*

/**
 * Animated tile with offset in seconds.
 *
 * @author Ondrej Milenovsky
 */
class AnimatedTile(type: TileType, model: ObjectModel, val offsetS: Double) : Tile(type, model)

/**
 * Level map containing all static tiles.
 * Consists of two arrays of lists of tiles:
 * background - tiles behind entities,
 * foreground - tiles in front of entities
 *
 * Each list consists of one or multiple tiles sorted from behind.
 *
 * The level also contains array of tile types.
 *
 * @author Ondrej Milenovsky
 */
class LevelMap(val size: Vector2I, val backgroundTile: Tile?) {

    val background: Array2D<MutableList<Tile>>
    val foreground: Array2D<MutableList<Tile>>
    val types: Array2D<TileType>
    val storedTiles: MutableSet<StoredTile> = LinkedHashSet()
    var timeMs: Long = 0
        private set

    val tileSize: Vector2D
        get() = Constants.TILE_SIZE

    init {
        val rect = Rectangle2I(size.x, size.y)
        foreground = Array2DImpl(rect)
        background = Array2DImpl(rect)
        types = Array2DImpl(rect)
    }

    fun addTime(timeDMs: Long) {
        timeMs += timeDMs
    }

    /**
     * Add new tile to the map
     * @param tile tile to add
     */
    fun addTile(tile: StoredTile) {
        if (ObjectGenerator.FRONT_TYPES.contains(tile.tile.type))
            addTile(foreground, tile, true)
        else
            addTile(background, tile, true)
        val pos = tile.pos
        val type = types.get(pos)
        if (tile.tile.type.isPreferredTo(type))
            types.set(pos, tile.tile.type)
    }

    /**
     * Adds tile to the array, the cell can contain unmodifiable list if size <= 1
     * @param array array where to add
     * @param tile tile to add
     */
    private fun addTile(array: Array2D<MutableList<Tile>>, tile: StoredTile, addLast: Boolean) {
        val list = array.get(tile.pos)
        if (addLast)
            list += tile.tile
        else
            list.add(0, tile.tile)
        array.set(tile.pos, list)
    }

    /**
     * Update type at this position.
     * @param i position
     */
    fun updateType(i: Vector2I) {
        var type: TileType? = null
        for (tile in background.get(i))
            if (tile.type.isPreferredTo(type))
                type = tile.type
        for (tile in foreground.get(i))
            if (tile.type.isPreferredTo(type))
                type = tile.type
        types.set(i, type)
    }

    /**
     * Removes the tile at the position. If the first tile in background is not the background tile,
     * adds background tile here.
     * @param pos position
     * @param tile tile to remove
     * @throws IllegalArgumentException if the tile is not in the map
     */
    fun removeTile(pos: Vector2I, tile: Tile) {
        var removed = tryRemoveTile(pos, tile, background)
        if (!removed)
            removed = tryRemoveTile(pos, tile, foreground)
        if (!removed)
            throw IllegalArgumentException("Tile ${tile.type} is not at $pos")
        val bgList = background.get(pos)
        if (bgList.isEmpty() || bgList[0] !== backgroundTile)
            addTile(background, StoredTile(pos, backgroundTile!!), false)
    }

    private fun tryRemoveTile(pos: Vector2I, tile: Tile, array: Array2D<MutableList<Tile>>): Boolean {
        var list = array.get(pos)
        if (list.size == 1 && list[0] === tile) {
            list = mutableListOf()
            array.set(pos, list)
            return true
        }
        val it = list.iterator()
        while (it.hasNext()) {
            val tile2 = it.next()
            if (tile2 === tile) {
                it.remove()
                if (list.size == 1) {
                    list = arrayListOf(list[0])
                    array.set(pos, list)
                }
                return true
            }
        }
        return false
    }

    fun getNearestTilePos(pos: Vector2D): Vector2I {
        val x = FastMath.round(pos.x / tileSize.x).toInt()
        val y = FastMath.round(pos.y / tileSize.y).toInt()
        return Vector2I(x, y)
    }
}

/**
 * Tile with position.
 *
 * @author Ondrej Milenovsky
 */
class StoredTile(val pos: Vector2I, val tile: Tile)

/**
 * Single static tile.
 *
 * @author Ondrej Milenovsky
 */
open class Tile(val type: TileType, val model: ObjectModel) {
    fun hasTileId(tileId: TileId): Boolean = tileId in model.allTileIds
}

/**
 * Type of tile
 *
 * @author Ondrej Milenovsky
 */
enum class TileType(
        /** type priority, lesser means more important  */
        private val priority: Int) {

    /** background tile  */
    GHOST(4),
    /** foreground tile  */
    GHOST_FRONT(100),
    /** wall  */
    WALL(0),
    /** shelf, agent can jump through it but cannot fall  */
    SHELF(1),
    /** spikes, impales agents  */
    SPIKES(2),
    /** water, agents will drown  */
    WATER(3),
    /** this should never get to the level, used in parser  */
    ENTITY(Integer.MAX_VALUE);

    infix fun isPreferredTo(o: TileType?) = o?.let { priority < o.priority } ?: true
}
