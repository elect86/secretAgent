package secretAgent.game.player

import cz.wa.secretagent.world.entity.item.ItemEntity
import cz.wa.secretagent.world.weapon.Weapon
import secretAgent.world.entity.agent.AgentInventory
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

/**
 * Class holding weapons and ammo of an player.
 *
 * @author Ondrej Milenovsky
 */
class PlayerWeapons : Serializable {

    /** weapon -> ammo  */
    var ammo: Map<Weapon, Int> = HashMap()
    /** all weapon  */
    var weapons: Set<Weapon> = HashSet()
    /** special items that are carried outside buildings  */
    var items: List<ItemEntity> = ArrayList()

    /**
     * Adds all the weapons and items to the inventory (does not remove from this object)
     * @param inventory
     */
    fun loadToInventory(inventory: AgentInventory) {
        for ((key, value) in ammo)
            inventory.addAmmo(key, value)
        for (weapon in weapons)
            inventory.addWeapon(weapon)
        for (item in items)
            inventory.addItem(item)
    }

    fun deepCopy(): PlayerWeapons {
        val ret = PlayerWeapons()
        ret.ammo += ammo
        ret.weapons += weapons
        ret.items += items
        return ret
    }

    companion object {
        private const val serialVersionUID = -7097475203090982225L
    }
}
