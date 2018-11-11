package secretAgent.game.action

import cz.wa.secretagent.game.sensor.SensorFactory
import cz.wa.secretagent.game.starter.MapStarter
import cz.wa.secretagent.world.entity.agent.AgentEntity
import secretAgent.game.ProjectileFactory
import secretAgent.world.SamWorld
import java.util.HashMap

/**
 * Creates actions with caching.
 *
 * @author Ondrej Milenovsky
 */
/**
 * Creates actions with caching.
 *
 * @author Ondrej Milenovsky
 */
class ActionFactory(
        private val agent: AgentEntity,
        private val world: SamWorld,
        private val sensorFactory: SensorFactory,
        val mapStarter: MapStarter,
        val projectileFactory: ProjectileFactory?) {

    private val cache: MutableMap<Any, AgentAction<AgentEntity>> = HashMap()

    fun <C> getAction(clazz: Class<C>): C {
        if (cache.containsKey(clazz)) {
            return cache[clazz] as C
        } else {
            val instance: C
            try {
                instance = clazz.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }

            val `object` = instance as AgentAction<AgentEntity>
            `object`.agent = agent
            `object`.world = world
            `object`.actionFactory = this
            `object`.sensorFactory = sensorFactory
            `object`.init()
            cache[clazz] = `object`
            return instance
        }
    }
}


/**
 * Action to control some agent.
 * Most actions are on/off, if an agent calls fire(true), then he fires until called fire(false).
 *
 * @author Ondrej Milenovsky
 */
abstract class AgentAction<E : AgentEntity> {

    var agent: E? = null

    var world: SamWorld? = null
    //        internal set
    var actionFactory: ActionFactory? = null
    //        internal set
    var sensorFactory: SensorFactory? = null
//        internal set

    abstract fun init()
}