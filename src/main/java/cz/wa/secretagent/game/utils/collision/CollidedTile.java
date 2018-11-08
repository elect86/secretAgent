package cz.wa.secretagent.game.utils.collision;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.utils.raycaster.RayHit;
import cz.wa.wautils.math.Vector2I;

/**
  * Class describing collision of projectile with tile.
  * 
  * @author Ondrej Milenovsky
  */
public class CollidedTile implements CollisionDescriptor {
    private final Vector2I tilePos;
    private final Vector2D hitPos;

    public CollidedTile(RayHit rayHit) {
        tilePos = rayHit.getMapPos();
        hitPos = rayHit.getHitPos();
    }

    public CollidedTile(Vector2I tilePos, Vector2D hitPos) {
        this.tilePos = tilePos;
        this.hitPos = hitPos;
    }

    /**
     * @return point where the projectile hit the tile
     */
    @Override
    public Vector2D getHitPos() {
        return hitPos;
    }

    /**
     * @return position of the tile in the grid
     */
    public Vector2I getTilePos() {
        return tilePos;
    }
}
