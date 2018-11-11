package secretAgent.io.campaign

import cz.wa.secretagent.io.FileSettings
import org.slf4j.LoggerFactory
import secretAgent.io.GraphicsFilesParser
import java.awt.Color
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * Parsed campaign containing title and file information.
 *
 * @author Ondrej Milenovsky
 */
class CampaignProperties(val title: String,
                         val description: String,
                         /** background color, can be null                          */
                         val bgColor: Color?,
                         val levelTilesFile: File?,
                         val addedTileFiles: Map<Int, File>,
                         val modelsFiles: List<File>,
                         val islandMapFile: File?,
                         val levelMapFiles: List<File?>)

/**
 * Parses campaign info from file.
 *
 * @author Ondrej Milenovsky
 */
class CampaignPropertiesParser(private val file: File, private val fileSettings: FileSettings) {

    @Throws(IOException::class)
    fun parse(): CampaignProperties {
        // read from file
        val p = Properties()
        p.load(FileReader(file))

        // parse
        val title = p.getProperty(TITLE_KEY).trim()
        val description = p.getProperty(DESCRIPTION_KEY).trim()
        val bgColor = GraphicsFilesParser.getColor(p, BG_COLOR_KEY)
        val levelTilesFile = getFile(p, LEVEL_TILES_KEY)
        val addedTileFiles = GraphicsFilesParser.getFileMap(p, ADDED_TILES_KEY, fileSettings.dataDir)
        val modelsFiles = GraphicsFilesParser.getFileList(p, MODELS_KEY, fileSettings.dataDir)
        val islandMapFile = getFile(p, ISLAND_MAP_KEY)
        val levelMapFiles = GraphicsFilesParser.getFileMap(p, LEVEL_MAPS_KEY, fileSettings.dataDir)

        val levelListFiles = convertToList(levelMapFiles!!)

        // return
        return CampaignProperties(title, description, bgColor, levelTilesFile, addedTileFiles!!, modelsFiles!!, islandMapFile, levelListFiles)
    }

    private fun convertToList(levelMapFiles: Map<Int, File>): List<File?> {
        // find max value
        val max = levelMapFiles.keys.max() ?: 0

        // create list of nulls
        val ret = MutableList<File?>(max) {null}
        // fill the list
        for ((key, value) in levelMapFiles) {
            val i = key - 1
            if (ret[i] != null)
                logger.warn("Duplicate level file: $i")
            ret[i] = value
        }
        //validate
        for (i in 0 until max) {
            if (ret[i] == null)
                logger.warn("Missing level file: ${i + 1}")
        }
        return ret
    }

    private fun getFile(p: Properties, key: String): File? {
        val fileName = p.getProperty(key)?.trim() ?: run {
            logger.warn("Missing $key")
            return null
        }
        return File(fileSettings.dataDir + fileName)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CampaignPropertiesParser::class.java)

        private const val TITLE_KEY = "title"
        private const val DESCRIPTION_KEY = "description"
        private const val BG_COLOR_KEY = "bgColor"
        private const val LEVEL_TILES_KEY = "levelTilesFile"
        private const val ADDED_TILES_KEY = "addedTileFiles"
        private const val MODELS_KEY = "modelsFiles"
        private const val ISLAND_MAP_KEY = "islandMapFile"
        private const val LEVEL_MAPS_KEY = "levelMapFiles"
    }

}