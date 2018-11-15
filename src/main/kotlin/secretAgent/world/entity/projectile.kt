package secretAgent.world.entity

import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.VectorUtils
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel
import secretAgent.world.entity.agent.Team

/**
 * Bullet that falls as it flies.
 *
 * @author Ondrej Milenovsky
 */
class BulletFallProjectile(model: ObjectModel, team: Team) : ProjectileEntity(model, Vector2D.ZERO, team) {

    override val secondType: ProjectileType?
        get() = ProjectileType.BULLET_FALL
}

/**
 * Simple bullet flying single direction.
 *
 * @author Ondrej Milenovsky
 */
class BulletProjectile(model: ObjectModel, team: Team?) : ProjectileEntity(model, Vector2D.ZERO, team) {

    override val secondType: ProjectileType?
        get() = ProjectileType.BULLET
}

/**
 * Dynamite that explodes and opens exit door.
 *
 * @author Ondrej Milenovsky
 */
class DynamiteProjectile(model: ObjectModel, pos: Vector2D, fuseTimeS: Double) : ProjectileEntity(model, pos, null) {

    /** remaining fuse time in seconds  */
    var remainingTimeS: Double = 0.toDouble()
        private set

    override val secondType: ProjectileType?
        get() = ProjectileType.DYNAMITE

    init {
        isStaticPos = true
        remainingTimeS = fuseTimeS
    }

    fun addTime(timeS: Double) {
        remainingTimeS -= timeS
    }

}

/**
 * Temporary class representing security laser. Will be replaced by laser entity.
 *
 * @author Ondrej Milenovsky
 */
class LevelLaserProjectile(model: ObjectModel, pos: Vector2D) : ProjectileEntity(model, pos, null) {

    override val secondType: ProjectileType?
        get() = ProjectileType.LEVEL_LASER

}

/**
 * Fired projectile, mine or spikes
 *
 * @author Ondrej Milenovsky
 */
abstract class ProjectileEntity(model: ObjectModel, pos: Vector2D,
                                /** team of agent who fired it  */
                                val team: Team?) : Entity(model, pos), HasModelAngle {
    /** impact damage (not explosion damage)  */
    var damage = 0.0
    /** distance to fly before it disappears  */
    var remainingDist: Double = 0.toDouble()
    /** angle of model  */
    override var modelAngle: Double = 0.toDouble()
        get() {
            if (field.isNaN())
                field = VectorUtils.getAngle(speed)
            return field
        }

    override val type: EntityType
        get() = EntityType.PROJECTILE

    abstract override val secondType: ProjectileType?

    init {
        isStaticPos = false
        sizeBounds = Rectangle2D.ZERO
    }

    override var speed: Vector2D
        get() = super.speed
        set(value) {
            super.speed = value
            modelAngle = Double.NaN
        }
}

/**
 * Type of projectile
 *
 * @author Ondrej Milenovsky
 */
enum class ProjectileType : EntityType2 {
    /** any bullet that flies single in direction and impacts on agents or walls  */
    BULLET {
        override fun createEntity(model: ObjectModel, team: Team): ProjectileEntity =
                BulletProjectile(model, team)
    },
    /** bullet that falls  */
    BULLET_FALL {
        override fun createEntity(model: ObjectModel, team: Team): ProjectileEntity =
                BulletFallProjectile(model, team)
    },
    /** same as bullet but on impact creates an explosion  */
    ROCKET {
        override fun createEntity(model: ObjectModel, team: Team): ProjectileEntity? =
                RocketProjectile(model, team)
    },
    /** grenade with physics, explodes after some time  */
    GRENADE {
        // TODO Auto-generated method stub
        override fun createEntity(model: ObjectModel, team: Team): ProjectileEntity? = null
    },
    /** static mine, explodes if any agent steps on it  */
    MINE {
        // TODO Auto-generated method stub
        override fun createEntity(model: ObjectModel, team: Team): ProjectileEntity? = null
    },
    /** ball that bounces from the floor  */
    BALL {
        // TODO Auto-generated method stub
        override fun createEntity(model: ObjectModel, team: Team): ProjectileEntity? = null
    },
    /** dynamite that opens the exit, creates explosion  */
    DYNAMITE,
    /** security laser, will not get to the game, will be replaced by laser entity  */
    LEVEL_LASER;

    /**
     * Creates projectile entity with model and team and nothing else.
     * @param model the model
     * @param team team of agent who fired it (can be null)
     * @return new entity
     * @throws UnsupportedOperationException if the type cannot create entity (DYNAMITE, LEVEL_LASER)
     */
    open fun createEntity(model: ObjectModel, team: Team): ProjectileEntity? =
        throw UnsupportedOperationException("This projectile cannot be created from its type.")
}

/**
 * Rocket that flies like bullet and then explodes.
 *
 * @author Ondrej Milenovsky
 */
class RocketProjectile(model: ObjectModel, team: Team) : ProjectileEntity(model, Vector2D.ZERO, team) {

    override val secondType: ProjectileType
        get() = ProjectileType.ROCKET
}
