package secretAgent.world.entity.agent

import cz.wa.secretagent.world.entity.item.AmmoItem
import cz.wa.secretagent.world.entity.item.ItemEntity
import cz.wa.secretagent.world.entity.item.ItemType
import cz.wa.secretagent.world.entity.item.WeaponItem
import cz.wa.secretagent.world.weapon.Weapon
import org.apache.commons.lang.Validate
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.LinkedHashSet

/**
 * Backpack of an agent. Can contain weapons, ammo and items.
 * Weapons and ammo are independent, can carry ammo but not the weapon.
 *
 * @author Ondrej Milenovsky
 */
class AgentInventory {

    /** item type -> set of items  */
    private val items: MutableMap<ItemType, ArrayList<ItemEntity>> = LinkedHashMap()
    /** weapon -> ammo  */
    private val ammo: MutableMap<Weapon, Int> = LinkedHashMap()
    /** all weapon  */
    val weapons: MutableSet<Weapon> = LinkedHashSet()

    /**
     * @param itemType type
     * @return list of all items of the type
     */
    fun getItems(itemType: ItemType): List<ItemEntity> = items[itemType] ?: emptyList()

    /**
     * Adds the item to the inventory.
     * If the item is ammo or weapon, the item is not stored as the item but as ammo or weapon.
     * (the inventory will not contain the item)
     * @param item item to add
     */
    fun addItem(item: ItemEntity) {
        item.pos = Vector2D.NaN
        item.speed = Vector2D.ZERO
        val type = item.secondType
        when (type) {
            ItemType.WEAPON -> weapons.add((item as WeaponItem).weapon)
            ItemType.AMMO -> {
                val ammoItem = item as AmmoItem
                addAmmo(ammoItem.weapon, ammoItem.count)
            }
            // item
            else -> items.getOrPut(type) { ArrayList(3) } += item
        }
    }

    /**
     * Removes the item from the inventory
     * @param item item to remove
     * @throws IllegalArgumentException if the item is not in the inventory
     */
    fun removeItem(item: ItemEntity) {
        val type = item.secondType
        val list = items[type]
        if (list == null || !list.remove(item))
            throw IllegalArgumentException("Item is not in the inventory: $item")
        if (list.isEmpty())
            items -= type
    }

    /**
     * Adds ammo for the weapon
     * @param weapon weapon
     * @param count ammo count
     */
    fun addAmmo(weapon: Weapon, count: Int) {
        Validate.isTrue(count > 0, "ammo count must be > 0")
        var curr = 0
        if (weapon in ammo)
            curr = ammo[weapon]!!
        curr += count
        ammo[weapon] = curr
    }

    fun addWeapon(weapon: Weapon) = weapons.add(weapon)

    /**
     * @param weapon weapon to remove
     * @throws IllegalArgumentException if the weapon is not in the inventory
     */
    fun removeWeapon(weapon: Weapon) {
        if (!weapons.remove(weapon))
            throw IllegalArgumentException("weapon is not in the inventory")
    }

    /**
     * @param weapon weapon
     * @return ammo count for the weapon
     */
    fun getAmmo(weapon: Weapon) = ammo[weapon] ?: 0

    /**
     * Reduces ammo count for the weapon by 1
     * @param weapon
     * @throws IllegalStateException if there is no ammo
     */
    fun removeOneAmmo(weapon: Weapon) {
        ammo[weapon]?.let {
            val count = it - 1
            if (count == 0)
                ammo -= weapon
            else
                ammo[weapon] = count
        } ?: throw IllegalStateException("No ammo for $weapon")
    }
}