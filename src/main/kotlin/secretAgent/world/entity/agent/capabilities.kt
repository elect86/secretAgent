package secretAgent.world.entity.agent

import cz.wa.secretagent.world.weapon.Weapon
import secretAgent.world.entity.ItemEntity
import secretAgent.world.entity.ItemType
import java.io.Serializable

/**
 * Capabilities of an agent's actions, immutable implementation.
 *
 * @author Ondrej Milenovsky
 */
class AgentCapabilities(
        /**
         * @param maxSpeed max move speed [pixel/s]
         * @param jumpStrength jump strength
         * @param jumpTimeS max jump duration [s]
         * @param canAim can aim weapon
         * @param canActivate can activate
         * @param inventoryLimits inventory limits, if null, then cannot pick anything
         */
        val maxHealth: Double,
        /** max move speed  */
        val maxSpeed: Double,
        /** jump strength, 0 = cannot jump  */
        val jumpStrength: Double,
        /** jump time, 0 = cannot jump  */
        val jumpTimeS: Double,
        /** if can aim up and down  */
        val canAim: Boolean,
        /** if can activate doors, defuse mines, ...  */
        val canActivate: Boolean, inventoryLimits: InventoryLimits?) : Serializable {

    /** limits for inventory  */
    val inventoryLimits: InventoryLimits = inventoryLimits ?: InventoryLimits.NOTHING

    constructor(c: AgentCapabilitiesTmp) : this(c.maxHealth, c.maxSpeed, c.jumpStrength, c.jumpTimeS, c.isCanAim,
            c.isCanActivate, c.inventoryLimits)

    companion object {
        private const val serialVersionUID = -6591972558599041031L
    }
}

/**
 * Capabilities of an agent's actions, mutable temporary implementation used in spring, cannot be used by agent.
 *
 * @author Ondrej Milenovsky
 */
class AgentCapabilitiesTmp {
    var maxHealth: Double = 0.0
    /** max move speed  */
    var maxSpeed: Double = 0.0
    /** jump strength, 0 = cannot jump  */
    var jumpStrength: Double = 0.0
    /** jump time in seconds, 0 = cannot jump  */
    var jumpTimeS: Double = 0.0
    /** if can aim up and down  */
    var isCanAim: Boolean = false
    /** if can activate doors, defuse mines, ...  */
    var isCanActivate: Boolean = false
    /** inventory limits, null if cannot pick up items  */
    var inventoryLimits: InventoryLimits? = null
}

/**
 * Mutable limitations for inventory. Mostly defines weapons.
 *
 * @author Ondrej Milenovsky
 */
class InventoryLimits(
        /** max number of weapons  */
    var maxWeapons: Int = 0,
        /** set of weapons that can carry, null means no limits  */
    var possibleWeapons: Set<Weapon> = HashSet(),
        /** max ammo for weapons, null means no limits  */
    var maxAmmo: Map<Weapon, Int> = HashMap(),
        /** items types that can pick up, null means no limits  */
    var picksItems: MutableSet<ItemType>? = HashSet()) : Serializable {

    /**
     * @param inventory inventory
     * @param item item to add
     * @return true if the item can fit in the inventory
     */
    fun canAdd(inventory: AgentInventory, item: ItemEntity): Boolean {
        return if (picksItems != null && !picksItems!!.contains(item.secondType)) {
            false
        } else true
        // TODO can add item
    }

    /**
     * @return list of items that cannot be in the inventory (they are removed from the inventory)
     */
    fun removeOverloadedItems(inventory: AgentInventory): List<ItemEntity> {
        // TODO overloaded items
        return emptyList()
    }

    companion object {
        private const val serialVersionUID = 1682490782394782061L
        /** cannot carry anything  */
        val NOTHING = InventoryLimits()
    }
}