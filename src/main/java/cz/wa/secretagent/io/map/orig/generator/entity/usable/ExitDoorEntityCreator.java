package cz.wa.secretagent.io.map.orig.generator.entity.usable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.io.SAMIO;
import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.entity.usable.ExitDoorUsable;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.secretagent.world.map.TileType;
import secretAgent.world.ObjectModel;

/**
 * Creates closed exit door. 
 * 
 * @author Ondrej Milenovsky
 */
public class ExitDoorEntityCreator implements EntityCreator<ExitDoorUsable> {
    private static final long serialVersionUID = 1972964051271447812L;

    private static final Logger logger = LoggerFactory.getLogger(ExitDoorEntityCreator.class);

    private SAMIO samIO;

    @Override
    public ExitDoorUsable createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        String openModel = null;
        if (args.isEmpty()) {
            logger.warn("No open model for exit door: " + tileId);
        } else {
            openModel = args.remove(0);
        }
        Map<TileId, Tile> replaceTiles = createReplaceTiles(args, tileId);
        return new ExitDoorUsable(model, pos, openModel, replaceTiles);
    }

    private Map<TileId, Tile> createReplaceTiles(List<String> args, TileId tileId) {
        Map<TileId, Tile> ret = new HashMap<TileId, Tile>(args.size() / 2);
        for (int i = 0; i < args.size() / 2; i++) {
            try {
                TileId findTileId = new TileId(args.remove(0));
                String modelName = args.remove(0);
                ObjectModel model = getSamIO().getWorldHolder().getGraphicsInfo().getModel(modelName);
                ret.put(findTileId, new Tile(TileType.GHOST_FRONT, model));
            } catch (IllegalArgumentException e) {
                logger.error("Error creating replace tile for OPEN_EXIT action: " + tileId, e);
            }
        }
        return ret;
    }

    public SAMIO getSamIO() {
        return samIO;
    }

    @Required
    public void setSamIO(SAMIO samIO) {
        this.samIO = samIO;
    }
}
