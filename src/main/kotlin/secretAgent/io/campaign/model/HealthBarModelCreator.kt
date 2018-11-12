package secretAgent.io.campaign.model

import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.slf4j.LoggerFactory
import secretAgent.io.tiles.ModelProperties
import secretAgent.view.model.HealthBarModel
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel

/**
 * Creates simple model.
 *
 * @author Ondrej Milenovsky
 */
class HealthBarModelCreator : AbstractModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): HealthBarModel? {
        val properties = modelProperties.properties
        val frame = getTileId(properties, modelName, FRAME_PROPERTY)
        val health = getTileId(properties, modelName, HEALTH_PROPERTY)
        return when {
            frame != null && health != null -> HealthBarModel(frame, health, 1.0)
            else -> null
        }
    }

    private fun getTileId(properties: Map<String, List<TileId>>, modelName: String, propertyName: String): TileId? {
        return when (val list = properties[propertyName]) {
            null -> {
                logger.warn("Health bar model '$modelName' requires property '$FRAME_PROPERTY'")
                null

            }
            else -> {
                if (list.size != 1)
                    logger.warn("Health bar model '$modelName' defines property $FRAME_PROPERTY, required number of tiles is 1, actual: ${list.size}")
                list[0]
            }
        }
    }

    override fun createModelInfo(tileId: TileId, model: GLModel, modelName: String) = ModelInfo(model)

    companion object {
        private const val serialVersionUID = 6809068456150092998L

        private val logger = LoggerFactory.getLogger(HealthBarModelCreator::class.java)

        private const val FRAME_PROPERTY = "frame"
        private const val HEALTH_PROPERTY = "health"
    }
}