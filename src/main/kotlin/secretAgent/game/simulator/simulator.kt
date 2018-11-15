package secretAgent.game.simulator

import cz.wa.secretagent.worldinfo.WorldHolder
import org.springframework.beans.factory.annotation.Required
import secretAgent.GameSimulator
import secretAgent.game.PlayerHolder
import secretAgent.game.simulator.entity.EntitySimulator
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityOrder
import secretAgent.world.entity.EntityType
import java.util.*

/**
 * The main game simulator, simulates only world, not menu.
 * The only things to simulate are entities. (tiles are animated by world time)
 * The entities are processed by order of the simulators.
 *
 * @author Ondrej Milenovsky
 */
class EntitiesSimulator : GameSimulator {

    lateinit var worldHolder: WorldHolder
    /**
     * @param simulators simulators for entity types, the entities are simulated in this order
     */
    var simulators: Map<EntityType, EntitySimulator<Entity>>? = null
        @Required
        set(simulators) {
            field = simulators
            entityOrder = EntityOrder(ArrayList(simulators!!.keys))
        }
    private var entityOrder: EntityOrder? = null
    lateinit var cameraSimulator: GameSimulator

    override fun move(timeS: Double): Boolean {
        if (worldHolder.menuHolder.isMenuActive) {
            return true
        }
        val entityMap = worldHolder.world.entityMap
        for (entity in entityOrder!!.getOrderedEntities(entityMap)) {
            val type = entity.type
            // something could have removed the entity, so check, if it is still there
            if (entityMap.getEntities(type).contains(entity))
                if (!simulators!![type]!!.move(entity, timeS))
                    return false
        }
        cameraSimulator.move(timeS)
        return true
    }

    companion object {
        private const val serialVersionUID = 5390556751042901539L
    }
}

/**
 * Keeps the player in center of screen but does not let the camera see outside map.
 *
 * @author Ondrej Milenovsky
 */
class SimpleCameraSimulator : GameSimulator {

    lateinit var playerHolder: PlayerHolder
    lateinit var worldHolder: WorldHolder

    override fun move(timeS: Double): Boolean {
        val camera = playerHolder.camera
        camera.pos = playerHolder.agent.pos
        camera.limitInMap(worldHolder.world.bounds)
        return true
    }

    companion object {
        private const val serialVersionUID = -3960824364903377922L
    }
}