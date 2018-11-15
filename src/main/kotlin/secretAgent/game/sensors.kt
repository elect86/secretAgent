package secretAgent.game

import cz.wa.secretagent.world.weapon.Weapon
import cz.wa.wautils.collection.Array2DView
import secretAgent.world.SamWorld
import secretAgent.world.TileType
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentEntity
import java.util.*


/**
 * Sensor for an agent.
 *
 * @author Ondrej Milenovsky
 */
abstract class AgentSensor<E : AgentEntity> {

    lateinit var entity: E
        internal set
    lateinit var world: SamWorld
        internal set
    lateinit var sensorFactory: SensorFactory
        internal set

    abstract fun init()
}

/**
 * Sensor about agent's weapons.
 *
 * @author Ondrej Milenovsky
 */
class AgentWeaponSensor internal constructor() : AgentSensor<HumanAgent>() {

    /**
     * @return list of weapons (even without ammo)
     */
    val weapons: Set<Weapon>
        get() = Collections.unmodifiableSet(entity.inventory.weapons)

    /** active weapon or null     */
    val activeWeapon: Weapon?
        get() = entity.weapon

    override fun init() {} // empty
}

/**
 * Sensor about level map.
 *
 * @author Ondrej Milenovsky
 */
class LevelSensor(private val world: SamWorld) {

    val tiles: Array2DView<TileType>
        get() = world.levelMap.types
}

/**
 * Factory creating sensors.
 *
 * @author Ondrej Milenovsky
 */
class SensorFactory(private val agent: AgentEntity, private val world: SamWorld) {
    private val cache: MutableMap<Any, AgentSensor<AgentEntity>>

    init {
        cache = HashMap()
    }

    fun <C> getSensor(clazz: Class<C>): C {
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

            val `object` = instance as AgentSensor<AgentEntity>
            `object`.entity = agent
            `object`.world = world
            `object`.init()
            cache[clazz] = `object`
            return instance
        }
    }
}
