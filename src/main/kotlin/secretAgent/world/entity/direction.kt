package secretAgent.world.entity

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D

/**
 * Some direction.
 *
 * @author Ondrej Milenovsky
 */
interface Direction {
    val vector: Vector2D
}

/**
 * Direction of an entity.
 *
 * @author Ondrej Milenovsky
 */
enum class EntityDirection(override val vector: Vector2D) : Direction {
    NONE(Vector2D.ZERO),
    LEFT(Vector2D(-1.0, 0.0)),
    RIGHT(Vector2D(1.0, 0.0)),
    UP(Vector2D(0.0, -1.0)),
    DOWN(Vector2D(0.0, 1.0));

    val directionX: EntityXDirection
        get () = when (this) {
            NONE -> EntityXDirection.NONE
            LEFT -> EntityXDirection.LEFT
            RIGHT -> EntityXDirection.RIGHT
            else -> throw IllegalStateException("Direction is not x: $this")
        }

    val directionY: EntityYDirection
        get () = when (this) {
            NONE -> EntityYDirection.NONE
            UP -> EntityYDirection.UP
            DOWN -> EntityYDirection.DOWN
            else -> throw IllegalStateException("Direction is not y: $this")
        }
}

/**
 * Direction x of an entity.
 *
 * @author Ondrej Milenovsky
 */
enum class EntityXDirection(override val vector: Vector2D) : Direction {
    NONE(Vector2D.ZERO),
    LEFT(Vector2D(-1.0, 0.0)),
    RIGHT(Vector2D(1.0, 0.0));

    val direction: EntityDirection
        get() = when (this) {
            LEFT -> EntityDirection.LEFT
            RIGHT -> EntityDirection.RIGHT
            else -> EntityDirection.NONE
        }

    operator fun times(b: Double) = Vector2D(vector.x * b, vector.y * b)
}

/**
 * Direction y of an entity.
 *
 * @author Ondrej Milenovsky
 */
enum class EntityYDirection(override val vector: Vector2D) : Direction {
    NONE(Vector2D.ZERO),
    UP(Vector2D(0.0, -1.0)),
    DOWN(Vector2D(0.0, 1.0));

    val direction: EntityDirection
        get() = when (this) {
            UP -> EntityDirection.UP
            DOWN -> EntityDirection.DOWN
            else -> EntityDirection.NONE
        }

    operator fun times(b: Double) = Vector2D(vector.x * b, vector.y * b)
}