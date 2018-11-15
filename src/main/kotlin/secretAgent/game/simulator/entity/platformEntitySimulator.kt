package secretAgent.game.simulator.entity

import cz.wa.secretagent.world.entity.platform.MovableCan
import cz.wa.secretagent.world.entity.platform.PlatformEntity
import cz.wa.secretagent.world.entity.platform.PlatformLift
import cz.wa.secretagent.world.entity.platform.PlatformType
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.game.utils.EntitiesFinder
import secretAgent.game.utils.EntityMover
import secretAgent.game.utils.EntityObserver
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.agent.AgentEntity

/**
 * Simulates a pushable can.
 * Can is pushed by agents and moves things stanting on it.
 *
 * @author Ondrej Milenovsky
 */
class CanEntitySimulator : AbstractEntitySimulator<PlatformEntity>() {

    var canFallG = 0.0
    var canMaxSpeed = 0.0

    override fun move(entity: PlatformEntity, timeS: Double): Boolean {
        moveCan(entity as MovableCan, timeS)
        return true
    }

    /**
     * Can is being pushed by agents that can activate, moves entities standing on it.
     */
    private fun moveCan(can: MovableCan, timeS: Double) {
        val world = worldHolder.world
        // push
        for (e2 in world.entityMap.getEntities(EntityType.AGENT)) {
            val agent = e2 as AgentEntity
            // get agent's future position
            val agentPos = agent.pos.add(agent.speed.add(agent.moveSpeed).scalarMultiply(timeS))
            if (agent.capabilities.canActivate && isAgentPushing(can, agent, agentPos))
                pushCan(can, agent, agentPos, timeS)
        }
        // push with other cans
        val bounds = can.sizeBounds.move(can.pos)
        for (e2 in world.entityMap.getEntities(EntityType.PLATFORM)) {
            if (e2.secondType == PlatformType.CAN) {
                val bounds2 = e2.sizeBounds.move(e2.pos)
                if (intersectFromSide(bounds, bounds2))
                    separateCans(can, e2)
            }
        }

        // fall
        val posSensor = EntityObserver(can, world)
        val canMover = EntityMover(world)
        val spdX = can.speed.x
        val onGround = posSensor.isOnGround
        if (!onGround) {
            var spdY = can.speed.y + canFallG * timeS
            spdY = FastMath.min(spdY, canMaxSpeed)
            can.speed = Vector2D(spdX, spdY)
            canMover.moveWithStepables(can, can.speed.scalarMultiply(timeS), onGround, posSensor)
        } else
            can.speed = Vector2D(spdX, 0.0)
        // clip
        canMover.doClipping(can)
    }

    /** If the two bounds intersect and and are side by side     */
    private fun intersectFromSide(b1: Rectangle2D, b2: Rectangle2D): Boolean =
            b1.x < b2.x2 && b1.x2 > b2.x && FastMath.abs(b1.y - b2.y) < b1.height / 2.0

    /** Move the two cans from each other in x     */
    private fun separateCans(can: MovableCan, e2: Entity) {
        val dist = can.pos.x - e2.pos.x
        val move = Vector2D(dist / 2.0, 0.0)
        can.pos = can.pos.add(move)
        e2.pos = e2.pos.subtract(move)
    }

    /** Push the can by the agent and move items on it.     */
    private fun pushCan(can: MovableCan, agent: AgentEntity, agentPos: Vector2D, timeS: Double) {
        // get entities standing on it
        val carying = EntitiesFinder(worldHolder.world).findEntitiesToCarry(can)

        // move
        val oldPos = can.pos
        if (oldPos.x < agentPos.x)
            pushLeft(can, agent, agentPos)
        else
            pushRight(can, agent, agentPos)

        // move entities on it
        val speed = can.pos.subtract(oldPos)
        for (e2 in carying)
            e2.pos = e2.pos.add(speed)
    }

    private fun pushLeft(can: MovableCan, agent: AgentEntity, agentPos: Vector2D) {
        val newX = agent.sizeBounds.x + agentPos.x - can.sizeBounds.x2
        can.pos = Vector2D(newX, can.pos.y)
    }

    private fun pushRight(can: MovableCan, agent: AgentEntity, agentPos: Vector2D) {
        val newX = agent.sizeBounds.x2 + agentPos.x - can.sizeBounds.x
        can.pos = Vector2D(newX, can.pos.y)
    }

    /**
     * If the agent is touching the can from sides and touching can's y center
     */
    private fun isAgentPushing(can: MovableCan, agent: AgentEntity, agentPos: Vector2D): Boolean {
        val canBounds = can.sizeBounds.move(can.pos)
        val agentBounds = agent.sizeBounds.move(agentPos)
        return agentBounds.x < canBounds.x2 && agentBounds.x2 > canBounds.x && agentBounds.y <= can.pos.y && agentBounds.y2 >= can.pos.y
    }

    companion object {
        private const val serialVersionUID = -3871663176151622542L
    }
}


/**
 * Simulates a platform.
 * Lift moves with things standing on it.
 *
 * @author Ondrej Milenovsky
 */
class LiftEntitySimulator : AbstractEntitySimulator<PlatformEntity>() {

    override fun move(entity: PlatformEntity, timeS: Double): Boolean {
        moveLift(entity as PlatformLift, timeS)
        return true
    }

    /**
     * Lift moves by itself and moves entities standing on it.
     */
    private fun moveLift(lift: PlatformLift, timeS: Double) {
        // get entities standing on it
        val carying = EntitiesFinder(worldHolder.world).findEntitiesToCarry(lift)

        // move
        var speed = lift.speed
        speed = when {
            lift.isMovingForward -> speed.scalarMultiply(timeS)
            else -> speed.scalarMultiply(-timeS)
        }
        val oldPos = lift.pos
        lift.pos = oldPos.add(speed)

        // check if hit something
        val posSensor = EntityObserver(lift, worldHolder.world)
        val objBounds = posSensor.touchingSolidObject
        if (objBounds != null) {
            // reverse direction
            lift.isMovingForward = !lift.isMovingForward
            // move a little back
            val dist = lift.sizeBounds.move(lift.pos).getIntersectingDist(objBounds)
            speed = speed.scalarMultiply(-dist / speed.norm)
            lift.pos = lift.pos.add(speed)
        }

        // move entities on it
        speed = lift.pos.subtract(oldPos)
        for (e2 in carying)
            e2.pos = e2.pos.add(speed)
    }

    companion object {
        private const val serialVersionUID = -8448655199600104508L
    }
}
