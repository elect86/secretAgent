package secretAgent.game.action

import cz.wa.secretagent.game.action.ActionFactory
import cz.wa.secretagent.game.sensor.SensorFactory
import cz.wa.secretagent.world.entity.agent.AgentEntity
import secretAgent.world.SamWorld

/**
 * Creates actions with caching.
 *
 * @author Ondrej Milenovsky
 */
//class ActionFactory(
//        val agent: AgentEntity,
//        val world: SamWorld,
//        val sensorFactory: SensorFactory,
//        val mapStarter: MapStarter,
//        val projectileFactory: ProjectileFactory?) {
//
//    val cache: MutableMap<Any, AgentAction<AgentEntity>> = HashMap()
//
//    fun <C>getAction(clazz: Class<C>): C = cache.getOrPut(clazz){
//
//        (clazz.getDeclaredConstructor().newInstance() as AgentAction<AgentEntity>).also {
//                it.agent = agent
//                it.world = world
//                it.actionFactory = this
//                it.sensorFactory = sensorFactory
//                it.init()
//            }
//        } as C
//}

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