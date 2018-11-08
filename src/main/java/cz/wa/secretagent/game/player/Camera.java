package cz.wa.secretagent.game.player;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;

/**
 * Camera looking on the world.
 * 
 * @author Ondrej Milenovsky
 */
public class Camera {
    private Vector2D pos;
    private double scale;
    private Vector2I screenSize;
    private Rectangle2I viewport;
    private Rectangle2D worldBounds;
    private Rectangle2I hudBounds;
    private boolean drawingHud;

    /**
     * Creates world camera
     * @param pos center of the camera in world coords
     * @param scale drawn scale
     * @param screenSize screen size
     * @param viewport viewport on screen
     */
    public Camera(Vector2D pos, double scale, Vector2I screenSize, Rectangle2I viewport) {
        setPos(pos);
        setScale(scale);
        setScreenSize(screenSize);
        setViewport(viewport);
        drawingHud = false;
    }

    /**
     * Creates camera for HUD
     * @param screenSize
     */
    public Camera(Vector2I screenSize, double scale) {
        this.screenSize = screenSize;
        this.scale = scale;
        pos = Vector2D.ZERO;
        viewport = new Rectangle2I(0, 0);
        drawingHud = true;
    }

    /**
     * @return center position in world where is the camera looking
     */
    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        Validate.notNull(pos, "pos is null");
        this.pos = pos;
        worldBounds = null;
    }

    /**
     * @return pixel scale, 2.0 means 1 pixel in world will be displayed as 2x2 square 
     */
    public double getScale() {
        return scale;
    }

    /**
     * @param screenSize total screen size in pixels
     */
    public void setScreenSize(Vector2I screenSize) {
        this.screenSize = screenSize;
    }

    public Vector2I getScreenSize() {
        return screenSize;
    }

    public void setScale(double scale) {
        Validate.isTrue(scale != 0, "scale is 0");
        this.scale = scale;
        worldBounds = null;
    }

    /**
     * @return viewport in screen pixels
     */
    public Rectangle2I getViewport() {
        return viewport;
    }

    public void setViewport(Rectangle2I viewport) {
        Validate.notNull(viewport, "viewport is null");
        this.viewport = viewport;
        worldBounds = null;
        // TODO better bounds
        hudBounds = new Rectangle2I(0, viewport.getY2(), screenSize.getX(), screenSize.getY()
                - viewport.getY2());
    }

    /**
     * @return bounds where the camera can see in the world, null if used HUD camera
     */
    public Rectangle2D getWorldBounds() {
        if (viewport.getWidth() == 0) {
            return null;
        }
        if (worldBounds == null) {
            worldBounds = computeWorldBounds();
        }
        return worldBounds;
    }

    /**
     * Ensures that the camera does not see outside the map.
     * @param mapBounds max visible bounds
     */
    public void limitInMap(Rectangle2D mapBounds) {
        Rectangle2D wb = getWorldBounds();
        // check scale
        if (wb.getWidth() > mapBounds.getWidth()) {
            scale = mapBounds.getWidth() / wb.getWidth();
            wb = computeWorldBounds();
        }
        if (wb.getHeight() > mapBounds.getHeight()) {
            scale = mapBounds.getHeight() / wb.getHeight();
            wb = computeWorldBounds();
        }
        // check pos
        boolean moved = false;
        // upper left corner
        if (wb.getX() < mapBounds.getX()) {
            pos = pos.add(new Vector2D(mapBounds.getX() - wb.getX(), 0));
            moved = true;
        }
        if (wb.getY() < mapBounds.getY()) {
            pos = pos.add(new Vector2D(0, mapBounds.getY() - wb.getY()));
            moved = true;
        }
        if (moved) {
            wb = computeWorldBounds();
        }
        worldBounds = wb;
        // lower right corner
        if (wb.getX2() > mapBounds.getX2()) {
            pos = pos.add(new Vector2D(mapBounds.getX2() - wb.getX2(), 0));
            moved = true;
        }
        if (wb.getY2() > mapBounds.getY2()) {
            pos = pos.add(new Vector2D(0, mapBounds.getY2() - wb.getY2()));
            moved = true;
        }
        if (moved) {
            worldBounds = null;
        }
    }

    private Rectangle2D computeWorldBounds() {
        double scale = this.scale;
        if (drawingHud) {
            scale = 1;
        }
        double wbw = viewport.getWidth() / scale;
        double wbh = viewport.getHeight() / scale;
        return new Rectangle2D(pos.getX() - wbw / 2, pos.getY() - wbh / 2, wbw, wbh);
    }

    public Vector2D getScreenPos(Vector2D worldPos) {
        double scale = this.scale;
        if (drawingHud) {
            scale = 1;
        }
        return worldPos.subtract(pos).scalarMultiply(scale)
                .add(new Vector2D(viewport.getWidth() / 2.0, viewport.getHeight() / 2.0));
    }

    public Rectangle2I getHudBounds() {
        return hudBounds;
    }

}
