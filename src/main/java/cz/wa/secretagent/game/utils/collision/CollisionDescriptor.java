package cz.wa.secretagent.game.utils.collision;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Object describing collision of projectile with something. 
 * 
 * @author Ondrej Milenovsky
 */
public interface CollisionDescriptor {
    /**
     * @return point where the projectile hit something
     */
    Vector2D getHitPos();
}
