package secretAgent.io

import cz.wa.secretagent.simulation.GameRunner
import cz.wa.secretagent.world.weapon.Weapon
import cz.wa.secretagent.worldinfo.CampaignInfo
import cz.wa.secretagent.worldinfo.WorldHolder
import cz.wa.wautils.math.Vector2I
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import secretAgent.game.GameSettings
import secretAgent.io.campaign.CampaignLoader
import secretAgent.io.campaign.model.ModelFactory
import secretAgent.io.map.orig.MapLoader
import secretAgent.io.map.orig.generator.entity.EntityFactory
import secretAgent.view.SamGraphics
import secretAgent.view.Settings2D
import secretAgent.world.SamWorld
import java.io.File
import java.io.IOException
import java.io.Serializable

/**
 * Loads maps, saved games, graphics, ...
 * Saves game.
 *
 * Does not start the game. When anything was loaded, it must be further processed.
 *
 * @author Ondrej Milenovsky
 */
class SamIO : Serializable {

    lateinit var worldHolder: WorldHolder
    lateinit var graphics: SamGraphics
    lateinit var gameRunner: GameRunner
    lateinit var settings2d: Settings2D
    lateinit var fileSettings: FileSettings
    lateinit var gameSettings: GameSettings
    lateinit var modelFactory: ModelFactory
    lateinit var entityFactory: EntityFactory
    lateinit var allWeapons: Map<String, Weapon>

    @Transient
    lateinit var campaignInfo: CampaignInfo
        private set

    /**
     * Loads campaign from file.
     * @param file
     * @throws IOException
     */
    @Throws(IOException::class)
    fun loadCampaign(file: File) {
        campaignInfo = CampaignLoader(file, graphics, settings2d, fileSettings, modelFactory).loadCampaign()
        worldHolder.graphicsInfo.update(campaignInfo.graphicsInfo)
        campaignInfo.clearGraphicsInfo()
        for (weapon in allWeapons.values) {
            weapon.linkModel(worldHolder.graphicsInfo)
            if (!weapon.hasLinkedModel())
                logger.warn("Missing model '${weapon.modelName}' for weapon: ${weapon.name}")
        }
        // check that every additional model has linked textures
        for ((key, value) in worldHolder.graphicsInfo.models)
            if (!value.hasLinkedTextures())
                logger.warn("Missing textures for model: $key")
    }

    /**
     * Loads default graphics settings
     * @throws IOException
     */
    @Throws(IOException::class)
    fun loadDefaultGraphics() {
        val file = File(fileSettings.dataDir + fileSettings.defaultGraphicsFile)
        val gri = GraphicsFilesLoader(file, modelFactory, graphics, fileSettings, settings2d).load()
        worldHolder.graphicsInfo = gri
    }

    /**
     * Loads player's saved progress from file.
     * @param file
     * @throws IOException
     */
    @Throws(IOException::class)
    fun loadPlayer(file: File) {
        // TODO load player
    }

    /**
     * Saves player's progress to file.
     * @param file
     * @throws IOException
     */
    @Throws(IOException::class)
    fun savePlayer(file: File) {
        // TODO save player
    }

    /**
     * Ends the game
     */
    fun clearMap() {
        worldHolder.world = createEmptyWorld()
    }

    private fun createEmptyWorld() = SamWorld(Vector2I.ZERO, null, -1)

    /**
     * Loads single map by level id.
     * @param levelId
     * @throws IOException
     */
    @Throws(IOException::class)
    fun loadLevelMap(levelId: Int) {
        val levels = campaignInfo.mapInfo.levelMapFiles
        assert(levelId >= 1 && levelId <= levels.size) { "Level id must be 1..${levels.size}, but is $levelId" }
        loadLevel(levels[levelId - 1], levelId)
    }

    /**
     * Loads island map.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun loadIslandMap() = loadLevel(campaignInfo.mapInfo.islandMapFile, 0)

    @Throws(IOException::class)
    private fun loadLevel(file: File, levelId: Int) {
        val world: SamWorld
        val fileExt = FilenameUtils.getExtension(file.name)
        if (fileExt.equals(fileSettings.mapOrigExt, ignoreCase = true))
            world = MapLoader(file, levelId, worldHolder.graphicsInfo, graphics, entityFactory).loadMap()
        else
            throw UnsupportedOperationException("TODO load different map type")
        worldHolder.world = world
    }

    fun dispose() {
        graphics.dispose()
//        graphics = null
//        campaignInfo = null
//        worldHolder = null
//        settings2d = null
    }

    companion object {
        private const val serialVersionUID = -912252983049381017L
        private val logger = LoggerFactory.getLogger(SamIO::class.java)
    }
}