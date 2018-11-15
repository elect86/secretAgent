package secretAgent.io.map.orig.generator.entity

import cz.wa.secretagent.world.entity.item.*
import cz.wa.secretagent.world.weapon.Weapon
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.entity.*

/**
 * Creates ammo items.
 *
 * @author Ondrej Milenovsky
 */
class AmmoEntityCreator : EntityCreator<AmmoItem> {

    lateinit var weapons: Collection<Weapon>

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): AmmoItem? {
        var count: Int
        val arg0 = args.removeAt(0)
        try {
            count = Integer.parseInt(arg0)
        } catch (e: NumberFormatException) {
            logger.error("Wrong ammo count number: $arg0 for ammo: $tileId", e)
            return null
        }

        if (count <= 0) {
            logger.error("Ammo count must be > 0: $arg0 for ammo: $tileId")
            return null
        }
        var weapon: Weapon? = null
        if (args.isNotEmpty()) {
            val arg1 = args.removeAt(0)
            weapon = findWeapon(arg1)
            if (weapon == null)
                logger.error("Wrong weapon name: $arg1 for ammo: $tileId")
        }
        return AmmoItem(model, pos, weapon, count)
    }

    private fun findWeapon(name: String): Weapon? {
        for (weapon in weapons)
            if (weapon.name == name)
                return weapon
        return null
    }

    companion object {
        private const val serialVersionUID = -3881522195676956109L
        private val logger = LoggerFactory.getLogger(AmmoEntityCreator::class.java)
    }
}

/**
 * Creates items.
 *
 * @author Ondrej Milenovsky
 */
class ItemEntityCreator : TypeEntityCreator<ItemEntity>() {

    override fun getEnum(arg0: String): Any = ItemType.valueOf(arg0)

    companion object {
        private val serialVersionUID = -4281138502713589175L
    }
}

/**
 * Creates junk items.
 *
 * @author Ondrej Milenovsky
 */
class JunkEntityCreator : EntityCreator<JunkItem> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): JunkItem {
        var price = 0
        val arg0 = args.removeAt(0)
        try {
            price = Integer.parseInt(arg0)
        } catch (e: NumberFormatException) {
            logger.error("Wrong price number: $arg0 for junk: $tileId", e)
        }
        return JunkItem(model, pos, true, price)
    }

    companion object {
        private const val serialVersionUID = 2910402011157261915L
        private val logger = LoggerFactory.getLogger(JunkEntityCreator::class.java)
    }
}

/**
 * Creates keys.
 *
 * @author Ondrej Milenovsky
 */
class KeyEntityCreator : EntityCreator<KeyItem> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): KeyItem? {
        if (args.isEmpty())
            logger.warn("Not enough arguments for key: $tileId")
        else {
            val lockType = args.removeAt(0)
            if (lockType.isEmpty())
                logger.warn("Lock type is empty for key: $tileId")
            else
                return KeyItem(model, pos, lockType)
        }
        return null
    }

    companion object {
        private const val serialVersionUID = -7309784166465152356L
        private val logger = LoggerFactory.getLogger(KeyEntityCreator::class.java)
    }
}