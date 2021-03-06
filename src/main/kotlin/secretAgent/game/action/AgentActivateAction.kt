package secretAgent.game.action

import cz.wa.secretagent.world.entity.usable.*
import org.slf4j.LoggerFactory
import secretAgent.game.ProjectileFactory
import secretAgent.game.starter.MapStarter
import secretAgent.world.entity.*
import secretAgent.world.entity.bgSwitch.SwitchEntity

/**
 * Action performing activating items. Only humans can do that.
 *
 * @author Ondrej Milenovsky
 */
class AgentActivateAction : AgentAction<HumanAgent>() {

    private var mapStarter: MapStarter? = null
    private var projectileFactory: ProjectileFactory? = null
    private var activating = true

    override fun init() {
        mapStarter = actionFactory!!.mapStarter
        projectileFactory = actionFactory!!.projectileFactory
    }

    /**
     * Start using the usable
     * @param entity entity to use
     */
    fun useEntity(entity: Entity) {
        if (activating)
            return
        activating = true

        if (entity.type == EntityType.USABLE)
            useUsable(entity as UsableEntity)
        else if (entity.type == EntityType.SWITCH)
            useSwitch(entity as SwitchEntity)
    }

    private fun useSwitch(entity: SwitchEntity) {
        if (tryUnlock(entity))
            entity.activate(agent!!, world!!)
    }

    private fun useUsable(usable: UsableEntity) {
        if (usable.secondType == UsableType.DOOR) {
            val door = usable as DoorUsable
            if (tryUnlock(door)) {
                // completely remove the door
                world!!.entityMap.removeEntity(door)
                val levelMap = world!!.levelMap
                levelMap.updateType(levelMap.getNearestTilePos(door.pos))
            }
        } else if (usable.secondType == UsableType.TELEPORT) {
            val teleport = usable as TeleportUsable
            teleport.destination?.let { agent!!.pos = it }
        } else if (usable.secondType == UsableType.EXIT) {
            mapStarter!!.exitLevelMap()
        } else if (usable.secondType == UsableType.EXIT_DOOR) {
            val dynamite = findDynamiteItem()
            dynamite?.let {
                usable.isActive = false
                useDynamite(it)
            }
        } else if (usable.secondType == UsableType.BUILDING) {
            val building = usable as BuildingUsable
            val levelId = building.levelId
            if (levelId > 0)
                mapStarter!!.startLevelMap(levelId)
            else
                logger.warn("Not linked level for building at " + building.pos)
        }
    }

    /**
     * Tries to find dynamite in inventory
     * @return dynamite item or null
     */
    private fun findDynamiteItem(): KeyItem? {
        for (entity in agent!!.inventory.getItems(ItemType.KEY)) {
            val key = entity as KeyItem
            if (key.lockType == DYNAMITE_LOCK) {
                return key
            }
        }
        return null
    }

    /**
     * Use the dynamite (remove from inventory and place)
     */
    private fun useDynamite(key: KeyItem) {
        agent!!.inventory.removeItem(key)
        val dynamite = projectileFactory!!.createDynamite(agent!!.pos)
        world!!.entityMap.addEntity(dynamite)
    }

    /**
     * Stop using, must be called after last use to reload
     */
    fun stopUsing() {
        activating = false
    }

    /**
     * Tries to unlock the door or switch. If can unlock, removes the key but does not activate the usable.
     * @return true if success
     */
    private fun tryUnlock(usable: LockedEntity): Boolean {
        if (usable.isLocked) {
            val key = findKey(usable)
            return if (key != null) {
                usable.unlock()
                agent!!.inventory.removeItem(key)
                true
            } else {
                false
            }
        }
        return true
    }

    /**
     * Find key for the usable, may be null
     */
    private fun findKey(usable: LockedEntity): KeyItem? {
        for (item in agent!!.inventory.getItems(ItemType.KEY)) {
            val key = item as KeyItem
            if (key.lockType == usable.lockType) {
                return key
            }
        }
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AgentActivateAction::class.java)

        private const val DYNAMITE_LOCK = "dynamite"
    }
}
