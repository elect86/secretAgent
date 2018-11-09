package cz.wa.secretagent.world.entity.item;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.world.weapon.Weapon;
import secretAgent.world.ObjectModel;

/**
 * Ammo for specific or current weapon. 
 * 
 * @author Ondrej Milenovsky
 */
public class AmmoItem extends ItemEntity {
    private final Weapon weapon;
    private final int count;

    public AmmoItem(ObjectModel model, Vector2D pos, Weapon weapon, int count) {
        super(model, pos);
        this.weapon = weapon;
        this.count = count;
    }

    @Override
    public ItemType getSecondType() {
        return ItemType.AMMO;
    }

    /**
     * @return weapon or null if for current weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    public int getCount() {
        return count;
    }
}
