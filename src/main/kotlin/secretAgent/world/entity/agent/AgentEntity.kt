package secretAgent.world.entity.agent

import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.model.AgentModel
import secretAgent.world.ObjectModel
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.EntityXDirection

/**
 * Some agent (human, robot).
 *
 * @author Ondrej Milenovsky
 */
abstract class AgentEntity(model: ObjectModel, pos: Vector2D,
                           /** team of the agent, agents from same team cannot hurt each other (except explosions)  */
                           val team: Team,
                           direction: EntityXDirection, sizeBounds: Rectangle2D,
                           speed: Vector2D, staticPos: Boolean)
    : Entity(model, pos, sizeBounds, speed, staticPos) {

    lateinit var capabilities: AgentCapabilities
    var health = 1.0

    // movement
    /** how long is moving same action  */
    var actionTime: Long = 0
        private set
    /** current action  */
    internal var currentAction: AgentAction = AgentAction.STAY
        /** Sets new current action and resets action time if action changed */
        set(value) {
            if (field != value)
                actionTime = 0
            field = value
        }
    /** current x direction  */
    var direction: EntityXDirection = direction
        set(direction) {
            assert(direction != EntityXDirection.NONE) { "direction must not be NONE" }
            field = direction
        }
    /** moving speed caused by the agent  */
    var moveSpeed = Vector2D.ZERO

    // activating
    /** entity that can be used right now or null  */
    var entityToUse: Entity? = null

    // weapons
    /** remaining reload time when firing weapon  */
    var reloadTimeRemainingS: Double = 0.toDouble()
    /** if the trigger is pressed  */
    var isFiring: Boolean = false

    val isControllable: Boolean
        get() = currentAction != AgentAction.DEATH

    /**
     * @return weapon rotation center relative to agent's position according to agent's direction
     */
    val weaponCenter: Vector2D
        get() {
            val model = model
            return when (model) {
                is AgentModel -> when (direction) {
                    EntityXDirection.LEFT -> Vector2D(-model.weaponCenter.x, model.weaponCenter.y)
                    else -> model.weaponCenter
                }
                else -> Vector2D.ZERO
            }
        }

    override val type: EntityType
        get() = EntityType.AGENT

    abstract override val secondType: AgentType

    fun addActionTime(timeMs: Long) {
        actionTime += timeMs
    }

    fun resetActionTime() {
        actionTime = 0
    }
}