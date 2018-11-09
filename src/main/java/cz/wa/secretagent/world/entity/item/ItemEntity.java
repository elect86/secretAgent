package cz.wa.secretagent.world.entity.item;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;

/**
 * Pickable item 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class ItemEntity extends Entity {

    public ItemEntity(ObjectModel model, Vector2D pos) {
        super(model, pos);
    }

    @Override
    public EntityType getType() {
        return EntityType.ITEM;
    }

    @Override
    public abstract ItemType getSecondType();

}
