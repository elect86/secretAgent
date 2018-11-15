package secretAgent.world.entity

import cz.wa.secretagent.Constants
import cz.wa.secretagent.world.EntityMap
import cz.wa.secretagent.world.entity.laser.LineLaser
import cz.wa.secretagent.world.weapon.Weapon
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentInventory
import secretAgent.world.entity.agent.AgentType
import secretAgent.world.entity.agent.Team
import java.io.Serializable
import java.util.*

/**
 * Entity is item, agent or door.
 *
 * @author Ondrej Milenovsky
 */
abstract class Entity @JvmOverloads constructor(val model: ObjectModel,
                                                /** position  */
                                                var pos: Vector2D,
                                                /** physical size  */
                                                var sizeBounds: Rectangle2D = DEFAULT_BOUNDS,
                                                /** current speed  */
                                                open var speed: Vector2D = Vector2D.ZERO,
                                                /** if cannot move  */
                                                var isStaticPos: Boolean = true) {

    /** primary type of the entity (never use instanceof !)     */
    abstract val type: EntityType

    /** secondary type of the entity (never use instanceof !)     */
    abstract val secondType: EntityType2

    companion object {

        val DEFAULT_BOUNDS = Rectangle2D(-Constants.TILE_SIZE.x / 2.0,
                -Constants.TILE_SIZE.y / 2.0, Constants.TILE_SIZE.x, Constants.TILE_SIZE.y)
    }
}

/**
 * Sorts by lines from up to down and left to right on each line.
 *
 * @author Ondrej Milenovsky
 */
object EntityComparator : Comparator<Entity> {

    override fun compare(o1: Entity, o2: Entity): Int {
        val p1 = o1.pos
        val p2 = o2.pos
        return when {
            p1.y < p2.y -> -1
            p1.y > p2.y -> 1
            else -> java.lang.Double.compare(p1.x, p2.x)
        }
    }
}

/**
 * Order of entity types.
 *
 * @author Ondrej Milenovsky
 */
class EntityOrder(val order: List<EntityType>) : Serializable {

    /**
     * @param entityMap entity map
     * @return new list of all entities with types only from the map sorted by the order,
     */
    fun getAllEntities(entityMap: EntityMap): List<Entity> {
        val ret = ArrayList<Entity>(entityMap.entityCount)
        // find types not defined by the order
        if (entityMap.typeCount > order.size) {
            val types = LinkedHashSet(entityMap.allTypes)
            types.removeAll(order)
        }
        // then add ordered types
        for (type in order)
            ret += entityMap.getEntities(type)
        return ret
    }

    /**
     * @param entityMap entity map
     * @return new list containing only entities with ordered type, ordered by the type
     */
    fun getOrderedEntities(entityMap: EntityMap): List<Entity> {
        val ret = ArrayList<Entity>(entityMap.entityCount)
        for (type in order)
            ret += entityMap.getEntities(type)
        return ret
    }

    companion object {
        private const val serialVersionUID = -5390144215185157807L
    }
}

/**
 * Single human agent (player or enemy), can jump.
 *
 * @author Ondrej Milenovsky
 */
class HumanAgent(model: ObjectModel, pos: Vector2D,
                 team: Team, direction: EntityXDirection,
                 sizeBounds: Rectangle2D)
    : AgentEntity(model, pos, team, direction, sizeBounds, Vector2D.ZERO, false) {

    // inventory
    val inventory = AgentInventory()
    /** active weapon, can be null  */
    var weapon: Weapon? = null
    /** if currently holding the jump button  */
    var isJumping = false
    /** jump time remaining, used when jumping  */
    var jumpRemainingS = 0.0
    /** current aiming angle (relative to direction, 0 is straight, +PI/2 is down, -PI/2 is UP)  */
    var aimAngle = 0.0
    /** current aiming action  */
    var aiming = EntityYDirection.NONE
    /** current laser sights, if not null, then is already in the world, when nulling, must be removed from the world !  */
    var laserSights: LineLaser? = null

    override val secondType: AgentType
        get() = AgentType.HUMAN
}

/**
 * Type of entity.
 *
 * @author Ondrej Milenovsky
 */
enum class EntityType {
    /** item that can be picked up  */
    ITEM,
    /** switch to change something in the map  */
    SWITCH,
    /** usable entity (door, building)  */
    USABLE,
    /** projectile that hurts agents (bullet, spike, fan, laser, explosion, ...)  */
    PROJECTILE,
    /** moving platform, can  */
    PLATFORM,
    /** some agent (enemy or player)  */
    AGENT,
    /** explosion  */
    EXPLOSION,
    /** laser  */
    LASER
}

/**
 * Second type of entity.
 *
 * @author Ondrej Milenovsky
 */
interface EntityType2   // empty

/**
 * Interface for entity that has time duration.
 *
 * @author Ondrej Milenovsky
 */
interface HasDuration {
    val durationMs: Long
}
/**
 * Entity that has model angle.
 *
 * @author Ondrej Milenovsky
 */
interface HasModelAngle {
    val modelAngle: Double
}

/**
 * Interface for entity that has time.
 *
 * @author Ondrej Milenovsky
 */
interface HasTime {
    val timeMs: Long
}

/**
 * Some entity that can be locked.
 *
 * @author Ondrej Milenovsky
 */
interface LockedEntity {

    val isLocked: Boolean

    val lockType: String?

    val type: EntityType

    val secondType: EntityType2

    fun unlock()
}


