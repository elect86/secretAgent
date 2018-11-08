package cz.wa.secretagent.world.entity.projectile;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.HasModelAngle;
import cz.wa.secretagent.world.entity.agent.Team;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.VectorUtils;

/**
 * Fired projectile, mine or spikes 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class ProjectileEntity extends Entity implements HasModelAngle {

    /** team of agent who fired it */
    private final Team team;
    /** impact damage (not explosion damage) */
    private double damage;
    /** distance to fly before it disappears */
    private double remainingDist;
    /** angle of model */
    private double modelAngle;

    public ProjectileEntity(ObjectModel model, Vector2D pos, Team team) {
        super(model, pos);
        this.team = team;
        setStaticPos(false);
        setSizeBounds(Rectangle2D.ZERO);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PROJECTILE;
    }

    @Override
    public abstract ProjectileType getSecondType();

    public Team getTeam() {
        return team;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getRemainingDist() {
        return remainingDist;
    }

    public void setRemainingDist(double remainingDist) {
        this.remainingDist = remainingDist;
    }

    @Override
    public double getModelAngle() {
        if (Double.isNaN(modelAngle)) {
            modelAngle = VectorUtils.getAngle(getSpeed());
        }
        return modelAngle;
    }

    @Override
    public void setSpeed(Vector2D speed) {
        super.setSpeed(speed);
        modelAngle = Double.NaN;
    }

}
