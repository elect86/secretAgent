package cz.wa.secretagent.view.renderer.world;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import cz.wa.secretagent.game.player.Camera;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.wautils.collection.Array2D;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Vector2I;

/**
 * Renders the level (background, walls, spikes, tables) 
 * 
 * @author Ondrej Milenovsky
 */
public class LevelRenderer extends AbstractWorldRenderer {

    private static final long serialVersionUID = -4093978814290475364L;

    @Override
    public void init() {
        // empty
    }

    @Override
    public void dispose() {
        // empty
    }

    /**
     * Background tiles, should be called before drawing entities.
     * @param level
     * @param camera
     */
    public void drawBackground(LevelMap level, Camera camera) {
        drawInternal(level, camera, level.getBackground());
    }

    /**
     * Foreground tiles, should be called after drawing entities.
     * @param level
     * @param camera
     */
    public void drawForeground(LevelMap level, Camera camera) {
        drawInternal(level, camera, level.getForeground());
    }

    private void drawInternal(LevelMap level, Camera camera, Array2D<List<Tile>> map) {
        Rectangle2D bounds = camera.getWorldBounds();
        Vector2D tileSize = level.getTileSize();

        int x1 = (int) FastMath.floor(bounds.getX() / tileSize.getX() + 0.5);
        int x2 = (int) FastMath.ceil(bounds.getX2() / tileSize.getX() + 0.5) - 1;
        int y1 = (int) FastMath.floor(bounds.getY() / tileSize.getY() + 0.5);
        int y2 = (int) FastMath.ceil(bounds.getY2() / tileSize.getY() + 0.5) - 1;

        x1 = FastMath.max(x1, 0);
        x2 = FastMath.min(x2, level.getSize().getX() - 1);
        y1 = FastMath.max(y1, 0);
        y2 = FastMath.min(y2, level.getSize().getY() - 1);

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                Vector2D pos = new Vector2D(x * tileSize.getX(), y * tileSize.getY());
                drawTiles(level, map, camera, new Vector2I(x, y), pos);
            }
        }
    }

    /**
     * Draw tiles at single cell
     */
    private void drawTiles(LevelMap level, Array2D<List<Tile>> map, Camera camera, Vector2I p, Vector2D pos) {
        for (Tile tile : map.get(p)) {
            renderModel(null, camera, tile.getModel(), pos);
        }
    }

}
