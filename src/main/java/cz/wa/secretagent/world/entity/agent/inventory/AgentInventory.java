package cz.wa.secretagent.world.entity.agent.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.entity.item.AmmoItem;
import cz.wa.secretagent.world.entity.item.ItemEntity;
import cz.wa.secretagent.world.entity.item.ItemType;
import cz.wa.secretagent.world.entity.item.WeaponItem;
import cz.wa.secretagent.world.weapon.Weapon;

/**
 * Backpack of an agent. Can contain weapons, ammo and items.
 * Weapons and ammo are independent, can carry ammo but not the weapon. 
 * 
 * @author Ondrej Milenovsky
 */
public class AgentInventory {

    /** item type -> set of items */
    private final Map<ItemType, List<ItemEntity>> items;
    /** weapon -> ammo */
    private final Map<Weapon, Integer> ammo;
    /** all weapon */
    private final Set<Weapon> weapons;

    public AgentInventory() {
        items = new LinkedHashMap<ItemType, List<ItemEntity>>();
        ammo = new LinkedHashMap<Weapon, Integer>();
        weapons = new LinkedHashSet<Weapon>();
    }

    /**
     * @param itemType type
     * @return list of all items of the type 
     */
    public List<ItemEntity> getItems(ItemType itemType) {
        if (items.containsKey(itemType)) {
            return items.get(itemType);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Adds the item to the inventory.
     * If the item is ammo or weapon, the item is not stored as the item but as ammo or weapon.
     * (the inventory will not contain the item)
     * @param item item to add
     */
    public void addItem(ItemEntity item) {
        item.setPos(Vector2D.NaN);
        item.setSpeed(Vector2D.ZERO);
        ItemType type = item.getSecondType();
        if (type == ItemType.WEAPON) {
            // weapon
            weapons.add(((WeaponItem) item).getWeapon());
        } else if (type == ItemType.AMMO) {
            // ammo
            AmmoItem ammoItem = (AmmoItem) item;
            addAmmo(ammoItem.getWeapon(), ammoItem.getCount());
        } else {
            // item
            List<ItemEntity> list;
            if (items.containsKey(type)) {
                list = items.get(type);
            } else {
                list = new ArrayList<ItemEntity>(3);
                items.put(type, list);
            }
            list.add(item);
        }
    }

    /**
     * Removes the item from the inventory 
     * @param item item to remove
     * @throws IllegalArgumentException if the item is not in the inventory
     */
    public void removeItem(ItemEntity item) {
        ItemType type = item.getSecondType();
        if (!items.containsKey(type) || !items.get(type).remove(item)) {
            throw new IllegalArgumentException("Item is not in the inventory: " + item);
        }
        if (items.get(type).isEmpty()) {
            items.remove(type);
        }
    }

    /**
     * @return mutable set of all weapons
     */
    public Set<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Adds ammo for the weapon
     * @param weapon weapon
     * @param count ammo count
     */
    public void addAmmo(Weapon weapon, int count) {
        Validate.isTrue(count > 0, "ammo count must be > 0");
        int curr = 0;
        if (ammo.containsKey(weapon)) {
            curr = ammo.get(weapon);
        }
        curr += count;
        ammo.put(weapon, curr);
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    /**
     * @param weapon weapon to remove
     * @throws IllegalArgumentException if the weapon is not in the inventory
     */
    public void removeWeapon(Weapon weapon) {
        if (!weapons.remove(weapon)) {
            throw new IllegalArgumentException("weapon is not in the inventory");
        }
    }

    /**
     * @param weapon weapon
     * @return ammo count for the weapon
     */
    public int getAmmo(Weapon weapon) {
        if (ammo.containsKey(weapon)) {
            return ammo.get(weapon);
        } else {
            return 0;
        }
    }

    /**
     * Reduces ammo count for the weapon by 1
     * @param weapon
     * @throws IllegalStateException if there is no ammo
     */
    public void removeOneAmmo(Weapon weapon) {
        if (ammo.containsKey(weapon)) {
            int count = ammo.get(weapon) - 1;
            if (count == 0) {
                ammo.remove(weapon);
            } else {
                ammo.put(weapon, count);
            }
        } else {
            throw new IllegalStateException("No ammo for " + weapon);
        }
    }
}
