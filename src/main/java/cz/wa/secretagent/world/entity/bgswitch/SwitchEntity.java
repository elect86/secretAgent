package cz.wa.secretagent.world.entity.bgswitch;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.LockedEntity;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.SwitchAction;

/**
 * Some switch.
 * 
 * @author Ondrej Milenovsky
 */
public abstract class SwitchEntity extends Entity implements LockedEntity {
    private final String lockType;
    /** false only if is single use and already activated */
    private boolean active;
    private boolean locked;
    private final String description;
    private final List<? extends SwitchAction> actions;
    private final boolean singleUse;

    public SwitchEntity(ObjectModel model, Vector2D pos, String lockType, boolean singleUse,
            String description, List<? extends SwitchAction> actions) {
        super(model, pos);
        if (StringUtils.isEmpty(lockType)) {
            lockType = null;
        }
        this.lockType = lockType;
        this.singleUse = singleUse;
        this.description = description;
        this.actions = actions;
        active = true;
        locked = lockType != null;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SWITCH;
    }

    @Override
    public String getLockType() {
        return lockType;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void unlock() {
        locked = false;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * @return name displayed when player can use the switch
     */
    public String getDescription() {
        return description;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public void activate(AgentEntity agent, SAMWorld world) {
        if (isActive()) {
            for (SwitchAction action : actions) {
                action.execute(agent, world);
            }
            if (singleUse) {
                active = false;
            }
        }
    }

    public List<? extends SwitchAction> getActions() {
        return actions;
    }

    @Override
    public abstract SwitchType getSecondType();

}
