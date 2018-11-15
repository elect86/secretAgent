package secretAgent.game.action

import cz.wa.secretagent.world.weapon.Weapon
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.game.ProjectileFactory
import secretAgent.game.utils.EntityObserver
import secretAgent.plus
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.EntityYDirection
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentType

/**
 * Action to control agent in level map.
 *
 * @author Ondrej Milenovsky
 */
class AgentLevelAction : AgentAction<AgentEntity>() {

    private lateinit var activateAction: AgentActivateAction
    private lateinit var posSensor: EntityObserver
    private lateinit var projectileFactory: ProjectileFactory

    private var canRepeatFire = false
    private var fireDown = false

    /**
     * @return if currently aiming weapon and cannot move
     */
    private val isAimingWeapon: Boolean
        get() = when {
            fireDown && agent!!.secondType == AgentType.HUMAN -> {
                val agent = agent as HumanAgent
                val weapon = agent.weapon
                weapon != null && weapon.isAimRotate && !weapon.isAimMove && agent.reloadTimeRemainingS <= 0
            }
            else -> false
        }

    override fun init() {
        activateAction = actionFactory!!.getAction(AgentActivateAction::class.java)
        posSensor = EntityObserver(agent!!, world!!)
        projectileFactory = actionFactory!!.projectileFactory!!
    }

    fun moveX(dir: EntityXDirection) {
        val agent = agent!!
        if (agent.isControllable) {
            agent.moveSpeed = when {
                isAimingWeapon -> Vector2D.ZERO
                else -> {
                    val maxSpeed = agent.capabilities.maxSpeed
                    Vector2D(0.0, agent.moveSpeed.y) + dir * maxSpeed
                }
            }
            if (dir != EntityXDirection.NONE)
                agent.direction = dir
        }
    }

    fun aimY(dir: EntityYDirection) {
        val agent = agent!!
        if (agent.isControllable && agent.capabilities.canAim && agent.secondType == AgentType.HUMAN) {
            val human = agent as HumanAgent
            val weapon = human.weapon ?: return
            human.aiming = when {
                weapon.isAimRotate -> when {
                    fireDown && weapon.isAimRotate && human.reloadTimeRemainingS <= 0 -> {
                        processLaserSight(human, weapon)
                        dir
                    }
                    else -> EntityYDirection.NONE
                }
                else -> {
                    human.aimAngle = when {
                        dir == EntityYDirection.UP -> -FastMath.PI / 2.0
                        dir == EntityYDirection.DOWN && !posSensor.isOnGround -> FastMath.PI / 2.0
                        else -> 0.0
                    }
                    dir
                }
            }
        }
    }

    private fun processLaserSight(human: HumanAgent, weapon: Weapon) {
        if (weapon.laserSights != null && human.laserSights == null) {
            // has no laser but needs one, create it but collapsed (might be immediately removed)
            val pos = human.pos
            val laser = projectileFactory.createLaserSight(pos, pos, weapon.laserSights)
            // use the laser
            human.laserSights = laser
            world!!.entityMap.addEntity(laser)
        }
    }

    fun jump(b: Boolean) {
        if (agent!!.secondType == AgentType.HUMAN) {
            val agent = agent as HumanAgent
            val jumpStrength = agent.capabilities.jumpStrength
            if (agent.isControllable && jumpStrength > 0 && !isAimingWeapon) {
                agent.isJumping = when {
                    b && (agent.isJumping || posSensor.isOnGround) -> {
                        if (!agent.isJumping)
                            agent.jumpRemainingS = agent.capabilities.jumpTimeS
                        true
                    }
                    else -> false
                }
            }
        }
    }

    fun activate(b: Boolean) {
        if (!b) {
            activateAction.stopUsing()
            return
        }
        val agent = agent!!
        if (agent.isControllable) {
            if (agent.secondType == AgentType.HUMAN) {
                val human = agent as HumanAgent
                if (fireDown && human.weapon?.isAimRotate == true) {
                    // cancel aiming
                    human.aimAngle = 0.0
                    human.aiming = EntityYDirection.NONE
                    removeLaserSight(human)
                    fireDown = false
                    canRepeatFire = false
                    return
                }
            }
            if (agent.capabilities.canActivate)
                agent.entityToUse?.let(activateAction::useEntity)
        }
    }

    fun fire(b: Boolean) {
        val agent = agent!!
        if (!b) {
            canRepeatFire = true
        }
        if (agent.isControllable && agent.secondType == AgentType.HUMAN) {
            val human = agent as HumanAgent
            val weapon = human.weapon
            if (weapon == null) {
                human.isFiring = false
                return
            }
            if (weapon.isAimRotate) {
                if (!b) // aiming weapon and not holding the trigger
                    if (fireDown)
                        fireAimedWeapon(human)  // fire
                    else {
                        // reset rotating weapon
                        human.isFiring = false
                        human.aimAngle = 0.0
                    }
                else if (!canRepeatFire)
                    return
            } else if (b && human.inventory.getAmmo(weapon) > 0) {
                // check repeat fire weapon
                if (weapon.isAimRotate)
                    human.isFiring = false
                else if (canRepeatFire || weapon.reloadTimeS >= 0) {
                    fireAimedWeapon(human)
                    canRepeatFire = false
                } else
                    human.isFiring = false
            } else
                human.isFiring = false
            fireDown = b
        }
    }

    private fun fireAimedWeapon(human: HumanAgent) {
        human.isFiring = true
        removeLaserSight(human)
    }

    private fun removeLaserSight(human: HumanAgent) {
        val laser = human.laserSights
        if (laser != null) {
            world!!.entityMap.removeEntity(laser)
            human.laserSights = null
        }
    }

    fun switchWeapon(weapon: Weapon?) {
        if (agent!!.isControllable) {
            val agent = agent as HumanAgent
            if (agent.isFiring || agent.reloadTimeRemainingS > 0) {
                return
            }
            val weapons = agent.inventory.weapons
            if (weapon == null || weapons.contains(weapon)) {
                agent.aimAngle = 0.0
                agent.weapon = weapon
            }
        }
    }

    fun dropWeapon() {
        if (agent!!.isControllable) {
            // TODO drop weapon
        }
    }
}