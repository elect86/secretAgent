package secretAgent.game.starter

import org.slf4j.LoggerFactory
import secretAgent.game.PlayerHolder
import secretAgent.game.player.PlayerStats
import secretAgent.io.SamIO
import java.io.File
import java.io.IOException
import java.io.Serializable

/**
 * Starts campaign, generates all required classes and loads files.
 * This class is called by main menu when selecting new game.
 *
 * @author Ondrej Milenovsky
 */
class CampaignStarter : Serializable {

    lateinit var samIO: SamIO
    lateinit var playerHolder: PlayerHolder
    lateinit var mapStarter: MapStarter
    lateinit var initialStats: PlayerStats

    fun startCampaign(campaignFile: File): Boolean {
        return try {
            samIO.loadCampaign(campaignFile)
            playerHolder.playerStats = initialStats.deepCopy()
            mapStarter.startIslandMap()
        } catch (e: IOException) {
            logger.error("Cannot start campaign " + campaignFile.absolutePath, e)
            false
        }

    }

    companion object {
        private const val serialVersionUID = -9220101832719429081L
        private val logger = LoggerFactory.getLogger(CampaignStarter::class.java)
    }
}