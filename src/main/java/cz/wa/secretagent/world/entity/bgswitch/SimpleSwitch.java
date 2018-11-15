package cz.wa.secretagent.world.entity.bgswitch;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.ObjectModel;
import secretAgent.world.entity.bgSwitch.SwitchAction;

import java.util.List;

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
