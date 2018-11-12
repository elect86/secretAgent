package secretAgent.io.campaign.model

import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.io.tiles.ModelProperties
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel
import java.io.Serializable

/**
 * Creates model from parsed information.
 *
 * @author Ondrej Milenovsky
 */
interface GLModelCreator : Serializable {

    /**
     * Creates model from properties and tileId.
     * @param modelProperties model properties
     * @param tileId tile id from which is the model created
     * @param modelName model name used for error messages
     * @return new model or null
     */
    fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): GLModel?

    /**
     * Creates model info from model and tileId
     * @param tileId tile id from which is the model created
     * @param model model to use
     * @param modelName model name used for error messages
     * @return new model info or null
     */
    fun createModelInfo(tileId: TileId, model: GLModel, modelName: String): ModelInfo?
}

/**
 * Abstract model creator with util methods.
 *
 * @author Ondrej Milenovsky
 */
abstract class AbstractModelCreator : GLModelCreator {

    companion object {
        private const val serialVersionUID = 4249585509120419992L

        private val logger = LoggerFactory.getLogger(AbstractModelCreator::class.java)

        const val SCALE_PROPERTY = "scale"

        /** Converts tile id to double         */
        fun tileToDouble(tileId: TileId) = java.lang.Double.parseDouble("${tileId.tileSetId.toString()}.${tileId.tileId}")

        /**
         * If the properties contains the property with at least 2 values, creates vector.
         * If the property has different number of values, prints warnings.
         * If the vector cannot be parsed, returns zero vector.
         */
        fun getVector(properties: Map<String, List<TileId>>, property: String,
                                modelName: String): Vector2D {
            var ret = Vector2D.ZERO
            properties[property]?.let { list ->
                if (list.size != 2)
                    logger.warn("Weapon model'$modelName'.$property must have 2 value, but has ${list.size}")
                if (list.size >= 2)
                    ret = Vector2D(tileToDouble(list[0]), tileToDouble(list[1]))
            }
            return ret
        }

        /** Extracts first tile from the property list, logs warnings, returns null if no value         */
        fun getSingleTileId(properties: Map<String, List<TileId>>, property: String,
                                      modelName: String): TileId? {
            properties[property]?.let { list ->
                if (list.size != 1)
                    logger.warn("Weapon model'$modelName'.$property must have 1 value, but has ${list.size}")
                if (list.isNotEmpty())
                    return list[0]
            }
            return null
        }
    }
}