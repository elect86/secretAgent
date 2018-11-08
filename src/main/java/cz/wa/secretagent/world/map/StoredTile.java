package cz.wa.secretagent.world.map;

import cz.wa.wautils.math.Vector2I;

/**
 * Tile with position.
 * 
 * @author Ondrej Milenovsky
 */
public class StoredTile {
    private final Vector2I pos;
    private final Tile tile;

    public StoredTile(Vector2I pos, Tile tile) {
        this.tile = tile;
        this.pos = pos;
    }

    public Tile getTile() {
        return tile;
    }

    public Vector2I getPos() {
        return pos;
    }
}
