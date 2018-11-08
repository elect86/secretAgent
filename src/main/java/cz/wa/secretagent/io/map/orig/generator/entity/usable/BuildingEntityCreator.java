package cz.wa.secretagent.io.map.orig.generator.entity.usable;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.usable.BuildingUsable;

/**
 * Generates island map building. 
 * 
 * @author Ondrej Milenovsky
 */
public class BuildingEntityCreator implements EntityCreator<BuildingUsable> {
    private static final long serialVersionUID = -2735846873194693731L;

    private static final Logger logger = LoggerFactory.getLogger(BuildingEntityCreator.class);

    private static final String FINAL_BUILDING = "FINAL";

    @Override
    public BuildingUsable createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        boolean finalBuilding = false;
        String finishedModel = null;
        if (!args.isEmpty()) {
            finishedModel = args.remove(0);
        } else {
            logger.warn("BUILDING has no finished model: " + tileId);
        }
        if (!args.isEmpty()) {
            String arg0 = args.remove(0);
            if (arg0.equals(FINAL_BUILDING)) {
                finalBuilding = true;
            } else {
                logger.warn("Unknown argument for BUILDING " + tileId + ": " + arg0);
            }
        }
        return new BuildingUsable(model, pos, finishedModel, finalBuilding);
    }

}
