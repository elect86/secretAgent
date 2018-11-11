package secretAgent.game.utils

import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.map.Tile
import cz.wa.secretagent.world.map.TileType
import cz.wa.wautils.collection.Array2D
import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.view.renderer.TileId
import secretAgent.world.SamWorld
import java.util.ArrayList

/**
 * Finds some info about an entity.
 *
 * @author Ondrej Milenovsky
 */
class EntityObserver(private val entity: Entity, private val world: SamWorld) {

    /** @return true if there is wall, shelf, platform or door under the entity, false if in the air     */
    val isOnGround: Boolean
        get() {
            val bounds = entity.sizeBounds.move(entity.pos)
            return isStandingOnTile(bounds) || isStandingOnEntity(bounds)
        }

    /** @return array of 2 tiles under the entity, if some tile is outside map, the type is null     */
    val tilesUnder: Array<TileSensing>
        get() {
            val tileSize = world.levelMap.tileSize
            val pos = entity.pos
            val ix = (pos.x / tileSize.x).toInt()
            val iy = (pos.y / tileSize.y).toInt() + 1

            val types = world.levelMap.types
            val ret = arrayOfNulls<TileSensing>(2)
            val p1 = Vector2I(ix, iy)
            val p2 = Vector2I(ix + 1, iy)
            return arrayOf(
                    TileSensing(if (types.isInside(p1)) types.get(p1) else TileType.WALL, p1, tileSize),
                    TileSensing(if (types.isInside(p2)) types.get(p2) else TileType.WALL, p2, tileSize))
        }

    /** @return bounds of first solid object that touches the entity or null     */
    val touchingSolidObject: Rectangle2D?
        get() {
            for (tile in collidingTiles)
                if (tile.type == TileType.WALL)
                    return tile.bounds
            val bounds = entity.sizeBounds.move(entity.pos)
            for (solid in EntitiesFinder(world).solidEntities)
                if (solid !== entity) {
                    val solidBounds = solid.getSizeBounds().move(solid.getPos())
                    if (bounds.intersects(solidBounds))
                        return solidBounds
                }
            return null
        }

    /** @return list of all tiles that collides with the entity, the tiles don't have to be walls     */
    // tile index
    val collidingTiles: List<TileSensing>
        get() {
            val map = world.levelMap

            val eb = entity.sizeBounds.move(entity.pos)

            val tileSize = map.tileSize
            val ix1 = (eb.x / tileSize.x + 0.5).toInt()
            val iy1 = (eb.y / tileSize.y + 0.5).toInt()
            val ix2 = (eb.x2 / tileSize.x + 0.5).toInt()
            val iy2 = (eb.y2 / tileSize.y + 0.5).toInt()

            val ret = ArrayList<TileSensing>((ix2 - ix1 + 1) * (iy2 - iy1 + 1))
            for (y in iy1..iy2)
                for (x in ix1..ix2) {
                    val i = Vector2I(x, y)
                    if (map.types.isInside(i))
                        ret.add(TileSensing(map.types.get(i), i, tileSize))
                }
            return ret
        }

    /** If the entity is standing on some other entity. The other entity can be solid usable, platform or can.     */
    private fun isStandingOnEntity(entityBounds: Rectangle2D): Boolean {
        for (entity2 in EntitiesFinder(world).solidEntities)
            if (isStandingOn(entityBounds, entity2.sizeBounds.move(entity2.pos)))
                return true
        return false
    }

    /** If the entity is standing on WALL or SHELF some tile from the world.     */
    private fun isStandingOnTile(entityBounds: Rectangle2D): Boolean {
        val tilesUnder = tilesUnder
        return isStandingOnTile(entityBounds, tilesUnder[0]) || isStandingOnTile(entityBounds, tilesUnder[1])
    }

    /** If the entity is standing on WALL or SHELF the tile from the world.     */
    private fun isStandingOnTile(entityBounds: Rectangle2D, tile: TileSensing): Boolean {
        return when {
            tile.type == TileType.WALL || tile.type == TileType.SHELF -> isStandingOn(entityBounds, tile.bounds)
            else -> false
        }
    }

    /**
     * @param entity2 the lower entity
     * @return true if the entity is standing on entity2
     */
    fun isStandingOn(entity2: Entity): Boolean = isStandingOn(entity2.sizeBounds.move(entity2.pos))

    /**
     * @param entity2Bounds bounds of the lower entity
     * @return true if the entity is standing on entity2
     */
    fun isStandingOn(entity2Bounds: Rectangle2D): Boolean = isStandingOn(entity.sizeBounds.move(entity.pos), entity2Bounds)

    /** If the entity is standing on item     */
    private fun isStandingOn(entityBounds: Rectangle2D, itemBounds: Rectangle2D): Boolean {
        if (entityBounds.x >= itemBounds.x2 || entityBounds.x2 <= itemBounds.x)
            return false
        val dy = FastMath.abs(itemBounds.y - entityBounds.y2)
        return dy <= FLOOR_MAX_DIST_STAND
    }

    /**
     * Get 9 closest tiles around the entity.
     * @param tileIds return only tiles with these ids
     * @return list of tiles
     */
    fun get9TilesAround(tileIds: Collection<TileId>): List<TileWithPosition> {
        val types = world.levelMap.types
        val background = world.levelMap.background
        val foreground = world.levelMap.foreground
        val tileSize = world.levelMap.tileSize

        val ret = ArrayList<TileWithPosition>(9)
        val x1 = FastMath.round(entity.pos.x / tileSize.x).toInt()
        val y1 = FastMath.round(entity.pos.y / tileSize.y).toInt()
        for (iy in y1 - 1..y1 + 1)
            for (ix in x1 - 1..x1 + 1) {
                val i = Vector2I(ix, iy)
                if (types.isInside(i)) {
                    findTiles(tileIds, background, tileSize, ret, i)
                    findTiles(tileIds, foreground, tileSize, ret, i)
                }
            }
        return ret
    }

    /** Finds and adds to the list tiles at the position with specified ids     */
    private fun findTiles(tileIds: Collection<TileId>, background: Array2D<List<Tile>>, tileSize: Vector2D,
                          ret: MutableList<TileWithPosition>, i: Vector2I) {
        for (tile in background.get(i))
            for (tileId in tileIds)
                if (tile.hasTileId(tileId))
                    ret += TileWithPosition(tileId, tile, i, tileSize)
    }

    companion object {
        val FLOOR_MAX_DIST_STAND = 0.1
    }
}