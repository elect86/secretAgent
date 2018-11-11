package secretAgent.game.simulator.entity

import cz.wa.secretagent.game.utils.EntitiesFinder
import cz.wa.secretagent.game.utils.EntityMover
import cz.wa.secretagent.game.utils.EntityObserver
import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.EntityType
import cz.wa.secretagent.world.entity.agent.AgentAction
import cz.wa.secretagent.world.entity.agent.AgentEntity
import cz.wa.secretagent.world.entity.agent.AgentType
import cz.wa.secretagent.world.entity.agent.HumanAgent
import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity
import cz.wa.secretagent.world.entity.item.AmmoItem
import cz.wa.secretagent.world.entity.item.ItemEntity
import cz.wa.secretagent.world.entity.item.ItemType
import cz.wa.secretagent.world.entity.projectile.BulletProjectile
import cz.wa.secretagent.world.entity.projectile.ProjectileEntity
import cz.wa.secretagent.world.entity.usable.DoorUsable
import cz.wa.secretagent.world.entity.usable.UsableType
import cz.wa.wautils.math.VectorUtils
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.springframework.beans.factory.annotation.Required
import secretAgent.game.ProjectileFactory
import secretAgent.game.utils.ProjectileCollider
import secretAgent.view.model.EmptyModel
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.EntityYDirection
import java.util.ArrayList

/**
 * Simulates an agent.
 * Agent interacts with solid items in the world. Can use usables, pick up items, fire, move.
 * This simulator does not care about platforms moving with the agent, explosions, projectiles, pushing cans.
 *
 * @author Ondrej Milenovsky
 */
class AgentEntitySimulator : AbstractEntitySimulator<AgentEntity>() {

    private val STAY_MAX_SPEED = 1.0
    private val MAX_DIST_FROM_USABLE = 2.0

    lateinit var projectileFactory: ProjectileFactory

    var fallG = 0.0
    /** aiming speed deg/sec  */
    var aimSpeedDS = 0.0

    var maxTotalSpeed = 0.0
    /** speed of slowing down when standing on ground  */
    var slowMove = 0.0

    override fun move(entity: AgentEntity, timeS: Double): Boolean {
        if (worldHolder.world.isIsland)
            moveOnIsland(entity, timeS)
        else
            moveInLevel(entity, timeS)
        return true
    }

    /** Perform moving on island.     */
    private fun moveOnIsland(agent: AgentEntity, timeS: Double) {
        agent.addActionTime(FastMath.round(timeS * 1000))
        val entityMover = EntityMover(worldHolder.world)

        // move
        entityMover.moveOnIsland(agent, timeS, STAY_MAX_SPEED)
        // clip
        entityMover.doClipping(agent)
        // find usable item
        findUsables(agent)
    }

    /** Finds and processes usables */
    private fun findUsables(agent: AgentEntity) {
        if (agent.capabilities.canActivate()) {
            val usable = EntitiesFinder(worldHolder.world).findUsableEntity(agent,
                    MAX_DIST_FROM_USABLE)
            agent.entityToUse = usable
            val playerHolder = worldHolder.playerHolder
            playerHolder.displayedText = when {
                playerHolder.agent === agent && usable != null -> createDysplayText(usable)
                else -> null
            }
        }
    }

    /** Creates text to display when about to use some entity     */
    private fun createDysplayText(usable: Entity): String = when (usable.type) {
        EntityType.SWITCH -> (usable as SwitchEntity).description
        EntityType.USABLE -> when (usable.secondType) {
            UsableType.DOOR -> "Open door with " + (usable as DoorUsable).lockType
            UsableType.TELEPORT -> "Use teleport"
            UsableType.BUILDING -> "Enter building"
            UsableType.EXIT_DOOR -> "Use dynamite"
            UsableType.EXIT -> "Finish building"
            else -> "Activate"
        }
        else -> "Activate"
    }

    /** Perform moving in level.     */
    private fun moveInLevel(agent: AgentEntity, timeS: Double) {
        agent.addActionTime(FastMath.round(timeS * 1000))
        val entityMover = EntityMover(worldHolder.world)
        var newAction = AgentAction.STAY
        // fall
        val posSensor = EntityObserver(agent, worldHolder.world)
        val speed = agent.speed
        val onGround = posSensor.isOnGround
        if (onGround) {
            // remove blast speed, fall and jump
            var spdX = speed.x
            if (spdX != 0.0) {
                if (spdX > 0) {
                    spdX -= slowMove * timeS
                    if (spdX < 0)
                        spdX = 0.0
                } else {
                    spdX += slowMove * timeS
                    if (spdX > 0)
                        spdX = 0.0
                }
            }
            agent.speed = Vector2D(spdX, 0.0)
        } else {
            agent.speed = speed.add(Vector2D(0.0, fallG * timeS))
            newAction = AgentAction.JUMP
        }
        // move
        newAction = entityMover.moveInLevel(agent, timeS, STAY_MAX_SPEED, maxTotalSpeed, newAction, onGround, posSensor)
        // clip
        entityMover.doClipping(agent)
        // find usable item
        findUsables(agent)

        if (agent.currentAction != AgentAction.DEATH)
            agent.currentAction = newAction
        // pick up items
        if (agent.secondType == AgentType.HUMAN && agent.currentAction != AgentAction.DEATH)
            pickUpItems(agent as HumanAgent)
        // aim
        processAiming(agent, onGround, timeS)
        // fire
        processFiring(agent, onGround, timeS)
    }

