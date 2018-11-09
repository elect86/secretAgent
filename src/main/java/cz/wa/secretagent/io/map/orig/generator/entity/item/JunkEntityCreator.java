package cz.wa.secretagent.io.map.orig.generator.entity.item;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.entity.item.JunkItem;
import secretAgent.world.ObjectModel;

/**
 * Creates junk items. 
 * 
 * @author Ondrej Milenovsky
 */
public class JunkEntityCreator implements EntityCreator<JunkItem> {
    private static final long serialVersionUID = 2910402011157261915L;

    private static final Logger logger = LoggerFactory.getLogger(JunkEntityCreator.class);

    @Override
    public JunkItem createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        int price = 0;
        String arg0 = args.remove(0);
        try {
            price = Integer.parseInt(arg0);
        } catch (NumberFormatException e) {
            logger.error("Wrong price number: " + arg0 + " for junk: " + tileId, e);
        }
        return new JunkItem(model, pos, true, price);
    }

}
