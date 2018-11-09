package cz.wa.secretagent.io.map.orig.generator.entity.bgswitch;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.entity.bgswitch.SimpleSwitch;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.AddTilesSwitchAction;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.DisableLaserSwitchAction;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.SwitchAction;
import secretAgent.world.ObjectModel;

/**
 * Creates simple switch.
 * 
 * @author Ondrej Milenovsky
 */
public class SimpleSwitchEntityCreator implements EntityCreator<SimpleSwitch> {
    private static final long serialVersionUID = 6272627304801242570L;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSwitchEntityCreator.class);

    private static final String DISABLE_LASER_ACTION = "DISABLE_LASER";
    private static final String ADD_TILES_ACTION = "ADD_TILES";

    private static final String ONCE_USE = "ONCE";
    private static final String REPEAT_USE = "REPEAT";

    @Override
    public SimpleSwitch createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        if (args.size() < 4) {
            logger.warn("Not enough arguments for switch: " + tileId);
        } else {
            String lockType = args.remove(0);
            boolean singleUse = getSingleUse(args.remove(0), tileId);
            String description = args.remove(0);
            SwitchAction action = createAction(args, tileId);
            return new SimpleSwitch(model, pos, lockType, singleUse, description,
                    Collections.singletonList(action));
        }
        return null;
    }

    private boolean getSingleUse(String str, TileId tileId) {
        if (str.equals(ONCE_USE)) {
            return true;
        } else if (str.equals(REPEAT_USE)) {
            return false;
        } else {
            logger.warn("Unknown use type '" + str + "' for switch: " + tileId);
            return true;
        }
    }

    private SwitchAction createAction(List<String> args, TileId tileId) {
        String actionType = args.remove(0);
        if (actionType.equals(DISABLE_LASER_ACTION)) {
            return new DisableLaserSwitchAction();
        } else if (actionType.equals(ADD_TILES_ACTION)) {
            if (args.isEmpty()) {
                logger.warn("Missing tile ids for action ADD_TILES for switch: " + tileId);
                return null;
            }
            return new AddTilesSwitchAction(parseTileIds(args, tileId));
        } else {
            logger.warn("Unknown action type '" + actionType + "' for switch: " + tileId);
            return null;
        }
    }

    private Set<TileId> parseTileIds(List<String> args, TileId tileId) {
        Set<TileId> ret = new LinkedHashSet<TileId>(args.size());
        for (String arg : args) {
            try {
                ret.add(new TileId(arg));
            } catch (IllegalArgumentException e) {
                logger.error("Wrong tileId format for switch: " + tileId, e);
            }
        }
        args.clear();
        return ret;
    }

}