    private fun processFiring(agent: AgentEntity, onGround: Boolean, timeS: Double) {
        val reload = agent.reloadTimeRemainingS - timeS
        agent.reloadTimeRemainingS = FastMath.max(reload, 0.0)
        if (agent.isFiring && agent.secondType == AgentType.HUMAN) {
            val human = agent as HumanAgent
            val weapon = human.weapon
            if (weapon != null && human.inventory.getAmmo(weapon) > 0 && (weapon.isAimMove || onGround)) {
                if (reload <= 0)
                    fireWeapon(human, timeS)
            } else
                agent.setFiring(false)
        }
    }

    /**
     * Only rotate aiming
     * @param onGround
     */
    private fun processAiming(agent: AgentEntity, onGround: Boolean, timeS: Double) {
        if (agent.secondType == AgentType.HUMAN) {
            val human = agent as HumanAgent
            val weapon = human.weapon
            // equipped weapon can aim
            if (weapon != null && weapon.isAimRotate) {
                val aiming = human.aiming
                if (!onGround && !weapon.isAimMove) {
                    // in the air and the weapon cannot work in the air
                    human.aimAngle = 0.0
                    human.aiming = EntityYDirection.NONE
                    val laser = human.laserSights
                    if (laser != null)
                        worldHolder.world.entityMap.removeEntity(laser)
                    human.laserSights = null
                } else {
                    if (aiming != EntityYDirection.NONE) {
                        // aiming
                        val add = aiming.vector.y * aimSpeedDS * FastMath.PI / 180.0 * timeS
                        var angle = human.aimAngle + add
                        if (angle < -FastMath.PI / 2.0)
                            angle = -FastMath.PI / 2.0
                        else if (angle > FastMath.PI / 2.0)
                            angle = FastMath.PI / 2.0
                        human.aimAngle = angle
                    }
                    if (weapon.laserSights != null)
                    // laser sight
                        processLaserSights(human)
                }
            }
        }
    }

    private fun processLaserSights(human: HumanAgent) {
        val laser = human.laserSights ?: return
        val weapon = human.weapon
        val dist = weapon.range
        var angle = human.aimAngle
        var pos = human.weaponCenter
        if (human.direction === EntityXDirection.LEFT) {
            angle = FastMath.PI - angle
            pos = Vector2D(-pos.x, pos.y)
        }
        pos = pos.add(human.pos)
        // create tmp projectile to simulate collision
        val projectile = BulletProjectile(EmptyModel.INSTANCE, null)
        projectile.pos = pos
        projectile.speed = Vector2D(FastMath.cos(angle), FastMath.sin(angle)).scalarMultiply(dist)
        projectile.remainingDist = dist
        // find collision
        val collision = ProjectileCollider(worldHolder.world, projectile, 1.0)
                .findNearestTileCollision(ProjectileHitTypes.TILE_TYPES)
        // get second position
        val pos2: Vector2D
        pos2 = when (collision) {
            null -> pos.add(projectile.speed)
            else -> collision.hitPos
        }
        // modify the laser
        laser.pos = pos
        laser.pos2 = pos2
    }

    /**
     * Fire weapon, remove ammo, add projectiles to the world, update reloading, ...
     * Can fail if projectile factory does not create a projectile because of environment.
     */
    private fun fireWeapon(human: HumanAgent, timeS: Double) {
        val weapon = human.weapon
        val reloadTimeS = weapon.reloadTimeS
        if (tryFireWeapon(human)) {
            human.inventory.removeOneAmmo(weapon)
            if (reloadTimeS >= 0)
                human.reloadTimeRemainingS = reloadTimeS
            else
                human.isFiring = false
        } else
            human.isFiring = false
    }

    /**
     * Try to fire the weapon, create projectiles, add them to the world.
     * Can fail only if the weapon cannot be fired because of environment (does not check ammo)
     * @return success
     */
    private fun tryFireWeapon(human: HumanAgent): Boolean {
        val weapon = human.weapon
        val entityMap = worldHolder.world.entityMap
        var lastProjectile: ProjectileEntity? = null
        // create projectiles
        for (i in 0 until weapon.projectileCount) {
            lastProjectile = projectileFactory.createProjectile(weapon, human)
            entityMap.addEntity(lastProjectile)
        }
        // shake weapon
        if (weapon.projectileCount == 1 && weapon.inaccuracyDg != 0.0) {
            var angle = VectorUtils.getAngle(lastProjectile!!.speed)
            if (human.direction === EntityXDirection.LEFT)
                angle = FastMath.PI - angle
            human.aimAngle = angle
        }
        return true
    }

    /**
     * Tries to pick up items with the agent.
     */
    private fun pickUpItems(agent: HumanAgent) {
        val agentBounds = agent.sizeBounds.move(agent.pos)
        for (entity in worldHolder.world.entityMap.getEntities(EntityType.ITEM)) {
            val itemBounds = entity.sizeBounds.move(entity.pos)
            val item = entity as ItemEntity
            if (agentBounds.intersects(itemBounds) && agent.capabilities.inventoryLimits.canAdd(agent.inventory, item))
                pickUpItem(agent, item)
        }
    }

    private fun pickUpItem(agent: HumanAgent, item: ItemEntity) {
        val inventory = agent.inventory
        if (item.secondType == ItemType.AMMO) {
            val ammo = item as AmmoItem
            if (ammo.weapon == null) {
                if (agent.weapon != null) {
                    inventory.addAmmo(agent.weapon, ammo.count)
                    worldHolder.world.entityMap.removeEntity(item)
                }
                return
            }
        }
        worldHolder.world.entityMap.removeEntity(item)
        inventory.addItem(item)
        // TODO equip weapon
    }

    companion object {
        private const val serialVersionUID = 4679035238019821173L
    }
}
