package secretAgent.world.entity

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.Tile

/**
 * Building on island map.
 *
 * @author Ondrej Milenovsky
 */
class BuildingUsable(model: ObjectModel, pos: Vector2D,
                     val finishedModel: String?,
                     val isFinalBuilding: Boolean) : UsableEntity(model, pos, true) {

    var levelId = -1

    override val secondType: UsableType?
        get() = UsableType.BUILDING
}

/**
 * Door that is opened by a key.
 *
 * @author Ondrej Milenovsky
 */
class DoorUsable(model: ObjectModel, pos: Vector2D,
                 lockType: String?) : UsableEntity(model, pos, true), LockedEntity {

    /** lock type, key with the type will open the door, null or "" means no key needed */
    override val lockType: String? = lockType?.takeUnless { it.isEmpty() }

    override val secondType: UsableType?
        get() = UsableType.DOOR

    /** if the door requires a key     */
    override val isLocked: Boolean
        get() = lockType != null

    override fun unlock() {} // nothing, the door will be removed
}

/**
 * Closed exit door.
 *
 * @author Ondrej Milenovsky
 */
class ExitDoorUsable(model: ObjectModel, pos: Vector2D,
                     val openModel: String?,
                     val replaceTiles: Map<TileId, Tile>) : UsableEntity(model, pos, true) {

    override val secondType: UsableType
        get() = UsableType.EXIT_DOOR
}

/**
 * Fence blocking the final level. This entity is not usable, will be removed automatically.
 *
 * @author Ondrej Milenovsky
 */
class FenceUsable(model: ObjectModel, pos: Vector2D) : UsableEntity(model, pos, true) {

    override val secondType: UsableType
        get() = UsableType.FENCE

    init {
        isActive = false
    }
}

/**
 * Teleport pointing to some position.
 *
 * @author Ondrej Milenovsky
 */
class TeleportUsable(model: ObjectModel, pos: Vector2D) : UsableEntity(model, pos, false) {

    var destination: Vector2D? = null

    override val secondType: UsableType
        get() = UsableType.TELEPORT
}


/**
 * Open exit door from the level.
 *
 * @author Ondrej Milenovsky
 */
class ExitUsable(model: ObjectModel, pos: Vector2D) : UsableEntity(model, pos, true) {

    override val secondType: UsableType
        get() = UsableType.EXIT
}

/**
 * Some usable entity that creates wall (door, building), drawn behind player and is a ghost.
 *
 * @author Ondrej Milenovsky
 */
abstract class UsableEntity(model: ObjectModel, pos: Vector2D,
                            /** false only if is single use and already activated  */
                            val isWall: Boolean) : Entity(model, pos) {

    var isActive = true

    override val type: EntityType
        get() = EntityType.USABLE

    abstract override val secondType: UsableType?
}

/**
 * Type of usable entities.
 *
 * @author Ondrej Milenovsky
 */
enum class UsableType : EntityType2 {
    /** door that is opened by a key  */
    DOOR,
    /** computer that controls laser  */
    EXIT_DOOR,
    /** exit door  */
    EXIT,
    /** teleport  */
    TELEPORT,
    /** building on island  */
    BUILDING,
    /** fence blocking the final level (not usable)  */
    FENCE
}