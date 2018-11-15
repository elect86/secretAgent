package secretAgent.world.entity

import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel
import secretAgent.world.entity.agent.Team

/**
 * Some laser (security, laser gun projectile, weapon laser sight).
 * Can damage agents from different team or do no damage.
 *
 * @author Ondrej Milenovsky
 */
abstract class LaserEntity(model: ObjectModel, pos: Vector2D,
                           /** team (same as projectile team)  */
                           val team: Team?,
                           /** damage per second to agents from different team  */
                           var damage: Double,
                           /** if is security laser  */
                           val isLevelLaser: Boolean) : Entity(model, pos) {

    override val type: EntityType
        get() = EntityType.LASER

    abstract override val secondType: LaserType?
}

/**
 * Type of laser
 *
 * @author Ondrej Milenovsky
 */
enum class LaserType : EntityType2 {
    /** perpendicular rectangular laser  */
    RECTANGULAR,
    /** laser defined by any two points  */
    LINE
}

/**
 * Laser defined by two points.
 *
 * @author Ondrej Milenovsky
 */
class LineLaser(model: ObjectModel, pos: Vector2D,
                team: Team?, damage: Double,
                /** second point of the laser  */
                var pos2: Vector2D, val width: Double,
                levelLaser: Boolean) : LaserEntity(model, pos, team, damage, levelLaser) {

    override val secondType: LaserType
        get() = LaserType.LINE
}

/**
 * Rectangular perpendicular laser.
 *
 * @author Ondrej Milenovsky
 */
class RectLaser(model: ObjectModel, pos: Vector2D,
                team: Team?, damage: Double,
                sizeBounds: Rectangle2D, levelLaser: Boolean) :
        LaserEntity(model, pos, team, damage, levelLaser) {

    override val secondType: LaserType
        get() = LaserType.RECTANGULAR

    init {
        super.sizeBounds = sizeBounds
    }
}
