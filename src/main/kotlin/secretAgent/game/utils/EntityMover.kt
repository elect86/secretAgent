package secretAgent.game.utils

import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.world.LevelMap
import secretAgent.world.SamWorld
import secretAgent.world.TileType
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentAction
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentType

/**
 * Class with useful methods for simulators and sensors.
 *
 * @author Ondrej Milenovsky
 */
class EntityMover(private val world: SamWorld) {

    fun moveOnIsland(agent: AgentEntity, timeS: Double, maxStaySpeed: Double) {
        var speedMove = agent.moveSpeed
        // limit move speed
        val maxSpeed = agent.capabilities.maxSpeed
        var speedNorm = speedMove.norm
        if (speedNorm > maxSpeed) {
            speedMove = speedMove.scalarMultiply(maxSpeed / speedNorm)
            speedNorm = maxSpeed
            agent.moveSpeed = speedMove
        }

        // set action
        agent.currentAction = when {
            speedNorm <= maxStaySpeed -> AgentAction.STAY
            else -> AgentAction.MOVE
        }

        // move
        val pos = agent.pos.add(speedMove.scalarMultiply(timeS))
        agent.pos = pos
    }

    /**
     * Modifies speed and moves the agent. Does not perform clipping except shelfs and platforms.
     * @param agent agent to move
     * @param timeS time diff
     * @param maxStaySpeed max speed still considered as staying
     * @param maxTotalSpeed max total speed
     * @param newAction_ new action
     * @param onGround if standing on ground
     * @param posSensor position sensor
     * @return modified new action
     */
    fun moveInLevel(agent: AgentEntity, timeS: Double, maxStaySpeed: Double,
                    maxTotalSpeed: Double, newAction_: AgentAction, onGround: Boolean, posSensor: EntityObserver): AgentAction {
        var newAction = newAction_
        var speedMove = agent.moveSpeed
        // limit move speed
        val maxSpeed = agent.capabilities.maxSpeed
        var speedX = speedMove.x
        if (speedX > maxSpeed)
            speedX = maxSpeed
        else if (speedX < -maxSpeed)
            speedX = -maxSpeed
        speedMove = Vector2D(speedX, speedMove.y)
        agent.moveSpeed = speedMove

        // jump
        if (agent.secondType == AgentType.HUMAN) {
            val human = agent as HumanAgent
            if (human.isJumping) {
                val jumpTimeS = FastMath.min(human.jumpRemainingS, timeS)
                human.jumpRemainingS = human.jumpRemainingS - timeS

                val jumpAdd = Vector2D(0.0, -agent.capabilities.jumpStrength * jumpTimeS)
                agent.speed = agent.speed.add(jumpAdd)
                if (human.jumpRemainingS <= 0)
                    human.isJumping = false
            }
        }

        // set action
        if (newAction != AgentAction.JUMP && FastMath.abs(speedX) > maxStaySpeed)
            newAction = AgentAction.MOVE

        // limit total speed
        var speed = agent.speed.add(speedMove)
        val speedNorm = speed.norm
        if (speedNorm > maxTotalSpeed) {
            speed = speed.scalarMultiply(maxTotalSpeed / speedNorm)
            agent.speed = speed.subtract(speedMove)
        }
        // move
        val speedPS = speed.scalarMultiply(timeS)
        moveWithStepables(agent, speedPS, onGround, posSensor)
        return newAction
    }

    /**
     * Moves the agent and checks if fell on shelf or platform
     * @param entity the entity to move
     * @param speedPS speed in pixel/s
     * @param onGround if standing on ground
     */
    fun moveWithStepables(agent: Entity, speedPS: Vector2D, onGround: Boolean, posSensor: EntityObserver) {
        // move
        val pos = agent.pos.add(speedPS)
        agent.pos = pos

        // check shelfs and platforms if falling
        if (!onGround && speedPS.y > 0) {
            // shelfs
            val tilesUnder = posSensor.tilesUnder
            val agentBounds = agent.sizeBounds.move(pos)
            for (tile in tilesUnder)
                // check if he is touching a shelf with legs
                if (tile.type == TileType.SHELF && agentBounds.intersects(tile.bounds))
                    if (stepOn(agent, speedPS, agentBounds, tile.bounds))
                        return
            // platforms
            for (entity in EntitiesFinder(world).solidEntities) {
                val entityBounds = entity.sizeBounds.move(entity.pos)
                if (agentBounds.intersects(entityBounds) && stepOn(agent, speedPS, agentBounds, entityBounds))
                    return
            }
        }
    }

