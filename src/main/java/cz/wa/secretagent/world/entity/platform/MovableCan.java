package cz.wa.secretagent.world.entity.platform;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.ObjectModel;

/**
 * Can that can be pushed and stepped on. 
 * 
 * @author Ondrej Milenovsky
 */
public class MovableCan extends PlatformEntity {

    public MovableCan(ObjectModel model, Vector2D pos, Rectangle2D bounds) {
        super(model, pos, bounds);
    }

    @Override
    public PlatformType getSecondType() {
        return PlatformType.CAN;
    }

}
