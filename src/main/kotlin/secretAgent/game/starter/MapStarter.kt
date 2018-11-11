package secretAgent.game.starter

import cz.wa.secretagent.game.PlayerHolder
import cz.wa.secretagent.io.SAMIO
import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.EntityType
import cz.wa.secretagent.world.entity.agent.HumanAgent
import cz.wa.secretagent.world.entity.usable.BuildingUsable
import cz.wa.secretagent.world.entity.usable.UsableType
import cz.wa.secretagent.world.map.StoredTile
import cz.wa.secretagent.world.map.Tile
import cz.wa.secretagent.world.map.TileType
import cz.wa.secretagent.world.weapon.WeaponOrder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Required
import secretAgent.game.ProjectileFactory
import secretAgent.game.SensorFactory
import secretAgent.game.action.ActionFactory
import secretAgent.game.controller.menuCreator.MainMenuCreator
import java.io.IOException
import java.io.Serializable
import java.util.ArrayList

/**
 * Starts single map.
 *
 * @author Ondrej Milenovsky
 */
class MapStarter : Serializable {

    lateinit var io: SAMIO
    lateinit var playerHolder: PlayerHolder
    lateinit var cameraInfo: CameraInfo
    lateinit var projectileFactory: ProjectileFactory
    lateinit var mainMenuCreator: MainMenuCreator
    lateinit var weaponOrder: WeaponOrder

    fun startMainMenu(): Boolean {
        io.clearMap()
        val menuHolder = io.worldHolder.menuHolder
        menuHolder.removeAllFrames()

        val mainMenu = mainMenuCreator.getMainMenu()
        mainMenu.selectedIndex = 0
        menuHolder.addFrame(mainMenu)
        return true
    }

    fun startIslandMap(): Boolean {
        val file = io.campaignInfo.mapInfo.islandMapFile
        try {
            // load map
            io.loadIslandMap()
            completeBuildings()
            // find player
            val agent = findFirstPlayerAgent()
            if (agent == null) {
                logger.warn("No start position in the map " + file.absolutePath)
                return false
            }
            // update position
            val stats = playerHolder.playerStats
            if (stats.islandPos == null)
                stats.islandPos = agent.pos
             else
                agent.pos = stats.islandPos
            // create and set game classes
            val worldHolder = io.worldHolder
            val world = worldHolder.world
            val camera = cameraInfo.createCamera(agent.pos)

            val sensorFactory = SensorFactory(agent, world)
            val actionFactory = ActionFactory(agent, world, sensorFactory, this, null)

            playerHolder.also {
                it.islandController.init(actionFactory)
                it.levelController.init(null, null)
                it.agent = agent
                it.camera = camera
            }
            world.isRunning = true
            return true
        } catch (e: IOException) {
            logger.error("Cannot start island map " + file.absolutePath, e)
            return false
        }

    }

    /**
     * Replace completed buildings by ghosts. Also removes fence when only final building remains.
     */
    private fun completeBuildings() {
        val entityMap = io.worldHolder.world.entityMap
        val finishedLevels = playerHolder.playerStats.finishedLevels
        val levelMap = io.worldHolder.world.levelMap
        val graphicsInfo = io.worldHolder.graphicsInfo
        var levelCount = 0
        var finishCounted = false

        // check all buildings
        for (entity in ArrayList(entityMap.getEntities(EntityType.USABLE))) {
            if (entity.secondType == UsableType.BUILDING) {
                val building = entity as BuildingUsable
                if (finishedLevels.contains(building.levelId)) {
                    // the entity is finished building, remove it
                    entityMap.removeEntity(building)
                    // create ghost tile
                    val model = graphicsInfo.getModel(building.finishedModel)
                    val pos = levelMap.getNearestTilePos(building.pos)
                    val tile = StoredTile(pos, Tile(TileType.GHOST, model))
                    // add the tile
                    levelMap.addTile(tile)
                    // update type to remove the wall
                    levelMap.updateType(pos)
                }
                // update count
                if (building.isFinalBuilding) {
                    if (!finishCounted) {
                        finishCounted = true
                        levelCount++
                    }
                } else
                    levelCount++
            }
        }

        // remove fences
        if (finishedLevels.size >= levelCount - 1)
            for (entity in ArrayList(entityMap.getEntities(EntityType.USABLE)))
                if (entity.secondType == UsableType.FENCE)
                    entityMap.removeEntity(entity)
    }

    fun startLevelMap(id: Int): Boolean {
        val levelMapFiles = io.campaignInfo.mapInfo.levelMapFiles
        if (id - 1 >= levelMapFiles.size) {
            logger.warn("Missing level $id in campaign definition")
            return false
        }

        val file = levelMapFiles[id - 1]
        // store position on map
        playerHolder.playerStats.islandPos = playerHolder.agent.pos
        try {
            // load map
            io.loadLevelMap(id)
            // find player
            val agent = findFirstPlayerAgent()
            if (agent == null) {
                logger.warn("No start position in the map " + file.absolutePath)
                return false
            }
            // create and set game classes
            val camera = cameraInfo.createCamera(agent.pos)
            val world = io.worldHolder.world

            val sensorFactory = SensorFactory(agent, world)
            val actionFactory = ActionFactory(agent, world, sensorFactory, this, projectileFactory)

            playerHolder.also {
                it.islandController.init(null)
                it.levelController.init(actionFactory, sensorFactory)
                it.agent = agent
                it.camera = camera
            }
            // select first weapon
            val agentsWeapons = agent.inventory.weapons
            for (weapon in weaponOrder)
                if (agentsWeapons.contains(weapon)) {
                    agent.weapon = weapon
                    break
                }

            val worldHolder = io.worldHolder
            worldHolder.world.isRunning = true
            return true
        } catch (e: IOException) {
            logger.error("Cannot start island map " + file.absolutePath, e)
            return false
        }
    }

    private fun findFirstPlayerAgent(): HumanAgent? {
        val entityMap = io.worldHolder.world.entityMap
        val agents = entityMap.getEntities(EntityType.AGENT)
        for (entity in agents) {
            val agent = entity as HumanAgent
            if (agent.team == playerHolder.team)
                return agent
        }
        return null
    }

    /**
     * Exit level map and display summary
     */
    fun exitLevelMap() {
        // TODO summary screen
        playerHolder.playerStats.finishedLevels += io.worldHolder.world.levelId
        startIslandMap()
    }

    companion object {
        private const val serialVersionUID = 8727551963670481421L
        private val logger = LoggerFactory.getLogger(MapStarter::class.java)
    }
}