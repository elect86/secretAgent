package cz.wa.secretagent.world.entity.laser;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.Team;
import secretAgent.world.ObjectModel;

/**
 * Some laser (security, laser gun projectile, weapon laser sight).
 * Can damage agents from different team or do no damage. 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class LaserEntity extends Entity {
    /** team (same as projectile team) */
    private final Team team;
    /** if is security laser */
    private final boolean levelLaser;

    /** damage per second to agents from different team */
    private double damage;

    public LaserEntity(ObjectModel model, Vector2D pos, Team team, double damage, boolean levelLaser) {
        super(model, pos);
        this.team = team;
        this.damage = damage;
        this.levelLaser = levelLaser;
    }

    @Override
    public EntityType getType() {
        return EntityType.LASER;
    }

    public Team getTeam() {
        return team;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isLevelLaser() {
        return levelLaser;
    }

    @Override
    public abstract LaserType getSecondType();

}
