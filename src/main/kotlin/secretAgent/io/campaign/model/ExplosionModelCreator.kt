package secretAgent.io.campaign.model

import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.slf4j.LoggerFactory
import secretAgent.io.tiles.ModelProperties
import secretAgent.view.model.ExplosionModel
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel

/**
 * Creates explosion model.
 *
 * @author Ondrej Milenovsky
 */
class ExplosionModelCreator : AbstractModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): ExplosionModel? {
        // frames
        val tileIds = modelProperties.properties[FRAMES_PROPERTY] ?: run {
            logger.warn("Explosion model '$modelName' must have property: $FRAMES_PROPERTY")
            return null
        }
        return ExplosionModel(tileIds)
    }

    override fun createModelInfo(tileId: TileId, model: GLModel, modelName: String) = ModelInfo(0, model)

    companion object {
        private const val serialVersionUID = 4746311062870890398L

        private val logger = LoggerFactory.getLogger(ExplosionModelCreator::class.java)

        private const val FRAMES_PROPERTY = "frames"
    }
}