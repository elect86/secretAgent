package cz.wa.secretagent.world.entity.usable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;

/**
 * Fence blocking the final level. This entity is not usable, will be removed automatically. 
 * 
 * @author Ondrej Milenovsky
 */
public class FenceUsable extends UsableEntity {

    public FenceUsable(ObjectModel model, Vector2D pos) {
        super(model, pos, true);
        setActive(false);
    }

    @Override
    public UsableType getSecondType() {
        return UsableType.FENCE;
    }

}
