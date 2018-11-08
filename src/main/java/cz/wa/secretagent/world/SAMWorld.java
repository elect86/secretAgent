package cz.wa.secretagent.world;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Vector2I;

/**
 * World class containing all current entities, background and items.  
 * 
 * @author Ondrej Milenovsky
 */
public class SAMWorld {

    private LevelMap levelMap;
    private EntityMap entityMap;
    private int levelId;
    private boolean running;

    /**
     * Creates paused world.
     * @param size map size
     * @param island true if it is island map
     */
    public SAMWorld(Vector2I size, Tile backgroundTile, int levelId) {
        this.levelId = levelId;
        levelMap = new LevelMap(size, backgroundTile);
        entityMap = new EntityMap();
        running = false;
    }

    public LevelMap getLevelMap() {
        return levelMap;
    }

    public EntityMap getEntityMap() {
        return entityMap;
    }

    public void addSimTimeMs(long timeMs) {
        levelMap.addTime(timeMs);
    }

    public boolean isIsland() {
        return levelId == 0;
    }

    public int getLevelId() {
        return levelId;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isEmpty() {
        return levelMap.getSize().equals(Vector2I.ZERO);
    }

    /**
     * @return visible bounds of the world
     */
    public Rectangle2D getBounds() {
        Vector2D tileSize = levelMap.getTileSize();
        double width = levelMap.getSize().getX() * tileSize.getX();
        double height = levelMap.getSize().getY() * tileSize.getY();
        return new Rectangle2D(-tileSize.getX() / 2.0, -tileSize.getY() / 2.0, width, height);
    }

}
