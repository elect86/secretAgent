package cz.wa.secretagent.world.entity.usable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.world.entity.LockedEntity;
import secretAgent.world.ObjectModel;

/**
 * Door that is opened by a key. 
 * 
 * @author Ondrej Milenovsky
 */
public class DoorUsable extends UsableEntity implements LockedEntity {

    private final String lockType;

    /**
     * @param model model
     * @param pos position
     * @param lockType lock type, key with the type will open the door, null or "" means no key needed
     */
    public DoorUsable(ObjectModel model, Vector2D pos, String lockType) {
        super(model, pos, true);
        if (StringUtils.isEmpty(lockType)) {
            lockType = null;
        }
        this.lockType = lockType;
    }

    @Override
    public UsableType getSecondType() {
        return UsableType.DOOR;
    }

    /**
     * @return lock type or null
     */
    @Override
    public String getLockType() {
        return lockType;
    }

    /**
     * @return if the door requires a key
     */
    @Override
    public boolean isLocked() {
        return lockType != null;
    }

    @Override
    public void unlock() {
        // nothing, the door will be removed
    }

}
