package secretAgent.game

import cz.wa.secretagent.world.entity.laser.LineLaser
import cz.wa.secretagent.world.entity.projectile.DynamiteProjectile
import cz.wa.secretagent.world.entity.projectile.ProjectileEntity
import cz.wa.secretagent.world.weapon.Weapon
import cz.wa.secretagent.worldinfo.WorldHolder
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.slf4j.LoggerFactory
import secretAgent.plus
import secretAgent.view.model.ExplosionModel
import secretAgent.view.model.LaserModel
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.Explosion
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentType
import java.awt.Color
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable
import kotlin.random.Random

/**
 * Factory that creates projectiles fired from weapons.
 * Also creates lasers and explosions.
 *
 * @author Ondrej Milenovsky
 */
class ProjectileFactory : Serializable {

    var worldHolder: WorldHolder? = null

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(stream: ObjectInputStream) = stream.defaultReadObject()

    /**
     * Creates projectile fired by the agent.
     * @param weapon weapon from which is the projectile fired
     * @param agent agent who fired the projectile
     * @return new projectile, can be null if the agent cannot fire (place mine in the air)
     */
    fun createProjectile(weapon: Weapon, agent: AgentEntity): ProjectileEntity {
        val projectile = createEntity(weapon, agent)
        // position
        projectile.pos = agent.pos + agent.weaponCenter
        // speed
        val spd = weapon.projectileSpeed
        var angle = 0.0
        if (agent.secondType == AgentType.HUMAN)
            angle = (agent as HumanAgent).aimAngle
        if (agent.direction === EntityXDirection.LEFT)
            angle = FastMath.PI - angle
        if (weapon.inaccuracyDg != 0.0)
            angle += (Random.nextDouble() - 0.5) * 2.0 * weapon.inaccuracyDg * FastMath.PI / 180

        val speed = Vector2D(spd * FastMath.cos(angle), spd * FastMath.sin(angle))
        projectile.speed = speed
        // other parameters
        projectile.damage = weapon.projectileDamage
        projectile.remainingDist = weapon.range
        return projectile
    }

    /**
     * Creates projectile entity without any non final parameters.
     */
    private fun createEntity(weapon: Weapon, agent: AgentEntity): ProjectileEntity {
        val model = worldHolder!!.graphicsInfo.getModel(weapon.projectileModelName)
        val type = weapon.projectileType
        return type.createEntity(model, agent.team)
    }

    /**
     * Creates dynamite at the position
     * @param pos center position
     * @return new dynamite entity
     */
    fun createDynamite(pos: Vector2D): DynamiteProjectile {
        val model = worldHolder!!.graphicsInfo.getModel(DYNAMITE_MODEL)
        return DynamiteProjectile(model, pos, DYNAMITE_FUSE)
    }

    /**
     * Creates laser defined by two points with no damage.
     * @param pos1 point 1
     * @param pos2 point 2
     * @param color color
     * @return new laser entity
     */
    fun createLaserSight(pos1: Vector2D, pos2: Vector2D, color: Color): LineLaser? {
        val model1 = worldHolder!!.graphicsInfo.getModel(LASER_SIGHT_MODEL)
        if (model1 !is LaserModel) {
            logger.warn("Model '$LASER_SIGHT_MODEL' must be laser model")
            return null
        }
        val model = model1.copyWithColor(color)
        return LineLaser(model, pos1, null, 0.0, pos2, LASER_SIGHT_WIDTH, false)
    }

    /**
     * Creates explosion. (does not add to the world)
     * @param pos center position
     * @param modelName model name
     * @param radius radius
     * @param damage damage in center
     * @param durationS duration in seconds
     * @return the explosion entity
     */
    fun createExplosion(pos: Vector2D, modelName: String, radius: Double, damage: Double,
                        durationS: Double): Explosion? {
        val model = worldHolder!!.graphicsInfo.getModel(modelName)
        if (model !is ExplosionModel) {
            logger.warn("Model '$modelName' must be explosion model")
            return null
        }
        return Explosion(model, pos, radius, damage, durationS)
    }

    companion object {
        private const val serialVersionUID = -6967087863987254819L

        private val logger = LoggerFactory.getLogger(ProjectileFactory::class.java)

        private const val DYNAMITE_MODEL = "dynamite"
        private const val DYNAMITE_FUSE = 3.0

        private const val LASER_SIGHT_MODEL = "laserSight"
        private const val LASER_SIGHT_WIDTH = 2.0
    }
}
