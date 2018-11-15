package secretAgent.io.tiles

import cz.wa.wautils.math.Vector2I
import secretAgent.view.renderer.TileId
import secretAgent.world.ModelType
import secretAgent.world.TileType
import java.util.*

/**
 * Info about single entity, that is created from a tile.
 *
 * @author Ondrej Milenovsky
 */
open class EntityProperties(args: List<String>) {
    val args: List<String> = Collections.unmodifiableList(args)
}

/**
 * Parsed properties for model.
 *
 * @author Ondrej Milenovsky
 */
class ModelProperties(val name: String, val type: ModelType?) {
    /** property name -> list of tile ids  */
    val properties: MutableMap<String, List<TileId>> = HashMap()
}

/**
 * Info about single tile. Tile can be static tile (part of map) or entity, always is one null of tileType and entityInfo.
 * If no model is specified, then it is simple model.
 *
 * @author Ondrej Milenovsky
 */
class TileProperties {
    val tileType: TileType?
    val entityProperties: EntityProperties?
    val modelRef: String?

    val isTile: Boolean
        get() = tileType != null

    constructor(tileType: TileType, modelRef: String?) {
        assert(tileType != TileType.ENTITY) {"tileType must not be ENTITY"}
        this.tileType = tileType
        this.entityProperties = null
        this.modelRef = modelRef
    }

    constructor(entityPr: EntityProperties, modelRef: String?) {
        this.tileType = null
        this.entityProperties = entityPr
        this.modelRef = modelRef
    }
}

/**
 * Parsed tiles info.
 *
 * @author Ondrej Milenovsky
 */
class TilesProperties(val tileSize: Vector2I?,
                      val tiles: Map<TileId, TileProperties>,
                      val models: Map<String, ModelProperties>)