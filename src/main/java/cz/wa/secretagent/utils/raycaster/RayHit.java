package cz.wa.secretagent.utils.raycaster;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.wautils.math.Vector2I;

/**
 * Class returned by raycaster. Holds hit map tile and hit point.
 * 
 * @author Ondrej Milenovsky
 */
public class RayHit {
    private final Vector2I mapPos;
    private final Vector2D hitPos;

    public RayHit(Vector2I mapPos, Vector2D hitPos) {
        super();
        this.mapPos = mapPos;
        this.hitPos = hitPos;
    }

    public Vector2I getMapPos() {
        return mapPos;
    }

    public Vector2D getHitPos() {
        return hitPos;
    }
}
