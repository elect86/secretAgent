package cz.wa.secretagent.world.entity.bgswitch;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.SwitchAction;
import secretAgent.world.ObjectModel;

/**
 * Simple switch to activate/deactivate something.
 * 
 * @author Ondrej Milenovsky
 */
public class SimpleSwitch extends SwitchEntity {

    public SimpleSwitch(ObjectModel model, Vector2D pos, String lockType, boolean singleUse,
                        String description, List<? extends SwitchAction> actions) {
        super(model, pos, lockType, singleUse, description, actions);
    }

    @Override
    public SwitchType getSecondType() {
        return SwitchType.SIMPLE;
    }
}
