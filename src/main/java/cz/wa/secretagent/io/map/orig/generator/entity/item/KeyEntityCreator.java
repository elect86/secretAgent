package cz.wa.secretagent.io.map.orig.generator.entity.item;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.item.KeyItem;

/**
 * Creates keys. 
 * 
 * @author Ondrej Milenovsky
 */
public class KeyEntityCreator implements EntityCreator<KeyItem> {
    private static final long serialVersionUID = -7309784166465152356L;

    private static final Logger logger = LoggerFactory.getLogger(KeyEntityCreator.class);

    @Override
    public KeyItem createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        if (args.isEmpty()) {
            logger.warn("Not enough arguments for key: " + tileId);
        } else {
            String lockType = args.remove(0);
            if (lockType.isEmpty()) {
                logger.warn("Lock type is empty for key: " + tileId);
            } else {
                return new KeyItem(model, pos, lockType);
            }
        }
        return null;
    }

}
