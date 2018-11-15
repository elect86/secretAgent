package cz.wa.secretagent.world.entity.item;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.ObjectModel;
import secretAgent.world.entity.Entity;
import secretAgent.world.entity.EntityType;

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
