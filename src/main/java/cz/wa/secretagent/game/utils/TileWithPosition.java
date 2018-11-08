package cz.wa.secretagent.game.utils;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Vector2I;

/**
 * Tile with position and tile id. 
 * 
 * @author Ondrej Milenovsky
 */
public class TileWithPosition {
    private final TileId tileId;
    private final Tile tile;
    private final Vector2I pos;
    private final Rectangle2D bounds;

    public TileWithPosition(TileId tileId, Tile tile, Vector2I pos, Vector2D tileSize) {
        this.tileId = tileId;
        this.tile = tile;
        this.pos = pos;
        double sizeX = tileSize.getX();
        double sizeY = tileSize.getY();
        this.bounds = new Rectangle2D(-sizeX / 2.0, -sizeY / 2.0, sizeX, sizeY).move(new Vector2D(pos.getX()
                * sizeX, pos.getY() * sizeY));
    }

    public TileId getTileId() {
        return tileId;
    }

    public Tile getTile() {
        return tile;
    }

    public Vector2I getPos() {
        return pos;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }
}
