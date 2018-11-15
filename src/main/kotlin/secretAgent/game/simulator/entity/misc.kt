package secretAgent.game.simulator.entity

import cz.wa.secretagent.worldinfo.WorldHolder
import org.slf4j.LoggerFactory
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType2
import java.io.Serializable

/**
 * Simulates an entity by type. Has reference to the world.
 *
 * @author Ondrej Milenovsky
 */
abstract class AbstractEntitySimulator<E : Entity> : EntitySimulator<E> {

    lateinit var worldHolder: WorldHolder

    companion object {
        private const val serialVersionUID = -1392493042338721293L
    }
}

/**
 * Simulates an entity by type.
 *
 * @author Ondrej Milenovsky
 */
interface EntitySimulator<E : Entity> : Serializable {
    /**
     * @param entity entity to simulate
     * @param timeS time diff in seconds
     * @return false if stop further moving of other entities
     */
    fun move(entity: E, timeS: Double): Boolean
}

/**
 * Simulates entity according to its second type.
 * Has map of simulators for entity second type.
 *
 * @author Ondrej Milenovsky
 */
class MultipleEntitySimulator<E : Entity> : EntitySimulator<E> {

    lateinit var simulators: Map<EntityType2, EntitySimulator<E>>

    override fun move(entity: E, timeS: Double): Boolean {
        val type2 = entity.secondType
        return simulators[type2]?.move(entity, timeS) ?: true.also {
            logger.warn("No simulator for " + entity.type + "." + type2)
        }
    }

    companion object {
        private const val serialVersionUID = -4024151035446413564L
        private val logger = LoggerFactory.getLogger(MultipleEntitySimulator::class.java)
    }
}