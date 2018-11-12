package secretAgent.io.tiles

import cz.wa.secretagent.utils.lwjgl.TextureLoader
import cz.wa.wautils.math.Vector2I
import org.slf4j.LoggerFactory
import secretAgent.io.campaign.model.ModelFactory
import secretAgent.view.renderer.GLTileSet
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import java.io.File
import java.io.IOException
import java.util.HashMap

/**
 * Loads image with multiple tiles.
 *
 * @author Ondrej Milenovsky
 */
class GLTileSetLoader(private val file: File) {

    @Throws(IOException::class)
    fun load(tileSize: Vector2I, texFilter: Boolean): GLTileSet {
        logger.info("Loading texture: " + file.absolutePath)
        val texture = TextureLoader.loadTexture(file, texFilter)
        return GLTileSet(texture, tileSize.x, tileSize.y)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GLTileSetLoader::class.java)
    }
}

/**
 * Loads models from file.
 *
 * @author Ondrej Milenovsky
 */
class ModelsLoader(private val file: File, private val modelFactory: ModelFactory) {

    @Throws(IOException::class)
    fun loadModels(): Map<String, ObjectModel> {
        val tilesPr = TilesPropertiesParser(file, 0).parse(true)
        return createModels(tilesPr)
    }

    private fun createModels(tilesPr: TilesProperties): Map<String, ObjectModel> {
        val ret = HashMap<String, ObjectModel>(tilesPr.models.size)
        for ((name, value) in tilesPr.models) {
            val modelInfo = modelFactory.getModel(value, TileId(0, 0), name)
            if (modelInfo != null) {
                ret[name] = modelInfo.model
            } else {
                logger.warn("Failed to create model '" + name + "': " + file.absolutePath)
            }
        }
        return ret
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ModelsLoader::class.java)
    }
}
