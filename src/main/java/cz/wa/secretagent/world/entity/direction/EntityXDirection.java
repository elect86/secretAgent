package cz.wa.secretagent.world.entity.direction;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Direction x of an entity.
 * 
 * @author Ondrej Milenovsky
 */
public enum EntityXDirection implements Direction {
    NONE(Vector2D.ZERO),
    LEFT(new Vector2D(-1, 0)),
    RIGHT(new Vector2D(1, 0));

    private final Vector2D dir;

    private EntityXDirection(Vector2D dir) {
        this.dir = dir;
    }

    @Override
    public Vector2D getVector() {
        return dir;
    }

    public EntityDirection getDirection() {
        if (this == LEFT) {
            return EntityDirection.LEFT;
        } else if (this == RIGHT) {
            return EntityDirection.RIGHT;
        } else {
            return EntityDirection.NONE;
        }
    }

}
