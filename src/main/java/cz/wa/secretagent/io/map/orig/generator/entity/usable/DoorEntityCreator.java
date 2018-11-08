package cz.wa.secretagent.io.map.orig.generator.entity.usable;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.usable.DoorUsable;

/**
 * Creates door that is opened by a key. 
 * 
 * @author Ondrej Milenovsky
 */
public class DoorEntityCreator implements EntityCreator<DoorUsable> {
    private static final long serialVersionUID = 216540582465239681L;

    private static final Logger logger = LoggerFactory.getLogger(DoorEntityCreator.class);

    @Override
    public DoorUsable createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        if (args.isEmpty()) {
            logger.warn("Not enough arguments for door: " + tileId);
        } else {
            String lockType = args.remove(0);
            if (lockType.isEmpty()) {
                logger.warn("Lock type is empty for door: " + tileId);
            } else {
                return new DoorUsable(model, pos, lockType);
            }
        }
        return null;
    }

}
