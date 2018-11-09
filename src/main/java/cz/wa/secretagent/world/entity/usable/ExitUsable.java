package cz.wa.secretagent.world.entity.usable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.ObjectModel;

/**
 * Open exit door from the level. 
 * 
 * @author Ondrej Milenovsky
 */
public class ExitUsable extends UsableEntity {

    public ExitUsable(ObjectModel model, Vector2D pos) {
        super(model, pos, true);
    }

    @Override
    public UsableType getSecondType() {
        return UsableType.EXIT;
    }
}
