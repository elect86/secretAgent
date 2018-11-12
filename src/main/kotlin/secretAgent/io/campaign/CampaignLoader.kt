package secretAgent.io.campaign

import cz.wa.secretagent.worldinfo.CampaignInfo
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.secretagent.worldinfo.map.MapInfo
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import secretAgent.io.FileSettings
import secretAgent.io.GraphicsFilesLoader
import secretAgent.io.campaign.model.ModelFactory
import secretAgent.view.SamGraphics
import secretAgent.view.Settings2D
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Parses campaign definition file, graphics definitions and loads all textures.
 * Then processes the parsed structures and generates CampaignInfo.
 *
 * @author Ondrej Milenovsky
 */
class CampaignLoader(
        /** file containing campaign definition         */
        private val file: File,
        /** reference to graphics, used to load textures, if null, then textures are not loaded or linked with models */
        private val graphics: SamGraphics,
        private val settings2d: Settings2D,
        private val fileSettings: FileSettings,
        private val modelFactory: ModelFactory) {

    /**
     * Parses the campaign file and all subfiles (except maps), loads the textures and generates CampaignInfo.
     * @return parsed and processed CampaignInfo
     * @throws IOException
     * @throws RuntimeException when there is fatal error in file contents
     */
    @Throws(IOException::class)
    fun loadCampaign(): CampaignInfo {
        // general info
        logger.info("Loading campaign: " + file.absolutePath)
        val cp = CampaignPropertiesParser(file, fileSettings).parse()
        validate(cp)

        // tiles
        val tilesFiles = HashMap(cp.addedTileFiles)
        tilesFiles[GraphicsInfo.ORIG_LEVEL_TILES_ID] = cp.levelTilesFile

        val gri = GraphicsFilesLoader(file, modelFactory, graphics, fileSettings, settings2d)
                .load(tilesFiles, cp.modelsFiles).apply { bgColor = cp.bgColor  }
        return CampaignInfo(cp.title, MapInfo(cp.islandMapFile, cp.levelMapFiles), gri)
    }

    private fun validate(cp: CampaignProperties) {
        if (StringUtils.isEmpty(cp.title))
            logger.warn("Missing campaign title in: ${file.absolutePath}")
        assert(cp.levelTilesFile != null) {"Missing level tiles file"}
        assert(cp.islandMapFile != null) {"Missing island map file"}
        assert(cp.levelMapFiles != null) {"Missing level map files"}
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CampaignLoader::class.java)
    }
}