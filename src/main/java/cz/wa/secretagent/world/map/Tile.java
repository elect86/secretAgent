package cz.wa.secretagent.world.map;

import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;

/**
 * Single static tile. 
 * 
 * @author Ondrej Milenovsky
 */
public class Tile {
    private final TileType type;
    private final ObjectModel model;

    public Tile(TileType type, ObjectModel model) {
        this.type = type;
        this.model = model;
    }

    public TileType getType() {
        return type;
    }

    public ObjectModel getModel() {
        return model;
    }

    public boolean hasTileId(TileId tileId) {
        return model.getAllTileIds().contains(tileId);
    }

}
