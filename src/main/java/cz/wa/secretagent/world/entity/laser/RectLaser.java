package cz.wa.secretagent.world.entity.laser;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.ObjectModel;
import secretAgent.world.entity.agent.Team;

/**
 * Rectangular perpendicular laser. 
 * 
 * @author Ondrej Milenovsky
 */
public class RectLaser extends LaserEntity {

    public RectLaser(ObjectModel model, Vector2D pos, Team team, double damage, Rectangle2D sizeBounds,
                     boolean levelLaser) {
        super(model, pos, team, damage, levelLaser);
        setSizeBounds(sizeBounds);
    }

    @Override
    public LaserType getSecondType() {
        return LaserType.RECTANGULAR;
    }
}