    /**
     * Makes the agent step on the target tile (or platform)
     * @param entity the entity
     * @param speedPS speed for this frame
     * @param agentBounds moved bounds of the agent
     * @param tileBounds not moved bounds of the tile
     * @return true if stepped
     */
    private fun stepOn(agent: Entity, speedPS: Vector2D, agentBounds: Rectangle2D, tileBounds: Rectangle2D): Boolean {
        // check if was above the tile before moved
        var pos = agent.pos
        val y2 = agentBounds.y2 - speedPS.y
        val tileY1 = tileBounds.y
        if (y2 <= tileY1) {
            pos = Vector2D(pos.x, tileY1 - agent.sizeBounds.y2)
            agent.pos = pos
            agent.speed = Vector2D(agent.speed.x, 0.0)
            // no need to check anything else
            return true
        }
        return false
    }

    /**
     * Pushes agent (entity) the best direction from the walls
     */
    fun doClipping(agent: Entity) {
        // DISGUSTING EVIL !!! I'm sure the original game has something nicer
        val map = world.levelMap
        // agent pos
        var px = agent.pos.x
        var py = agent.pos.y
        // agent speed
        var speed = agent.speed
        if (agent.type == EntityType.AGENT) {
            speed = speed.add((agent as AgentEntity).moveSpeed)
        }
        val spdX = speed.x
        val spdY = speed.y
        val spdX2 = agent.speed.x
        var spdY2 = agent.speed.y
        // agent bounds
        val agentSize = agent.sizeBounds
        var sx1 = px + agentSize.x
        var sy1 = py + agentSize.y
        var sx2 = px + agentSize.x2
        var sy2 = py + agentSize.y2
        // tile index
        val tileSize = map.tileSize
        val ix = FastMath.floor(px / tileSize.x).toInt()
        val iy = FastMath.floor(py / tileSize.y).toInt()
        // tile pos
        val tx = (ix + 0.5) * tileSize.x
        val ty = (iy + 0.5) * tileSize.y
        // check only 4 closest tiles around him
        val t11 = getType(map, ix, iy)
        val t21 = getType(map, ix + 1, iy)
        val t12 = getType(map, ix, iy + 1)
        val t22 = getType(map, ix + 1, iy + 1)
        // stop speed
        var stopX = false
        var stopY = false
        var stopJump = false
        // upper left tile
        if (t11 == TileType.WALL && sx1 < tx && sy1 < ty) {
            val dx = tx - sx1
            val dy = ty - sy1
            if (t12 == TileType.WALL || t21 != TileType.WALL && dx < dy) {
                // push right
                px += dx
                sx1 += dx
                sx2 += dx
                if (spdX < 0 && spdX2 < 0)
                    stopX = true
            } else {
                // push down
                py += dy
                sy1 += dy
                sy2 += dy
                if (spdY < 0 && spdY2 < 0)
                    stopY = true
                stopJump = true
            }
        }
        // upper right tile
        if (t21 == TileType.WALL && sx2 > tx && sy1 < ty) {
            val dx = tx - sx2
            val dy = ty - sy1
            if (t22 == TileType.WALL || t11 != TileType.WALL && -dx < dy) {
                // push left
                px += dx
                sx1 += dx
                sx2 += dx
                if (spdX > 0 && spdX2 > 0)
                    stopX = true
            } else {
                // push down
                val d = ty - sy1
                py += d
                sy1 += d
                sy2 += d
                if (spdY < 0 && spdY2 < 0)
                    stopY = true
                stopJump = true
            }
        }
        // lower left tile
        if (t12 == TileType.WALL && sx1 < tx && sy2 > ty) {
            val dx = tx - sx1
            val dy = ty - sy2
            if (t11 == TileType.WALL || t22 != TileType.WALL && dx < -dy) {
                // push right
                px += dx
                sx1 += dx
                sx2 += dx
                if (spdX < 0 && spdX2 < 0)
                    stopX = true
            } else {
                // push up
                py += dy
                sy1 += dy
                sy2 += dy
                if (spdY > 0 && spdY2 > 0)
                    stopY = true
                stopJump = true
            }
        }
        // lower right tile
        if (t22 == TileType.WALL && sx2 > tx && sy2 > ty) {
            val dx = tx - sx2
            val dy = ty - sy2
            if (t21 == TileType.WALL || t12 != TileType.WALL && -dx < -dy) {
                // push left
                px += dx
                sx1 += dx
                sx2 += dx
                if (spdX > 0 && spdX2 > 0)
                    stopX = true
            } else {
                // push up
                py += dy
                sy1 += dy
                sy2 += dy
                if (spdY > 0 && spdY2 > 0)
                    stopY = true
                stopJump = true
            }
        }
        if (stopJump && agent.secondType === AgentType.HUMAN && (agent as HumanAgent).isJumping) {
            spdY2 = 0.0
        }
        speed = Vector2D(if (stopX) 0.0 else spdX2, if (stopY) 0.0 else spdY2)
        agent.speed = speed
        agent.pos = Vector2D(px, py)
    }

    /**
     * Get type at the position, if out of map, returns WALL.
     */
    private fun getType(map: LevelMap, x: Int, y: Int): TileType {
        val i = Vector2I(x, y)
        val types = map.types
        return when {
            types.isInside(i) -> types.get(i)
            else -> TileType.WALL
        }
    }

}