package secretAgent.game.action

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.plus
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.EntityYDirection
import secretAgent.world.entity.HumanAgent

/**
 * Action to control an agent on island map.
 *
 * @author Ondrej Milenovsky
 */
class AgentIslandAction : AgentAction<HumanAgent>() {

    lateinit var activateAction: AgentActivateAction

    override fun init() {
        activateAction = actionFactory!!.getAction(AgentActivateAction::class.java)
    }

    fun moveX(dir: EntityXDirection) {
        val agent = agent!!
        if (agent.isControllable) {
            val maxSpeed = agent.capabilities.maxSpeed
            val addSpeed = dir * maxSpeed
            agent.moveSpeed = Vector2D(0.0, agent.moveSpeed.y) + addSpeed
            if (dir != EntityXDirection.NONE)
                agent.direction = dir
        }
    }

    fun moveY(dir: EntityYDirection) {
        val agent = agent!!
        if (agent.isControllable) {
            val maxSpeed = agent.capabilities.maxSpeed
            agent.moveSpeed = Vector2D(agent.moveSpeed.x, 0.0) + dir * maxSpeed
        }
    }

    fun activate(b: Boolean) {
        if (!b) {
            activateAction.stopUsing()
            return
        }
        val agent = agent!!
        if (agent.isControllable && agent.capabilities.canActivate)
            agent.entityToUse?.let(activateAction::useEntity)
    }
}