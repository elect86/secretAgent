package secretAgent.world.entity

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel

/**
 * Explosion, has no second type, there is only one explosion.
 *
 * @author Ondrej Milenovsky
 */
class Explosion(model: ObjectModel, pos: Vector2D,
                val radius: Double,
                val damage: Double,
                val durationS: Double) : Entity(model, pos) {

    var timeS = 0.0
        private set

    override val type: EntityType
        get() = EntityType.EXPLOSION

    override val secondType: EntityType2?
        get() = null

    fun addTime(timeS: Double) {
        this.timeS += timeS
    }
}