package secretAgent.world.entity.bgSwitch

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel
import secretAgent.world.SamWorld
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.EntityType2
import secretAgent.world.entity.LockedEntity
import secretAgent.world.entity.agent.AgentEntity

/**
 * Simple switch to activate/deactivate something.
 *
 * @author Ondrej Milenovsky
 */
class SimpleSwitch(model: ObjectModel, pos: Vector2D,
                   lockType: String, singleUse: Boolean,
                   description: String, actions: List<SwitchAction>) :
        SwitchEntity(model, pos, lockType, singleUse, description, actions) {

    override val secondType: SwitchType
        get() = SwitchType.SIMPLE
}

/**
 * Some switch.
 *
 * @author Ondrej Milenovsky
 */
abstract class SwitchEntity(model: ObjectModel,
                            pos: Vector2D,
                            lockType: String?,
                            val isSingleUse: Boolean,
                            /** name displayed when player can use the switch */
                            val description: String,
                            val actions: List<SwitchAction>) : Entity(model, pos), LockedEntity {

    override val lockType: String? = lockType.takeUnless { it.isNullOrEmpty() }
    /** false only if is single use and already activated  */
    var isActive: Boolean = true
        private set
    override var isLocked: Boolean = lockType != null

    override val type: EntityType
        get() = EntityType.SWITCH

    abstract override val secondType: SwitchType

    override fun unlock() {
        isLocked = false
    }

    fun activate(agent: AgentEntity, world: SamWorld) {
        if (isActive) {
            for (action in actions)
                action.execute(agent, world)
            if (isSingleUse)
                isActive = false
        }
    }
}

/**
 * Type of a switch.
 *
 * @author Ondrej Milenovsky
 */
enum class SwitchType : EntityType2 {
    SIMPLE
}
