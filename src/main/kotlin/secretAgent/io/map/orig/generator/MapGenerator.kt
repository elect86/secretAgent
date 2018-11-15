package secretAgent.io.map.orig.generator

import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.wautils.math.Vector2I
import org.slf4j.LoggerFactory
import secretAgent.io.map.orig.MapLevel
import secretAgent.io.map.orig.generator.entity.EntityFactory
import secretAgent.io.map.orig.generator.mapping.TileIdMapper
import secretAgent.io.map.orig.generator.mapping.TileMap
import secretAgent.view.model.SimpleModel
import secretAgent.view.renderer.TileId
import secretAgent.world.SamWorld
import secretAgent.world.Tile
import secretAgent.world.TileType
import secretAgent.world.entity.Entity
import java.util.*

/**
 * Generates world from parsed map. Takes care of mapping and creating entities with default properties.
 * Some background tiles might not have linked textures.
 * The generated level contains too many lists and the list may contain tiles, that will never be visible, should be postprocessed.
 *
 * @author Ondrej Milenovsky
 */
class MapGenerator(private val level: MapLevel,
                   private val levelId: Int,
                   graphicsInfo: GraphicsInfo, entityFactory: EntityFactory) {

    private val generator = ObjectGenerator(graphicsInfo, entityFactory, GraphicsInfo.ORIG_LEVEL_TILES_ID)
    private val mapper = TileIdMapper()

    private var world: SamWorld? = null

    /**
     * Generates the world from parsed original map.
     * @ param islandMap if the map is island map with houses or single house
     * @return generated world
     */
    fun generateWorld(): SamWorld {
        val size = level.size
        // background tiles
        val bgTile = generateBackground()
        val bgOverTiles = emptyList<TileMap.Item>() // mapper.mapTile(level.getBgTileOver());
        val world = SamWorld(size, bgTile, levelId)
        this.world = world

        // whole map
        for (y in 0 until size.y) {
            for (x in 0 until size.x) {
                val pos = Vector2I(x, y)

                // reset to background
                val map = world.levelMap
                map.background.set(pos, ArrayList(Arrays.asList(bgTile)))
                map.foreground.set(pos, ArrayList(2))
                map.types.set(pos, TileType.GHOST)

                var tiles: List<TileMap.Item>

                // process back tile
                var origCode = level.tiles[x][y]
                if (isValidTile(pos, origCode)) {
                    // some tile here
                    tiles = mapper.mapTile(origCode, levelId == 0)
                    generateTiles(pos, tiles)
                } else // empty tile here, process bg tile
                    if (bgOverTiles.isNotEmpty())
                        generateTiles(pos, bgOverTiles)

                // process forward tile
                origCode = level.overTiles[x][y]
                if (isValidTile(pos, origCode)) {
                    tiles = mapper.mapTile(origCode, levelId == 0)
                    generateTiles(pos, tiles)
                }
            }
        }
        return world
    }

    private fun isValidTile(pos: Vector2I, origCode: Int)= origCode != TileIdMapper.EMPTY_CODE && (origCode != TileIdMapper.TILE_CODE42 || pos.x > 0)

    private fun generateTiles(pos: Vector2I, tiles: List<TileMap.Item>) {
        for (item in tiles) {
            val p = pos.add(item.pos)
            val obj = generator.generate(p, item.tileId)
            processObject(obj, p)
        }
    }

    private fun processObject(obj: GeneratedObject?, pos: Vector2I) {
        if (obj == null)
            return
        val type = obj.type
        if (type == GeneratedType.ENTITY)
            world!!.entityMap.addEntity(obj.`object` as Entity)
        else {
            val map = world!!.levelMap
            val tile = obj.`object` as Tile
            // update tile type
            if (tile.type.isPreferredTo(map.types.get(pos)))
                map.types.set(pos, tile.type)
            // insert tile
            if (type == GeneratedType.BACKGROUND)
                map.background.get(pos).add(tile)
            else if (type == GeneratedType.FOREGROUND)
                map.foreground.get(pos).add(tile)
        }
    }

    private fun generateBackground(): Tile {
        val bgId = mapper.mapBackground(level.bgTile)
        val obj = generator.generate(null, bgId)!!.`object`
        return when (obj) {
            is Tile -> obj
            else -> Tile(TileType.GHOST, SimpleModel(TileId(GraphicsInfo.ORIG_LEVEL_TILES_ID, bgId))).also {
                logger.warn("Background tile is not simple object")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MapGenerator::class.java)
    }
}
