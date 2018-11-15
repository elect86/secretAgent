package secretAgent.world.entity

import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel

/**
 * Can that can be pushed and stepped on.
 *
 * @author Ondrej Milenovsky
 */
class MovableCan(model: ObjectModel,
                 pos: Vector2D,
                 bounds: Rectangle2D) : PlatformEntity(model, pos, bounds) {

    override val secondType: PlatformType?
        get() = PlatformType.CAN
}

/**
 * Moving platform (elevator).
 *
 * @author Ondrej Milenovsky
 */
abstract class PlatformEntity(model: ObjectModel,
                              pos: Vector2D,
                              bounds: Rectangle2D) : Entity(model, pos, bounds, Vector2D.ZERO, false) {

    override val type: EntityType
        get() = EntityType.PLATFORM

    abstract override val secondType: PlatformType?
}

/**
 * Moving platform.
 *
 * @author Ondrej Milenovsky
 */
class PlatformLift(model: ObjectModel,
                   pos: Vector2D,
                   bounds: Rectangle2D,
                   speed: Vector2D) : PlatformEntity(model, pos, bounds) {

    var isMovingForward: Boolean = false

    override val secondType: PlatformType
        get() = PlatformType.LIFT

    init {
        super.speed = speed
    }
}

/**
 * Type of platform.
 *
 * @author Ondrej Milenovsky
 */
enum class PlatformType : EntityType2 {
    LIFT,
    CAN
}
