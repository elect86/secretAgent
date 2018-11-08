package cz.wa.secretagent.world.entity.agent.capabilities;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.wa.secretagent.world.entity.agent.inventory.AgentInventory;
import cz.wa.secretagent.world.entity.item.ItemEntity;
import cz.wa.secretagent.world.entity.item.ItemType;
import cz.wa.secretagent.world.weapon.Weapon;

/**
 * Mutable limitations for inventory. Mostly defines weapons.
 * 
 * @author Ondrej Milenovsky
 */
public class InventoryLimits implements Serializable {
    private static final long serialVersionUID = 1682490782394782061L;

    /** cannot carry anything */
    public static final InventoryLimits NOTHING = new InventoryLimits();

    /** max number of weapons */
    private int maxWeapons;
    /** set of weapons that can carry, null means no limits */
    private Set<Weapon> possibleWeapons;
    /** max ammo for weapons, null means no limits */
    private Map<Weapon, Integer> maxAmmo;
    /** items types that can pick up, null means no limits */
    private Set<ItemType> picksItems;

    public InventoryLimits() {
        maxWeapons = 0;
        maxAmmo = new HashMap<Weapon, Integer>();
        possibleWeapons = new HashSet<Weapon>();
        picksItems = new HashSet<ItemType>();
    }

    public InventoryLimits(int maxWeapons, Set<Weapon> possibleWeapons, Map<Weapon, Integer> maxAmmo,
            Set<ItemType> picksItems) {
        this.maxWeapons = maxWeapons;
        this.possibleWeapons = possibleWeapons;
        this.maxAmmo = maxAmmo;
        this.picksItems = picksItems;
    }

    public Map<Weapon, Integer> getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(Map<Weapon, Integer> maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getMaxWeapons() {
        return maxWeapons;
    }

    public void setMaxWeapons(int maxWeapons) {
        this.maxWeapons = maxWeapons;
    }

    public Set<Weapon> getPossibleWeapons() {
        return possibleWeapons;
    }

    public void setPossibleWeapons(Set<Weapon> possibleWeapons) {
        this.possibleWeapons = possibleWeapons;
    }

    public Set<ItemType> getPicksItems() {
        return picksItems;
    }

    public void setPicksItems(Set<ItemType> picksItems) {
        this.picksItems = picksItems;
    }

    /**
     * @param inventory inventory
     * @param item item to add
     * @return true if the item can fit in the inventory
     */
    public boolean canAdd(AgentInventory inventory, ItemEntity item) {
        if ((picksItems != null) && (!picksItems.contains(item.getSecondType()))) {
            return false;
        }
        // TODO can add item
        return true;
    }

    /**
     * @return list of items that cannot be in the inventory (they are removed from the inventory)
     */
    public List<ItemEntity> removeOverloadedItems(AgentInventory inventory) {
        // TODO overloaded items
        return Collections.emptyList();
    }
}
