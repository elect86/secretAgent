package cz.wa.secretagent.world.entity.platform;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.ObjectModel;

/**
 * Moving platform. 
 * 
 * @author Ondrej Milenovsky
 */
public class PlatformLift extends PlatformEntity {

    private boolean movingForward;

    public PlatformLift(ObjectModel model, Vector2D pos, Rectangle2D bounds, Vector2D speed) {
        super(model, pos, bounds);
        setSpeed(speed);
    }

    @Override
    public PlatformType getSecondType() {
        return PlatformType.LIFT;
    }

    public boolean isMovingForward() {
        return movingForward;
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }

}
