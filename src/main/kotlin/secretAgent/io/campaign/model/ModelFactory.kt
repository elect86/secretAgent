package secretAgent.io.campaign.model

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties
import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import org.slf4j.LoggerFactory
import secretAgent.view.model.SimpleModel
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel
import secretAgent.world.ModelType
import secretAgent.world.ObjectModel
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.HashMap

/**
 * Creates model infos that are immutable singletons.
 *
 * @author Ondrej Milenovsky
 */
class ModelFactory : Serializable {

    lateinit var modelCreators: Map<ModelType, GLModelCreator>

    @Transient
    private var infoCache: MutableMap<ModelInfo, ModelInfo> = HashMap()
    @Transient
    private var modelCache: MutableMap<GLModel, GLModel> = HashMap()

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(stream: ObjectInputStream) {
        stream.defaultReadObject()
        infoCache = HashMap()
        modelCache = HashMap()
    }

    fun getModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): ModelInfo? {
        val type = modelProperties.type
        val creator = modelCreators[type]
        if (creator != null) {
            val model = creator.createModel(modelProperties, tileId, modelName) ?: return null
            val info = creator.createModelInfo(tileId, getCachedModel(model), modelName) ?: return null
            return getCachedInfo(info)
        } else {
            logger.warn("No model creator for type: $type")
            return null
        }
    }

    fun getSimpleModel(tileId: TileId): ModelInfo? = getModel(ModelProperties("", ModelType.SIMPLE), tileId, "")

    private fun getCachedInfo(info: ModelInfo): ModelInfo = infoCache.getOrPut(info) { info }

    fun createSimpleModel(tileId: TileId): ObjectModel {
        val model = SimpleModel(tileId)
        return getCachedModel(model)
    }

    private fun getCachedModel(model: GLModel): GLModel = modelCache.getOrPut(model) { model }

    companion object {
        private const val serialVersionUID = -4885331968864703472L

        private val logger = LoggerFactory.getLogger(ModelFactory::class.java)
    }
}