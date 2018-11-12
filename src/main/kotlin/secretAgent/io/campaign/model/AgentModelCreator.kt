package secretAgent.io.campaign.model

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties
import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.view.model.AgentModel
import secretAgent.view.model.AgentTextures
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel

/**
 * Creates AgentModel.
 *
 * @author Ondrej Milenovsky
 */
class AgentModelCreator : AbstractModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): AgentModel {
        val properties = modelProperties.properties

        // stay duration
        var stayDuration = DEFAULT_DURATION_MS
        val durationList = properties[STAY_DURATION]
        if (durationList != null) {
            if (durationList.size != 1)
                logger.warn("Agent model '$modelName'.$STAY_DURATION must have 1 value, but has ${durationList.size}")
            if (durationList.isNotEmpty())
                stayDuration = durationList[0].tileId.toLong()
        }

        // textures
        val stayLeft = properties[STAY_LEFT]
        val stayRight = properties[STAY_RIGHT]
        val moveLeft = properties[MOVE_LEFT]
        val moveRight = properties[MOVE_RIGHT]
        val jumpLeft = properties[JUMP_LEFT]
        val jumpRight = properties[JUMP_RIGHT]
        val deathLeft = properties[DEATH_LEFT]
        val deathRight = properties[DEATH_RIGHT]

        val textures = createTextures(stayLeft, stayRight, moveLeft, moveRight, jumpLeft,
                jumpRight, deathLeft, deathRight, modelName)

        // TODO weapon center
        // scale
        var scale = 0.0
        val scaleTile = getSingleTileId(properties, SCALE_PROPERTY, modelName)
        if (scaleTile != null)
            scale = tileToDouble(scaleTile)
        return AgentModel(textures!![0], textures[1], scale, stayDuration, DEFAULT_WEAPON_CENTER)
    }

    private fun createTextures(stayLeft: List<TileId>?, stayRight: List<TileId>?,
                               moveLeft: List<TileId>?, moveRight: List<TileId>?, jumpLeft: List<TileId>?, jumpRight: List<TileId>?,
                               deathLeft: List<TileId>?, deathRight: List<TileId>?, modelName: String): Array<AgentTextures?>? {

        val ret = arrayOfNulls<AgentTextures>(2)
        val missingLeft = stayLeft == null || moveLeft == null || jumpLeft == null || deathLeft == null
        val missingRight = stayRight == null || moveRight == null || jumpRight == null || deathRight == null

        if (missingLeft && missingRight) {
            logger.warn("Agent model '$modelName' does not have complete at least one direction of textures.")
            return null
        }

        // create left
        if (!missingLeft) {
            ret[0] = AgentTextures(stayLeft!!, moveLeft!!, jumpLeft!!, deathLeft!!)
            if (missingRight && (stayRight != null || moveRight != null || jumpRight != null || deathRight != null))
                logger.warn("Agent model '$modelName' has incomplete right direction, ignoring all defined right textures")
        }
        //create right
        if (!missingRight) {
            ret[1] = AgentTextures(stayRight!!, moveRight!!, jumpRight!!, deathRight!!)
            if (missingLeft && (stayLeft != null || moveLeft != null || jumpLeft != null || deathLeft != null))
                logger.warn("Agent model '$modelName' has incomplete left direction, ignoring all defined left textures")
        }

        return ret
    }

    override fun createModelInfo(tileId: TileId, model: GLModel, modelName: String) = ModelInfo(model)

    companion object {

        private val serialVersionUID = -7333931381388152107L

        private val logger = LoggerFactory.getLogger(AgentModelCreator::class.java)

        private val DEFAULT_WEAPON_CENTER = Vector2D(0.0, 2.5)
        private const val DEFAULT_DURATION_MS: Long = 1000

        private const val STAY_DURATION = "stayDuration"
        private const val STAY_LEFT = "stayLeft"
        private const val STAY_RIGHT = "stayRight"
        private const val MOVE_LEFT = "moveLeft"
        private const val MOVE_RIGHT = "moveRight"
        private const val JUMP_LEFT = "jumpLeft"
        private const val JUMP_RIGHT = "jumpRight"
        private const val DEATH_LEFT = "deathLeft"
        private const val DEATH_RIGHT = "deathRight"
    }
}