package cz.wa.secretagent.world.entity.direction;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Direction of an entity.
 * 
 * @author Ondrej Milenovsky
 */
public enum EntityDirection implements Direction {
    NONE(Vector2D.ZERO),
    LEFT(new Vector2D(-1, 0)),
    RIGHT(new Vector2D(1, 0)),
    UP(new Vector2D(0, -1)),
    DOWN(new Vector2D(0, 1));

    private final Vector2D dir;

    private EntityDirection(Vector2D dir) {
        this.dir = dir;
    }

    @Override
    public Vector2D getVector() {
        return dir;
    }

    public EntityXDirection getDirectionX() {
        if (this == NONE) {
            return EntityXDirection.NONE;
        } else if (this == LEFT) {
            return EntityXDirection.LEFT;
        } else if (this == RIGHT) {
            return EntityXDirection.RIGHT;
        } else {
            throw new IllegalStateException("Direction is not x: " + toString());
        }
    }

    public EntityYDirection getDirectionY() {
        if (this == NONE) {
            return EntityYDirection.NONE;
        } else if (this == UP) {
            return EntityYDirection.UP;
        } else if (this == DOWN) {
            return EntityYDirection.DOWN;
        } else {
            throw new IllegalStateException("Direction is not y: " + toString());
        }
    }

}
