package secretAgent.world.entity.bgSwitch

import cz.wa.secretagent.world.entity.laser.LaserEntity
import cz.wa.secretagent.world.map.StoredTile
import secretAgent.view.renderer.TileId
import secretAgent.world.SamWorld
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.agent.AgentEntity
import java.util.*

/**
 * Adds tiles to the map.
 *
 * @author Ondrej Milenovsky
 */
class AddTilesSwitchAction(val tileIds: Set<TileId>) : SwitchAction {

    override fun execute(agent: AgentEntity, world: SamWorld) {
        for (tile in getStoredTiles(world))
            world.levelMap.addTile(tile)
    }

    /** Find and remove required tiles from stored tiles.     */
    private fun getStoredTiles(world: SamWorld): List<StoredTile> {
        val storedTiles = world.levelMap.storedTiles
        val ret = ArrayList<StoredTile>(storedTiles.size)
        val it = storedTiles.iterator()
        while (it.hasNext()) {
            val tile = it.next()
            for (tileId in tileIds)
                if (tile.tile.hasTileId(tileId)) {
                    ret+=tile
                    it.remove()
                    break
                }
        }
        return ret
    }
}

/**
 * Disables laser.
 *
 * @author Ondrej Milenovsky
 */
class DisableLaserSwitchAction : SwitchAction {

    override fun execute(agent: AgentEntity, world: SamWorld) {
        val entityMap = world.entityMap
        for (entity in ArrayList(entityMap.getEntities(EntityType.LASER)))
            if ((entity as LaserEntity).isLevelLaser)
                entityMap.removeEntity(entity)
    }
}

/**
 * Action of a switch.
 *
 * @author Ondrej Milenovsky
 */
interface SwitchAction {
    /**
     * Execute the action.
     * @param agent
     * @param world
     */
    fun execute(agent: AgentEntity, world: SamWorld)
}
