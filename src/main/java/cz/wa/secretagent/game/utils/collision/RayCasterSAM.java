package cz.wa.secretagent.game.utils.collision;

import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.utils.raycaster.RayCaster;
import cz.wa.secretagent.utils.raycaster.RayHit;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.wautils.collection.Array2D;
import cz.wa.wautils.math.Vector2I;

/**
 * Ray casting.
 * 
 * @author Ondrej Milenovsky
 */
public class RayCasterSAM extends RayCaster {
    private final LevelMap map;
    private final Set<TileType> hitTypes;

    public RayCasterSAM(Vector2D startPos, Vector2D dir, double maxDist, LevelMap map, Set<TileType> hitTypes) {
        super(convertTo1(startPos, map.getTileSize()), dir, maxDist / map.getTileSize().getX());
        this.map = map;
        this.hitTypes = hitTypes;
    }

    /**
     * Converts vector from world position to coords where 0:0 is upper left corner and tile size is 1x1.
     * @param v
     * @param tileSize
     * @return
     */
    private static Vector2D convertTo1(Vector2D v, Vector2D tileSize) {
        return new Vector2D(0.5 + v.getX() / tileSize.getX(), 0.5 + v.getY() / tileSize.getY());
    }

    /**
     * Inverse to convertTo1
     * @param v
     * @param tileSize
     * @return
     */
    private static Vector2D convertFrom1(Vector2D v, Vector2D tileSize) {
        return new Vector2D((v.getX() - 0.5) * tileSize.getX(), (v.getY() - 0.5) * tileSize.getY());
    }

    @Override
    public RayHit castRay() {
        RayHit ret = super.castRay();
        if (ret == null) {
            return null;
        } else {
            return new RayHit(ret.getMapPos(), convertFrom1(ret.getHitPos(), map.getTileSize()));
        }
    }

    @Override
    protected boolean isWall(Vector2I i) {
        Array2D<TileType> types = map.getTypes();
        if (types.isInside(i)) {
            return hitTypes.contains(types.get(i));
        } else {
            return true;
        }
    }

}
