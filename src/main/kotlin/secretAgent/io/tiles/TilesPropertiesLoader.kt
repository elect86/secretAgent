package secretAgent.io.tiles

import cz.wa.secretagent.worldinfo.graphics.EntityInfo
import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import cz.wa.secretagent.worldinfo.graphics.TileInfo
import cz.wa.secretagent.worldinfo.graphics.TilesInfo
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import secretAgent.io.campaign.model.ModelFactory
import secretAgent.view.renderer.TileId
import java.io.File
import java.io.IOException

/**
 * Loads tiles properties and generates TilesInfo
 *
 * @author Ondrej Milenovsky
 */
class TilesPropertiesLoader(private val file: File, private val tileSetId: Int, private val modelFactory: ModelFactory) {

    @Throws(IOException::class)
    fun loadInfo(): TilesInfo {
        logger.info("Loading tiles definition: " + file.absolutePath)

        val tilesPr = TilesPropertiesParser(file, tileSetId).parse(false)
        return createTilesInfo(tilesPr)
    }

    /**
     * Creates single tile set info from parsed tile properties.
     */
    private fun createTilesInfo(tilesPr: TilesProperties): TilesInfo {
        val tiles = HashMap<Int, TileInfo>(tilesPr.tiles.size)
        for (entry in tilesPr.tiles.entries) {
            val tileId = entry.key
            val tileInfo = createTileInfo(tileId, entry.value, tilesPr.models)
            tiles[tileId.tileId] = tileInfo
        }
        return TilesInfo(tilesPr.tileSize, tiles)
    }

    /**
     * Creates tile info for single tile.
     */
    private fun createTileInfo(tileId: TileId, tilePr: TileProperties, models: Map<String, ModelProperties>): TileInfo {
        var model: ModelInfo? = null
        val modelRef = tilePr.modelRef
        val hasModelRef = !StringUtils.isEmpty(modelRef)
        if (hasModelRef) {
            if (models.containsKey(modelRef))
                model = modelFactory.getModel(models[modelRef]!!, tileId, modelRef!!)
            else
                logger.warn("No model definition: '$modelRef' for tile: $tileId")
        }
        if (model == null) {
            if (hasModelRef)
                logger.warn("Failed to create model '$modelRef' for tile $tileId, creating simple model")
            model = modelFactory.getSimpleModel(tileId)
        }
        return TileInfo(tilePr.tileType, createEntityInfo(tilePr.entityProperties), model)
    }

    private fun createEntityInfo(entityProperties: EntityProperties?): EntityInfo? =
            entityProperties?.let(::EntityInfo)

    companion object {
        private val logger = LoggerFactory.getLogger(TilesPropertiesLoader::class.java)
    }
}