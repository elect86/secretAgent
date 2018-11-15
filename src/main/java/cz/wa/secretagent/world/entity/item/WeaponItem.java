//package cz.wa.secretagent.world.entity.item;
//
//import org.apache.commons.lang.Validate;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import cz.wa.secretagent.world.weapon.Weapon;
//import secretAgent.world.ObjectModel;
//
///**
// * Weapon item, can contain ammo.
// *
// * @author Ondrej Milenovsky
// */
//public class WeaponItem extends AmmoItem {
//
//    public WeaponItem(ObjectModel model, Vector2D pos, Weapon weapon, int ammoCount) {
//        super(model, pos, weapon, ammoCount);
//        Validate.notNull(weapon, "weapon is null");
//    }
//
//    @Override
//    public ItemType getSecondType() {
//        return ItemType.WEAPON;
//    }
//
//}
