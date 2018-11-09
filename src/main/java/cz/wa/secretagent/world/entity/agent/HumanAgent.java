package cz.wa.secretagent.world.entity.agent;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.agent.inventory.AgentInventory;
import cz.wa.secretagent.world.entity.laser.LineLaser;
import cz.wa.secretagent.world.weapon.Weapon;
import cz.wa.wautils.math.Rectangle2D;
import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.entity.EntityXDirection;
import secretAgent.world.entity.EntityYDirection;

/**
 * Single human agent (player or enemy), can jump.
 * 
 * @author Ondrej Milenovsky
 */
public class HumanAgent extends AgentEntity {
    // inventory
    private final AgentInventory inventory;
    /** active weapon, can be null */
    private Weapon weapon;
    /** if currently holding the jump button */
    private boolean jumping;
    /** jump time remaining, used when jumping */
    private double jumpRemainingS;
    /** current aiming angle (relative to direction, 0 is straight, +PI/2 is down, -PI/2 is UP) */
    private double aimAngle;
    /** current aiming action */
    private EntityYDirection aiming;
    /** current laser sights, if not null, then is already in the world, when nulling, must be removed from the world ! */
    private LineLaser laserSights;

    public HumanAgent(ObjectModel model, Vector2D pos, Team team, EntityXDirection direction,
            Rectangle2D sizeBounds) {
        super(model, pos, team, direction, sizeBounds, Vector2D.ZERO, false);
        inventory = new AgentInventory();
        setAimAngle(0);
        aiming = EntityYDirection.NONE;
    }

    /**
     * @param weapon active weapon, can be null
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * @return active weapon, can be null
     */
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public AgentType getSecondType() {
        return AgentType.HUMAN;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    /**
     * @return remaining jump time in seconds
     */
    public double getJumpRemainingS() {
        return jumpRemainingS;
    }

    /**
     * @param jumpRemainingS remaining jump time in seconds
     */
    public void setJumpRemainingS(double jumpRemainingS) {
        this.jumpRemainingS = jumpRemainingS;
    }

    public AgentInventory getInventory() {
        return inventory;
    }

    public double getAimAngle() {
        return aimAngle;
    }

    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
    }

    public EntityYDirection getAiming() {
        return aiming;
    }

    public void setAiming(EntityYDirection aiming) {
        Validate.notNull(aiming, "aiming is null");
        this.aiming = aiming;
    }

    public LineLaser getLaserSights() {
        return laserSights;
    }

    public void setLaserSights(LineLaser laserSights) {
        this.laserSights = laserSights;
    }
}
