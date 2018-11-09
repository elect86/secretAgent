package cz.wa.secretagent.view.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.texture.GLGraphics;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.view.model.AbstractModel;

/**
 * Simple not animated sprite. 
 * 
 * @author Ondrej Milenovsky
 */
public class SimpleModel extends AbstractModel {

    /** tile id to the texture */
    private final TileId tileId;
    /** texture for openGl */
    private transient TextureToDraw texture;

    public SimpleModel(TileId tileId) {
        this(tileId, DEFAULT_BOUNDS, 0);
    }

    public SimpleModel(TileId tileId, double scale) {
        this(tileId, (scale == 0) ? DEFAULT_BOUNDS : null, scale);
    }

    /**
     * @param tileId tile id of the texture
     * @param bounds model draw bounds, if null, will be computed from the texture
     */
    public SimpleModel(TileId tileId, Rectangle2D bounds, double scale) {
        super(bounds, scale);
        Validate.notNull(tileId, "tileId is null");
        this.tileId = tileId;
    }

    public TileId getTileId() {
        return tileId;
    }

    @Override
    public Set<TileId> getAllTileIds() {
        return Collections.singleton(tileId);
    }

    public TextureToDraw getTexture() {
        return texture;
    }

    protected void setTexture(TextureToDraw texture) {
        this.texture = texture;
    }

    @Override
    public ModelType getType() {
        return ModelType.SIMPLE;
    }

    @Override
    protected Rectangle2D linkTexturesInternal(SAMGraphics graphics) {
        TextureToDraw tile = ((GLGraphics) graphics).getTile(tileId);
        setTexture(tile);
        if (tile != null) {
            return getModelBounds(tile.getTileBounds());
        } else {
            return null;
        }
    }

    @Override
    public boolean hasLinkedTextures() {
        return texture != null;
    }

    @Override
    protected Collection<TextureToDraw> getAllTextures() {
        return Collections.singletonList(texture);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((tileId == null) ? 0 : tileId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SimpleModel other = (SimpleModel) obj;
        if (tileId == null) {
            if (other.tileId != null) {
                return false;
            }
        } else if (!tileId.equals(other.tileId)) {
            return false;
        }
        return true;
    }

}
