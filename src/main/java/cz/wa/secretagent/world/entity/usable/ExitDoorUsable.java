package cz.wa.secretagent.world.entity.usable;

import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.map.Tile;

/**
 * Closed exit door. 
 * 
 * @author Ondrej Milenovsky
 */
public class ExitDoorUsable extends UsableEntity {

    private final String openModel;
    private final Map<TileId, Tile> replaceTiles;

    public ExitDoorUsable(ObjectModel model, Vector2D pos, String exitModel, Map<TileId, Tile> replaceTiles) {
        super(model, pos, true);
        openModel = exitModel;
        this.replaceTiles = replaceTiles;
    }

    @Override
    public UsableType getSecondType() {
        return UsableType.EXIT_DOOR;
    }

    public String getOpenModel() {
        return openModel;
    }

    public Map<TileId, Tile> getReplaceTiles() {
        return replaceTiles;
    }
}
