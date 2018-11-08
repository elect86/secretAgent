package cz.wa.secretagent.worldinfo.graphics;

import java.util.Map;

import cz.wa.wautils.math.Vector2I;

/**
 * Info about single tile set.
 * 
 * @author Ondrej Milenovsky
 */
public class TilesInfo {

    private final Vector2I tileSize;
    private final Map<Integer, TileInfo> tiles;

    public TilesInfo(Vector2I tileSize, Map<Integer, TileInfo> tiles) {
        this.tileSize = tileSize;
        this.tiles = tiles;
    }

    public Vector2I getTileSize() {
        return tileSize;
    }

    public Map<Integer, TileInfo> getTiles() {
        return tiles;
    }

    public TileInfo getTile(int tileId) {
        return tiles.get(tileId);
    }

    public boolean containsTile(int tileId) {
        return tiles.containsKey(tileId);
    }

}
