package cz.wa.secretagent.world.entity.agent;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.view.model.AgentModel;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.capabilities.AgentCapabilities;
import cz.wa.secretagent.world.entity.direction.EntityXDirection;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Some agent (human, robot).
 * 
 * @author Ondrej Milenovsky
 */
public abstract class AgentEntity extends Entity {

    private AgentCapabilities capabilities;
    private double health;
    /** team of the agent, agents from same team cannot hurt each other (except explosions) */
    private Team team;

    // movement
    /** how long is moving same action */
    private long actionTimeMs;
    /** current action */
    private AgentAction currentAction;
    /** current x direction */
    private EntityXDirection direction;
    /** moving speed caused by the agent */
    private Vector2D moveSpeed;

    // activating
    /** entity that can be used right now or null */
    private Entity entityToUse;

    // weapons
    /** remaining reload time when firing weapon */
    private double reloadTimeRemainingS;
    /** if the trigger is pressed */
    private boolean firing;

    public AgentEntity(ObjectModel model, Vector2D pos, Team team, EntityXDirection direction,
            Rectangle2D sizeBounds, Vector2D speed, boolean staticPos) {
        super(model, pos, sizeBounds, speed, staticPos);
        this.team = team;
        setDirection(direction);
        actionTimeMs = 0;
        currentAction = AgentAction.STAY;
        moveSpeed = Vector2D.ZERO;
        health = 1;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.AGENT;
    }

    @Override
    public abstract AgentType getSecondType();

    public void setCapabilities(AgentCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public AgentCapabilities getCapabilities() {
        return capabilities;
    }

    public AgentAction getCurrentAction() {
        return currentAction;
    }

    /**
     * Sets new current action and resets action time if action changed
     * @param action
     */
    public void setCurrentAction(AgentAction action) {
        Validate.notNull(action, "action is null");
        if (this.currentAction != action) {
            actionTimeMs = 0;
        }
        this.currentAction = action;
    }

    public EntityXDirection getDirection() {
        return direction;
    }

    public void setDirection(EntityXDirection direction) {
        Validate.notNull(direction, "direction is null");
        Validate.isTrue(direction != EntityXDirection.NONE, "direction must not be NONE");
        this.direction = direction;
    }

    public long getActionTime() {
        return actionTimeMs;
    }

    public void addActionTime(long timeMs) {
        this.actionTimeMs += timeMs;
    }

    public void resetActionTime() {
        actionTimeMs = 0;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isControlable() {
        return currentAction != AgentAction.DEATH;
    }

    public Entity getEntityToUse() {
        return entityToUse;
    }

    public void setEntityToUse(Entity entityToUse) {
        this.entityToUse = entityToUse;
    }

    public Vector2D getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(Vector2D moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getReloadTimeRemainingS() {
        return reloadTimeRemainingS;
    }

    public void setReloadTimeRemainingS(double reloadTimeRemainingS) {
        this.reloadTimeRemainingS = reloadTimeRemainingS;
    }

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    /**
     * @return weapon rotation center relative to agent's position according to agent's direction
     */
    public Vector2D getWeaponCenter() {
        ObjectModel model = getModel();
        if (model instanceof AgentModel) {
            Vector2D ret = ((AgentModel) model).getWeaponCenter();
            if (direction == EntityXDirection.LEFT) {
                ret = new Vector2D(-ret.getX(), ret.getY());
            }
            return ret;
        } else {
            return Vector2D.ZERO;
        }
    }

}
