package cz.wa.secretagent.io.map.orig.generator.entity.platform;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.platform.PlatformLift;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.entity.EntityDirection;

/**
 * Creates moving platform. 
 * 
 * @author Ondrej Milenovsky
 */
public class PlatformLiftEntityCreator implements EntityCreator<PlatformLift> {
    private static final long serialVersionUID = -3975166234798772114L;

    private static final Logger logger = LoggerFactory.getLogger(PlatformLiftEntityCreator.class);

    private EntityDirection defaultDirection;
    private double speed;
    private Rectangle2D bounds;

    @Override
    public PlatformLift createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        EntityDirection dir = defaultDirection;
        if (!args.isEmpty()) {
            String arg0 = args.remove(0);
            try {
                dir = EntityDirection.valueOf(arg0);
            } catch (IllegalArgumentException e) {
                logger.error("Wrong direction: " + args + " for lift: " + tileId, e);
            }
        }
        return new PlatformLift(model, pos, bounds, dir.getVector().scalarMultiply(speed));
    }

    public EntityDirection getDefaultDirection() {
        return defaultDirection;
    }

    @Required
    public void setDefaultDirection(EntityDirection defaultDirection) {
        this.defaultDirection = defaultDirection;
    }

    public double getSpeed() {
        return speed;
    }

    @Required
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    @Required
    public void setBounds(Rectangle2D bounds) {
        this.bounds = bounds;
    }

}
