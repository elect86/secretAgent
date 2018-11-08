package cz.wa.secretagent.io.map.orig;

import cz.wa.wautils.math.Vector2I;

/**
 * Single parsed original level in byte format. There should be no nulls in the arrays after generating the map.
 * 
 * @author Ondrej Milenovsky
 */
public class MapLevel {
    private final Vector2I size;
    private final int bgTile;
    private final int bgTileOver;
    private final int[][] tiles;
    private final int[][] items;
    private final int[] offsets;

    public MapLevel(Vector2I size, int bgTile, int bgTileOver, int[][] tiles, int[][] items, int[] offsets) {
        this.size = size;
        this.bgTile = bgTile;
        this.bgTileOver = bgTileOver;
        this.tiles = tiles;
        this.items = items;
        this.offsets = offsets;
    }

    public Vector2I getSize() {
        return size;
    }

    public int getBgTile() {
        return bgTile;
    }

    public int getBgTileOver() {
        return bgTileOver;
    }

    public int[][] getTiles() {
        return tiles;
    }

    public int[][] getOverTiles() {
        return items;
    }

    public int[] getOffsets() {
        return offsets;
    }

}
