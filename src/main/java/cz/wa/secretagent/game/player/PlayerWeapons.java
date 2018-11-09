//package cz.wa.secretagent.game.player;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import cz.wa.secretagent.world.entity.agent.inventory.AgentInventory;
//import cz.wa.secretagent.world.entity.item.ItemEntity;
//import cz.wa.secretagent.world.weapon.Weapon;
//
///**
// * Class holding weapons and ammo of an player.
// *
// * @author Ondrej Milenovsky
// */
//public class PlayerWeapons implements Serializable {
//
//    private static final long serialVersionUID = -7097475203090982225L;
//
//    /** weapon -> ammo */
//    private Map<Weapon, Integer> ammo;
//    /** all weapon */
//    private Set<Weapon> weapons;
//    /** special items that are carried outside buildings */
//    private List<ItemEntity> items;
//
//    public PlayerWeapons() {
//        ammo = new HashMap<Weapon, Integer>();
//        weapons = new HashSet<Weapon>();
//        items = new ArrayList<ItemEntity>();
//    }
//
//    /**
//     * Adds all the weapons and items to the inventory (does not remove from this object)
//     * @param inventory
//     */
//    public void loadToInventory(AgentInventory inventory) {
//        for (Map.Entry<Weapon, Integer> entry : ammo.entrySet()) {
//            inventory.addAmmo(entry.getKey(), entry.getValue());
//        }
//        for (Weapon weapon : weapons) {
//            inventory.addWeapon(weapon);
//        }
//        for (ItemEntity item : items) {
//            inventory.addItem(item);
//        }
//    }
//
//    public Map<Weapon, Integer> getAmmo() {
//        return ammo;
//    }
//
//    public void setAmmo(Map<Weapon, Integer> ammo) {
//        this.ammo = ammo;
//    }
//
//    public Set<Weapon> getWeapons() {
//        return weapons;
//    }
//
//    public void setWeapons(Set<Weapon> weapons) {
//        this.weapons = weapons;
//    }
//
//    public List<ItemEntity> getItems() {
//        return items;
//    }
//
//    public void setItems(List<ItemEntity> items) {
//        this.items = items;
//    }
//
//    public PlayerWeapons deepCopy() {
//        PlayerWeapons ret = new PlayerWeapons();
//        ret.ammo.putAll(ammo);
//        ret.weapons.addAll(weapons);
//        ret.items.addAll(items);
//        return ret;
//    }
//
//}
