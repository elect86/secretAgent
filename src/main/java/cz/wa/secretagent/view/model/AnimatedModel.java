package cz.wa.secretagent.view.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Animated model. 
 * 
 * @author Ondrej Milenovsky
 */
public class AnimatedModel extends AbstractModel {

    /** tile ids to the texture */
    private final List<TileId> tileIds;
    /** duration of all frames to cycle */
    private final long durationMs;
    /** texture for openGl */
    private transient List<TextureToDraw> textures;
    private transient int hash;

    /**
     * @param tileIds tile ids of the textures
     * @param scale scale of the model
     * @param durationMs anim cycle duration
     */
    public AnimatedModel(List<TileId> tileIds, double scale, long durationMs) {
        super(scale);
        Validate.notNull(tileIds, "tileIds is null");
        Validate.isTrue(tileIds.size() >= 1, "tileIds must contain at least 1 item");
        Validate.isTrue(durationMs > 0, "duration must be > 0");
        this.tileIds = tileIds;
        this.durationMs = durationMs;
    }

    public List<TileId> getTileIds() {
        return tileIds;
    }

    @Override
    public Set<TileId> getAllTileIds() {
        return new HashSet<TileId>(tileIds);
    }

    public long getDurationMs() {
        return durationMs;
    }

    @Override
    public ModelType getType() {
        return ModelType.ANIMATED;
    }

    @Override
    public boolean hasLinkedTextures() {
        return textures != null;
    }

    public List<TextureToDraw> getTextures() {
        return textures;
    }

    protected void setTextures(List<TextureToDraw> textures) {
        this.textures = textures;
    }

    @Override
    protected Rectangle2D linkTexturesInternal(SAMGraphics graphics) {
        textures = extractTextures(graphics, tileIds);
        if (textures != null) {
            return getModelBounds(textures.get(0).getTileBounds());
        } else {
            return null;
        }
    }

    @Override
    public Collection<TextureToDraw> getAllTextures() {
        return textures;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (int) (durationMs ^ (durationMs >>> 32));
            result = prime * result + ((tileIds == null) ? 0 : tileIds.hashCode());
            hash = result;
        }
        return hash;
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
        AnimatedModel other = (AnimatedModel) obj;
        if (durationMs != other.durationMs) {
            return false;
        }
        if (tileIds == null) {
            if (other.tileIds != null) {
                return false;
            }
        } else if (!tileIds.equals(other.tileIds)) {
            return false;
        }
        return true;
    }

}
