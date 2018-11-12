package secretAgent.io.campaign.model

import org.slf4j.LoggerFactory
import secretAgent.io.tiles.ModelProperties
import secretAgent.view.model.LaserModel
import secretAgent.view.renderer.TileId
import java.awt.Color

/**
 * Creates laser model, same as animated model.
 *
 * @author Ondrej Milenovsky
 */
class LaserModelCreator : AnimatedModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): LaserModel {
        val model = super.createModel(modelProperties, tileId, modelName)!!
        val list = modelProperties.properties[COLOR_PROPERTY]
        var color = Color.WHITE
        if (list != null && list.isNotEmpty()) {
            if (list.size > 1)
                logger.warn("Laser model '$modelName' should define single color: $tileId")
            color = Color(list[0].tileId)
        } else {
            logger.warn("Laser model '$modelName' does not define color: $tileId")
        }
        return LaserModel(model.tileIds, model.durationMs, color)
    }

    companion object {
        private const val serialVersionUID = -6371384148477865812L

        private val logger = LoggerFactory.getLogger(LaserModelCreator::class.java)

        private const val COLOR_PROPERTY = "color"
    }
}