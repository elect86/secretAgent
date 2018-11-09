package cz.wa.secretagent.world.entity.platform;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Moving platform (elevator).
 * 
 * @author Ondrej Milenovsky
 */
public abstract class PlatformEntity extends Entity {

    public PlatformEntity(ObjectModel model, Vector2D pos, Rectangle2D bounds) {
        super(model, pos, bounds, Vector2D.ZERO, false);
    }

    @Override
    public EntityType getType() {
        return EntityType.PLATFORM;
    }

    @Override
    public abstract PlatformType getSecondType();

}
