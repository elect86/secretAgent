package secretAgent.io.campaign.model

import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.slf4j.LoggerFactory
import secretAgent.io.tiles.ModelProperties
import secretAgent.view.model.AnimatedModel
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel

/**
 * Creates animated model.
 *
 * @author Ondrej Milenovsky
 */
open class AnimatedModelCreator : AbstractModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): AnimatedModel? {
        // frames
        val tileIds = modelProperties.properties[FRAMES_PROPERTY] ?: run {
            logger.warn("Animated model '$modelName' must have property: $FRAMES_PROPERTY")
            return null
        }
        // duration
        var durationMs = DEFAULT_DURATION_MS
        getSingleTileId(modelProperties.properties, DURATION_PROPERTY, modelName)?.let { durTile ->
            durationMs = durTile.tileId.toLong()
        }
        // scale
        var scale = 0.0
        getSingleTileId(modelProperties.properties, SCALE_PROPERTY, modelName)?.let { scaleTile ->
            scale = tileToDouble(scaleTile)
        }
        return AnimatedModel(tileIds, scale, durationMs)
    }

    override fun createModelInfo(tileId: TileId, model: GLModel, modelName: String): ModelInfo {
        val tileIds = (model as AnimatedModel).tileIds
        var offset = tileIds.indexOf(tileId)
        if (offset < 0)
            offset = 0
        return ModelInfo(offset, model)
    }

    companion object {
        private const val serialVersionUID = -5648071321917849247L

        private val logger = LoggerFactory.getLogger(AnimatedModelCreator::class.java)

        private const val DEFAULT_DURATION_MS: Long = 1000

        private const val FRAMES_PROPERTY = "frames"
        private const val DURATION_PROPERTY = "duration"
    }
}
