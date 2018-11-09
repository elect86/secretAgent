package cz.wa.secretagent.view.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.Constants;
import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.texture.GLGraphics;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.GLModel;

/**
 * Abstract model taking care of determining transparent model.
 *
 * @author Ondrej Milenovsky
 */
public abstract class AbstractModel implements GLModel {
    protected static final Rectangle2D DEFAULT_BOUNDS = new Rectangle2D(-Constants.TILE_SIZE.getX() / 2.0,
            -Constants.TILE_SIZE.getY() / 2.0, Constants.TILE_SIZE.getX(), Constants.TILE_SIZE.getY());
    protected static final Rectangle2D MAX_BOUNDS = new Rectangle2D(-10000, -10000, 100000, 100000);

    /** where the texture will be drawn according to object center, by default used as max bounds */
    private transient Rectangle2D bounds;
    /** if != 0, then bounds will be conputed from texture, otherwise bounds must be defined */
    private final double scale;
    /** if the texture has at least 1 transparent pixel */
    private transient Boolean transparent;

    /**
     * @param bounds model draw bounds, if null, will be computed from the texture
     */
    public AbstractModel(Rectangle2D bounds, double scale) {
        Validate.isTrue((scale == 0) != (bounds == null), "bounds must be null xor scale != 0");
        this.bounds = bounds;
        this.scale = scale;
    }

    public AbstractModel(double scale) {
        if (scale == 0) {
            this.bounds = DEFAULT_BOUNDS;
        } else {
            this.bounds = null;
        }
        this.scale = scale;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    @Override
    public Rectangle2D getMaxBounds() {
        return bounds;
    }

    public double getScale() {
        return scale;
    }

    /**
     * @return if the texture has at least 1 transparent pixel
     */
    @Override
    public boolean isTransparent() {
        if (transparent == null) {
            Collection<TextureToDraw> textures = getAllTextures();
            if (textures == null) {
                return true;
            }
            transparent = false;
            for (TextureToDraw tex : textures) {
                if ((tex != null) && tex.isTransparent()) {
                    transparent = true;
                    break;
                }
            }
        }
        return transparent;
    }

    @Override
    public final void linkTextures(SAMGraphics graphics) {
        Rectangle2D newBounds = linkTexturesInternal(graphics);
        if (scale != 0) {
            bounds = newBounds;
        }
        isTransparent();
    }

    /**
     * Links textures from tile ids
     * @param graphics textures holder
     * @return model bounds (used if initialized to null)
     */
    protected abstract Rectangle2D linkTexturesInternal(SAMGraphics graphics);

    public static List<TextureToDraw> extractTextures(SAMGraphics graphics, List<TileId> tileIds) {
        GLGraphics gr = (GLGraphics) graphics;
        List<TextureToDraw> textures = new ArrayList<TextureToDraw>(tileIds.size());
        for (TileId tileId : tileIds) {
            TextureToDraw tex = gr.getTile(tileId);
            if (tex != null) {
                textures.add(tex);
            }
        }
        if (textures.isEmpty()) {
            return null;
        }
        return textures;
    }

    /**
     * @return collection of textures, that this model can use, used to determine if the model is transparent
     */
    protected abstract Collection<TextureToDraw> getAllTextures();

    protected static Rectangle2D getModelBounds(Rectangle2D tileBounds) {
        return tileBounds.move(new Vector2D(-tileBounds.getWidth() / 2.0, -tileBounds.getHeight() / 2.0));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(scale);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractModel other = (AbstractModel) obj;
        if (Double.doubleToLongBits(scale) != Double.doubleToLongBits(other.scale))
            return false;
        return true;
    }

}
