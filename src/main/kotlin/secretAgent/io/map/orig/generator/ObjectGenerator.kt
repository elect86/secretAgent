package secretAgent.io.map.orig.generator

import cz.wa.secretagent.Constants
import cz.wa.secretagent.world.map.AnimatedTile
import cz.wa.secretagent.world.map.Tile
import cz.wa.secretagent.world.map.TileType
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import cz.wa.secretagent.worldinfo.graphics.TileInfo
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.io.map.orig.generator.entity.EntityFactory
import secretAgent.view.model.AnimatedModel
import secretAgent.view.model.SimpleModel
import secretAgent.view.renderer.TileId
import java.util.*

/**
 * Generates object from tile id. The object can be tile, back tile or entity.
 * Some background tiles might not have linked texture.
 *
 * @author Ondrej Milenovsky
 */
class ObjectGenerator(private val graphicsInfo: GraphicsInfo,
                      private val entityFactory: EntityFactory,
                      private val tileSetId: Int) {

    private val staticTiles: MutableMap<TileId, GeneratedObject> = HashMap()

    fun generate(p: Vector2I?, tileId: Int): GeneratedObject? {
        if (tileId < 0)
            return null
        val pos = generatePos(p)
        return generateObject(pos, TileId(tileSetId, tileId))
    }

    /** Creates object (tiles with caching).     */
    private fun generateObject(pos: Vector2D?, tileId: TileId): GeneratedObject? {
        val tileSet = graphicsInfo.getTileSet(tileSetId)
        val tileInfo = tileSet.getTile(tileId.tileId)
        return when {
            tileInfo == null || tileInfo.isTile -> staticTiles.getOrPut(tileId) { createTile(tileId, tileInfo) }
            else -> {
                val model = graphicsInfo.getTileInfo(tileId).modelInfo.model
                entityFactory.createEntity(tileInfo.entityInfo, pos!!, tileId, model)?.let {
                    GeneratedObject(GeneratedType.ENTITY, it)
                }
            }
        }
    }

    /** Creates new object, model can be without linked texture     */
    private fun createTile(tileId: TileId, tileInfo_: TileInfo?): GeneratedObject {
        val tileInfo = tileInfo_ ?: run {
            val model = SimpleModel(tileId)
            TileInfo(TileType.GHOST, null, ModelInfo(model))
        }

        val modelInfo = tileInfo.modelInfo
        val type = tileInfo.tileType
        val tile: Tile = when {
            modelInfo.isAnimated -> {
                val tileIds = (modelInfo.model as AnimatedModel).tileIds
                var frame = tileIds.indexOf(tileId)
                if (frame < 0)
                    frame = 0
                AnimatedTile(type, modelInfo.model, frame.toDouble())
            }
            else -> Tile(type, modelInfo.model)
        }
        return GeneratedObject(getTileDepth(tileId.tileId, type), tile)
    }

    private fun getTileDepth(tileId: Int, type: TileType): GeneratedType = when {
        FRONT_TYPES.contains(type) -> GeneratedType.FOREGROUND
        else -> GeneratedType.BACKGROUND
    }

    private fun generatePos(p: Vector2I?): Vector2D? =
            p?.let {Vector2D(p.x * Constants.TILE_SIZE.x, p.y * Constants.TILE_SIZE.y) }

    companion object {
        /** Set of tile types that are drawn over entities  */
        @JvmField
        val FRONT_TYPES: Set<TileType> = hashSetOf(TileType.GHOST_FRONT, TileType.SPIKES, TileType.WALL, TileType.WATER)
    }
}