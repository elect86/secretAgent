package cz.wa.secretagent.game.utils;

import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.wautils.collection.Array2D;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Vector2I;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import secretAgent.view.renderer.TileId;
import secretAgent.world.SamWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Finds some info about an entity. 
 * 
 * @author Ondrej Milenovsky
 */
public class EntityObserver {
    public static final double FLOOR_MAX_DIST_STAND = 0.1;

    private final Entity entity;
    private final SamWorld world;

    public EntityObserver(Entity entity, SamWorld world) {
        this.entity = entity;
        this.world = world;
    }

    /**
     * @return true if there is wall, shelf, platform or door under the entity, false if in the air
     */
    public boolean isOnGround() {
        Rectangle2D bounds = entity.getSizeBounds().move(entity.getPos());
        return isStandingOnTile(bounds) || isStandingOnEntity(bounds);
    }

    /**
     * If the entity is standing on some other entity. The other entity can be solid usable, platform or can. 
     */
    private boolean isStandingOnEntity(Rectangle2D entityBounds) {
        for (Entity entity2 : new EntitiesFinder(world).getSolidEntities()) {
            if (isStandingOn(entityBounds, entity2.getSizeBounds().move(entity2.getPos()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the entity is standing on WALL or SHELF some tile from the world.
     */
    private boolean isStandingOnTile(Rectangle2D entityBounds) {
        TileSensing[] tilesUnder = getTilesUnder();
        return isStandingOnTile(entityBounds, tilesUnder[0]) || isStandingOnTile(entityBounds, tilesUnder[1]);
    }

    /**
     * If the entity is standing on WALL or SHELF the tile from the world.
     */
    private boolean isStandingOnTile(Rectangle2D entityBounds, TileSensing tile) {
        if ((tile.getType() == TileType.WALL) || (tile.getType() == TileType.SHELF)) {
            return isStandingOn(entityBounds, tile.getBounds());
        }
        return false;
    }

    /**
     * @return array of 2 tiles under the entity, if some tile is outside map, the type is null
     */
    public TileSensing[] getTilesUnder() {
        Vector2D tileSize = world.getLevelMap().getTileSize();
        Vector2D pos = entity.getPos();
        int ix = (int) (pos.getX() / tileSize.getX());
        int iy = (int) (pos.getY() / tileSize.getY()) + 1;

        Array2D<TileType> types = world.getLevelMap().getTypes();
        TileSensing[] ret = new TileSensing[2];
        Vector2I p1 = new Vector2I(ix, iy);
        Vector2I p2 = new Vector2I(ix + 1, iy);
        ret[0] = new TileSensing(types.isInside(p1) ? types.get(p1) : TileType.WALL, p1, tileSize);
        ret[1] = new TileSensing(types.isInside(p2) ? types.get(p2) : TileType.WALL, p2, tileSize);
        return ret;
    }

    /**
     * @param entity2 the lower entity
     * @return true if the entity is standing on entity2
     */
    public boolean isStandingOn(Entity entity2) {
        return isStandingOn(entity2.getSizeBounds().move(entity2.getPos()));
    }

    /**
     * @param entity2Bounds bounds of the lower entity
     * @return true if the entity is standing on entity2
     */
    public boolean isStandingOn(Rectangle2D entity2Bounds) {
        return isStandingOn(entity.getSizeBounds().move(entity.getPos()), entity2Bounds);
    }

    /**
     * If the entity is standing on item
     */
    private boolean isStandingOn(Rectangle2D entityBounds, Rectangle2D itemBounds) {
        if ((entityBounds.getX() >= itemBounds.getX2()) || (entityBounds.getX2() <= itemBounds.getX())) {
            return false;
        }
        double dy = FastMath.abs(itemBounds.getY() - entityBounds.getY2());
        return (dy <= FLOOR_MAX_DIST_STAND);
    }

    /**
     * @return bounds of first solid object that touches the entity or null
     */
    public Rectangle2D getTouchingSolidObject() {
        for (TileSensing tile : getCollidingTiles()) {
            if (tile.getType() == TileType.WALL) {
                return tile.getBounds();
            }
        }
        Rectangle2D bounds = entity.getSizeBounds().move(entity.getPos());
        for (Entity solid : new EntitiesFinder(world).getSolidEntities()) {
            if (solid != entity) {
                Rectangle2D solidBounds = solid.getSizeBounds().move(solid.getPos());
                if (bounds.intersects(solidBounds)) {
                    return solidBounds;
                }
            }
        }
        return null;
    }

    /**
     * @return list of all tiles that collides with the entity, the tiles don't have to be walls
     */
    public List<TileSensing> getCollidingTiles() {
        // tile index
        LevelMap map = world.getLevelMap();

        Rectangle2D eb = entity.getSizeBounds().move(entity.getPos());

        Vector2D tileSize = map.getTileSize();
        int ix1 = (int) (eb.getX() / tileSize.getX() + 0.5);
        int iy1 = (int) (eb.getY() / tileSize.getY() + 0.5);
        int ix2 = (int) (eb.getX2() / tileSize.getX() + 0.5);
        int iy2 = (int) (eb.getY2() / tileSize.getY() + 0.5);

        List<TileSensing> ret = new ArrayList<TileSensing>((ix2 - ix1 + 1) * (iy2 - iy1 + 1));
        for (int y = iy1; y <= iy2; y++) {
            for (int x = ix1; x <= ix2; x++) {
                Vector2I i = new Vector2I(x, y);
                if (map.getTypes().isInside(i)) {
                    ret.add(new TileSensing(map.getTypes().get(i), i, tileSize));
                }
            }
        }
        return ret;
    }

    /**
     * Get 9 closest tiles around the entity.
     * @param tileIds return only tiles with these ids
     * @return list of tiles
     */
    public List<TileWithPosition> get9TilesAround(Collection<TileId> tileIds) {
        Array2D<TileType> types = world.getLevelMap().getTypes();
        Array2D<List<Tile>> background = world.getLevelMap().getBackground();
        Array2D<List<Tile>> foreground = world.getLevelMap().getForeground();
        Vector2D tileSize = world.getLevelMap().getTileSize();

        List<TileWithPosition> ret = new ArrayList<TileWithPosition>(9);
        int x1 = (int) FastMath.round(entity.getPos().getX() / tileSize.getX());
        int y1 = (int) FastMath.round(entity.getPos().getY() / tileSize.getY());
        for (int iy = y1 - 1; iy <= y1 + 1; iy++) {
            for (int ix = x1 - 1; ix <= x1 + 1; ix++) {
                Vector2I i = new Vector2I(ix, iy);
                if (types.isInside(i)) {
                    findTiles(tileIds, background, tileSize, ret, i);
                    findTiles(tileIds, foreground, tileSize, ret, i);
                }
            }
        }
        return ret;
    }

    /**
     * Finds and adds to the list tiles at the position with specified ids
     */
    private void findTiles(Collection<TileId> tileIds, Array2D<List<Tile>> background, Vector2D tileSize,
            List<TileWithPosition> ret, Vector2I i) {
        for (Tile tile : background.get(i)) {
            for (TileId tileId : tileIds) {
                if (tile.hasTileId(tileId)) {
                    ret.add(new TileWithPosition(tileId, tile, i, tileSize));
                }
            }
        }
    }
}
