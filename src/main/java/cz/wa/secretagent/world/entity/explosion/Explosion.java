package cz.wa.secretagent.world.entity.explosion;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.EntityType2;

/**
 * Explosion, has no second type, there is only one explosion.
 * 
 * @author Ondrej Milenovsky
 */
public class Explosion extends Entity {

    private final double radius;
    private final double damage;
    private double timeS;
    private final double durationS;

    public Explosion(ObjectModel model, Vector2D pos, double radius, double damage, double durationS) {
        super(model, pos);
        this.radius = radius;
        this.damage = damage;
        this.durationS = durationS;
        timeS = 0;
    }

    public double getRadius() {
        return radius;
    }

    public double getDamage() {
        return damage;
    }

    public void addTime(double timeS) {
        this.timeS += timeS;
    }

    public double getTimeS() {
        return timeS;
    }

    public double getDurationS() {
        return durationS;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.EXPLOSION;
    }

    @Override
    public EntityType2 getSecondType() {
        return null;
    }

}
