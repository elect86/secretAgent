package secretAgent.io.campaign.model

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties
import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.slf4j.LoggerFactory
import secretAgent.view.model.AnimatedModel
import secretAgent.view.model.WeaponModel
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel

/**
 * Creates weapon model.
 *
 * @author Ondrej Milenovsky
 */
class WeaponModelCreator : AbstractModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): WeaponModel? {
        // wield frames
        val wieldTileIds = modelProperties.properties[WIELD_FRAMES_PROPERTY] ?: run {
            logger.warn("Weapon model '$modelName' must have property: $WIELD_FRAMES_PROPERTY")
            return null
        }
        // fire frames
        val fireTileIds = modelProperties.properties[FIRE_FRAMES_PROPERTY] ?: run {
            logger.warn("Weapon model '$modelName' must have property: $FIRE_FRAMES_PROPERTY")
            return null
        }
        // duration
        val duration = getSingleTileId(modelProperties.properties, WIELD_DURATION_PROPERTY, modelName)
        val durationMs = duration?.tileId?.toLong() ?: DEFAULT_DURATION_MS
        // wield center
        val wieldCenter = getVector(modelProperties.properties, WIELD_CENTER_PROPERTY, modelName)
        // fire center
        val fireCenter = getVector(modelProperties.properties, FIRE_CENTER_PROPERTY, modelName)
        // scale
        val sTile = getSingleTileId(modelProperties.properties, SCALE_PROPERTY, modelName)
        val scale = sTile?.let(::tileToDouble) ?: DEFAULT_SCALE
        // the fire model does not need duration (duration = reload time)
        val fireModel = AnimatedModel(fireTileIds, scale, durationMs)
        val wieldModel = AnimatedModel(wieldTileIds, scale, durationMs)
        return WeaponModel(wieldModel, wieldCenter, fireModel, fireCenter, scale)
    }

    override fun createModelInfo(tileId: TileId, model: GLModel, modelName: String) = ModelInfo(model)

    companion object {
        private const val serialVersionUID = 8116144786714680816L

        private val logger = LoggerFactory.getLogger(WeaponModelCreator::class.java)

        private const val DEFAULT_DURATION_MS: Long = 1000
        private const val DEFAULT_SCALE = 1.0

        private const val WIELD_FRAMES_PROPERTY = "wieldFrames"
        private const val WIELD_DURATION_PROPERTY = "wieldDuration"
        private const val WIELD_CENTER_PROPERTY = "wieldCenter"
        private const val FIRE_FRAMES_PROPERTY = "fireFrames"
        private const val FIRE_CENTER_PROPERTY = "fireCenter"
    }
}