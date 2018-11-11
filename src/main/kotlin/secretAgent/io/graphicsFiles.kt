package secretAgent.io

import cz.wa.secretagent.io.FileSettings
import cz.wa.secretagent.io.tiles.GLTileSetLoader
import cz.wa.secretagent.io.tiles.ModelsLoader
import cz.wa.secretagent.io.tiles.TilesPropertiesLoader
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.secretagent.worldinfo.graphics.TilesInfo
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.Validate
import org.slf4j.LoggerFactory
import secretAgent.io.campaign.model.ModelFactory
import secretAgent.view.SAMGraphics
import secretAgent.view.Settings2D
import secretAgent.view.renderer.GLGraphics
import secretAgent.world.ObjectModel
import java.awt.Color
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * Files defining graphics.
 *
 * @author Ondrej Milenovsky
 */
class GraphicsFiles(val bgColor: Color,  val tilesFiles: Map<Int, File>, val modelsFiles: List<File>)

/**
 * Parses graphics files definition and creates graphics info.
 *
 * @author Ondrej Milenovsky
 */
class GraphicsFilesLoader(private val file: File,
                          private val modelFactory: ModelFactory,
                          private val graphics: SAMGraphics?,
                          private val fileSettings: FileSettings,
                          private val settings2d: Settings2D) {

    @Throws(IOException::class)
    fun load(): GraphicsInfo {
        logger.info("Loading default graphics: " + file.absolutePath)
        // parse the properties file
        val gf = GraphicsFilesParser(file, fileSettings).parse()
        // load all other files
        return load(gf.tilesFiles, gf.modelsFiles).apply {
            bgColor = gf.bgColor
        }
    }

    fun load(tilesFiles: Map<Int, File>, modelsFiles: List<File>): GraphicsInfo {
        // load all the files
        val gri = loadGraphicsInfo(tilesFiles, modelsFiles, modelFactory, fileSettings.tilesDefExt)
        // load and link textures
        graphics?.let {
            loadTextures(gri, tilesFiles)
            gri.linkTexturesToModels(it)
        }
        return gri
    }

    /**
     * Loads and uses all new textures.
     */
    private fun loadTextures(gri: GraphicsInfo, tileFiles: Map<Int, File>) {
        for ((key, texFile) in tileFiles) {
            val tileSize = gri.tileSets[key]!!.tileSize
            try {
                val tileSet = GLTileSetLoader(texFile).load(tileSize, settings2d.texFilter)
                if (graphics is GLGraphics)
                    graphics.setTileSet(key, tileSet)
                else
                    throw UnsupportedOperationException("Unknown graphics, refactoring needed")
            } catch (e: IOException) {
                logger.error("Cannot load textures: ${texFile.absolutePath}", e)
            }

        }
    }

    /**
     * Parses info for all tile sets and generates the GraphicsInfo. The input files are images.
     */
    private fun loadGraphicsInfo(tileFiles: Map<Int, File>, modelFiles: List<File>,
                                 modelFactory: ModelFactory, tilesDefExt: String): GraphicsInfo {
        // tile sets
        val tileSets = HashMap<Int, TilesInfo>(tileFiles.size)
        for ((key, value) in tileFiles) {
            try {
                val setInfo = loadTileSetInfo(key, value, modelFactory, tilesDefExt)
                tileSets[key] = setInfo
            } catch (e: IOException) {
                logger.error("Cannot load tiles info: " + value.absolutePath, e)
            }

        }
        // models
        val models = loadModels(modelFiles, modelFactory)
        return GraphicsInfo(tileSets, models)
    }

    /**
     * Loads all models from the files.
     */
    private fun loadModels(modelFiles: List<File>, modelFactory: ModelFactory): Map<String, ObjectModel> {
        val ret = HashMap<String, ObjectModel>()
        for (file in modelFiles)
            try {
                ret += ModelsLoader(file, modelFactory).loadModels()
            } catch (e: IOException) {
                logger.error("Cannot load models: ${file.absolutePath}", e)
            }
        return ret
    }

    @Throws(IOException::class)
    private fun loadTileSetInfo(tileSetId: Int, tilesFile: File, modelFactory: ModelFactory, tilesDefExt: String): TilesInfo {
        val infoFileName = "${FilenameUtils.removeExtension(tilesFile.path)}.$tilesDefExt"
        val infoFile = File(infoFileName)

        return TilesPropertiesLoader(infoFile, tileSetId, modelFactory).loadInfo()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GraphicsFilesLoader::class.java)
    }
}

/**
 * Parses campaign info from file.
 *
 * @author Ondrej Milenovsky
 */
class GraphicsFilesParser(protected val file: File, protected val fileSettings: FileSettings) {

    @Throws(IOException::class)
    fun parse(): GraphicsFiles {
        // read from file
        val p = Properties()
        p.load(FileReader(file))

        // parse
        val bgColor = getColor(p, BG_COLOR_KEY)
        val tilesFiles = getFileMap(p, ADDED_TILES_KEY, fileSettings.dataDir)
        val modelsFiles = getFileList(p, MODELS_KEY, fileSettings.dataDir)

        // return
        return GraphicsFiles(bgColor!!, tilesFiles!!, modelsFiles!!)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GraphicsFilesParser::class.java)

        private val BG_COLOR_KEY = "bgColor"
        private val ADDED_TILES_KEY = "tilesFiles"
        private val MODELS_KEY = "modelsFiles"

        fun getColor(p: Properties, key: String): Color? {
            val str = p.getProperty(key)?.trim() ?: return null
            return try {
                Color.decode(str)
            } catch (e: NumberFormatException) {
                logger.warn("Wrong color format: $str")
                Color.BLACK
            }

        }

        fun getFileList(p: Properties, key: String, dataDir: String): List<File>? {
            val str = p.getProperty(key)?.trim() ?: return null
            val ret = ArrayList<File>()
            for (s in str.split(";")) {
                val i = s.trim()
                if (s.isNotEmpty())
                    ret += File(dataDir + s)
            }
            return ret
        }

        fun getFileMap(p: Properties, key: String, dataDir: String): Map<Int, File>? {
            val str = p.getProperty(key)?.trim() ?: return null
            val ret = HashMap<Int, File>()
            for (s in str.split(";")) {
                val i = s.trim()
                if (i.isNotEmpty()) {
                    if ("*" !in i) {
                        logger.warn("Tile set $s does not have id, valid example: 6*tiles/image.png")
                        continue
                    }
                    val ind = i.indexOf('*')
                    val num = i.substring(0, ind)
                    try {
                        val id = Integer.parseInt(num)
                        if (id in ret)
                            logger.warn("Duplicate tile set id: $id")
                        ret[id] = File(dataDir + s.substring(ind + 1))
                    } catch (e: NumberFormatException) {
                        logger.error("Tile set $s has id, that is not a number")
                    }
                }
            }
            return ret
        }
    }
}