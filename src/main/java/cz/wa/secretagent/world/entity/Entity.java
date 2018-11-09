package cz.wa.secretagent.world.entity;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.Constants;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.ObjectModel;

/**
 * Entity is item, agent or door. 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class Entity {

    public static final Rectangle2D DEFAULT_BOUNDS = new Rectangle2D(-Constants.TILE_SIZE.getX() / 2.0,
            -Constants.TILE_SIZE.getY() / 2.0, Constants.TILE_SIZE.getX(), Constants.TILE_SIZE.getY());

    private final ObjectModel model;

    /** position */
    private Vector2D pos;
    /** physical size */
    private Rectangle2D sizeBounds;
    /** current speed */
    private Vector2D speed;
    /** if cannot move */
    private boolean staticPos;

    public Entity(ObjectModel model, Vector2D pos) {
        this(model, pos, DEFAULT_BOUNDS, Vector2D.ZERO, true);
    }

    public Entity(ObjectModel model, Vector2D pos, Rectangle2D sizeBounds, Vector2D speed, boolean staticPos) {
        this.model = model;
        this.pos = pos;
        this.sizeBounds = sizeBounds;
        this.speed = speed;
        this.staticPos = staticPos;
    }

    public ObjectModel getModel() {
        return model;
    }

    /**
     * @return primary type of the entity (never use instanceof !)
     */
    public abstract EntityType getType();

    /**
     * @return secondary type of the entity (never use instanceof !)
     */
    public abstract EntityType2 getSecondType();

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public Rectangle2D getSizeBounds() {
        return sizeBounds;
    }

    public void setSizeBounds(Rectangle2D sizeBounds) {
        this.sizeBounds = sizeBounds;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public boolean isStaticPos() {
        return staticPos;
    }

    public void setStaticPos(boolean staticPos) {
        this.staticPos = staticPos;
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
