package secretAgent.io.map.orig.generator.entity

import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.entity.*

/**
 * Creates movable can.
 *
 * @author Ondrej Milenovsky
 */
class MovableCanEntityCreator : EntityCreator<MovableCan> {

    lateinit var bounds: Rectangle2D

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): MovableCan =
            MovableCan(model, pos, bounds)

    companion object {
        private const val serialVersionUID = -6412487887560920954L
    }
}

/**
 * Creates platforms.
 *
 * @author Ondrej Milenovsky
 */
class PlatformEntityCreator : TypeEntityCreator<ItemEntity>() {

    override fun getEnum(arg0: String): Any = PlatformType.valueOf(arg0)

    companion object {
        private const val serialVersionUID = 7144916967720674852L
    }
}

/**
 * Creates moving platform.
 *
 * @author Ondrej Milenovsky
 */
class PlatformLiftEntityCreator : EntityCreator<PlatformLift> {

    lateinit var defaultDirection: EntityDirection
    var speed = 0.0
    lateinit var bounds: Rectangle2D

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): PlatformLift {
        var dir = defaultDirection
        if (args.isNotEmpty()) {
            val arg0 = args.removeAt(0)
            try {
                dir = EntityDirection.valueOf(arg0)
            } catch (e: IllegalArgumentException) {
                logger.error("Wrong direction: $args for lift: $tileId", e)
            }
        }
        return PlatformLift(model, pos, bounds, dir.vector.scalarMultiply(speed))
    }

    companion object {
        private const val serialVersionUID = -3975166234798772114L
        private val logger = LoggerFactory.getLogger(PlatformLiftEntityCreator::class.java)
    }
}