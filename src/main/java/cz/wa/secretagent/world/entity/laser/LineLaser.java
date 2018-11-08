package cz.wa.secretagent.world.entity.laser;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.agent.Team;

/**
 * Laser defined by two points. 
 * 
 * @author Ondrej Milenovsky
 */
public class LineLaser extends LaserEntity {

    /** second point of the laser */
    private Vector2D pos2;
    private double width;

    public LineLaser(ObjectModel model, Vector2D pos, Team team, double damage, Vector2D pos2, double width,
            boolean levelLaser) {
        super(model, pos, team, damage, levelLaser);
        setPos2(pos2);
        this.setWidth(width);
    }

    @Override
    public LaserType getSecondType() {
        return LaserType.LINE;
    }

    public Vector2D getPos2() {
        return pos2;
    }

    public void setPos2(Vector2D pos2) {
        Validate.notNull(pos2, "pos2 is null");
        this.pos2 = pos2;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

}
