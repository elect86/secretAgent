package cz.wa.secretagent.world.entity.item;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;

/**
 * Key for a door, floppy or dynamite. 
 * 
 * @author Ondrej Milenovsky
 */
public class KeyItem extends ItemEntity {

    private final String lockType;

    /**
     * @param model model
     * @param pos position
     * @param lockType lock type, will open door with same type
     */
    public KeyItem(ObjectModel model, Vector2D pos, String lockType) {
        super(model, pos);
        Validate.notEmpty(lockType, "lockType is empty");
        this.lockType = lockType;
    }

    @Override
    public ItemType getSecondType() {
        return ItemType.KEY;
    }

    public String getLockType() {
        return lockType;
    }
}
