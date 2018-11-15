package secretAgent.world.entity

import cz.wa.secretagent.world.weapon.Weapon
import org.apache.commons.lang.Validate
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel

/**
 * Ammo for specific or current weapon.
 *
 * @author Ondrej Milenovsky
 */
open class AmmoItem(model: ObjectModel, pos: Vector2D,
               /** weapon or null if for current weapon                */
               val weapon: Weapon?,
               val count: Int) : ItemEntity(model, pos) {

    override val secondType: ItemType?
        get() = ItemType.AMMO
}

/**
 * Pickable item
 *
 * @author Ondrej Milenovsky
 */
abstract class ItemEntity(model: ObjectModel, pos: Vector2D) : Entity(model, pos) {

    override val type: EntityType
        get() = EntityType.ITEM

    abstract override val secondType: ItemType?
}

/**
 * Type of item.
 *
 * @author Ondrej Milenovsky
 */
enum class ItemType : EntityType2 {
    JUNK,
    KEY,
    AMMO,
    WEAPON,
    POWER_UP
}

/**
 * Item that is sold for cash.
 *
 * @author Ondrej Milenovsky
 */
class JunkItem(model: ObjectModel, pos: Vector2D,
               enemyPick: Boolean, val price: Int) : ItemEntity(model, pos) {

    override val secondType: ItemType
        get() = ItemType.JUNK
}

/**
 * Key for a door, floppy or dynamite.
 *
 * @author Ondrej Milenovsky
 */
class KeyItem(model: ObjectModel, pos: Vector2D,
              /** lock type, will open door with same type */
              val lockType: String) : ItemEntity(model, pos) {

    override val secondType: ItemType
        get() = ItemType.KEY

    init {
        assert(lockType.isNotEmpty()) {"lockType is empty"}
    }
}

/**
 * Power up (speed up)
 *
 * @author Ondrej Milenovsky
 */
class PowerUpItem(model: ObjectModel, pos: Vector2D)
    : ItemEntity(model, pos) { // TODO Auto-generated constructor stub

    override val secondType: ItemType
        get() = ItemType.POWER_UP
}


/**
 * Weapon item, can contain ammo.
 *
 * @author Ondrej Milenovsky
 */
class WeaponItem(model: ObjectModel, pos: Vector2D, weapon: Weapon, ammoCount: Int) :
        AmmoItem(model, pos, weapon, ammoCount) {

    override val secondType: ItemType
        get() = ItemType.WEAPON
}