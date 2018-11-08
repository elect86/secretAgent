package cz.wa.secretagent.view.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.Constants;
import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Model of an agent. 
 * 
 * @author Ondrej Milenovsky
 */
public class AgentModel extends AbstractModel {

    private static final double MAX_BOUNDS_TILES = 2;
    /** max bounds to decide if the model is in viewport, default is 2 tiles to each direction from center */
    private static final Rectangle2D MAX_BOUNDS = new Rectangle2D(-Constants.TILE_SIZE.getX()
            * MAX_BOUNDS_TILES, -Constants.TILE_SIZE.getY() * MAX_BOUNDS_TILES, Constants.TILE_SIZE.getX()
            * MAX_BOUNDS_TILES * 2, Constants.TILE_SIZE.getY() * MAX_BOUNDS_TILES * 2);

    /** duration of frames when standing */
    private final long stayDurationMs;
    /** textures when turned left (can be null) */
    private final AgentTextures leftTextures;
    /** textures when turned right (can be null) */
    private final AgentTextures rightTextures;
    /** center position for gun when wielding one (when turned right) */
    private final Vector2D weaponCenter;

    /**
     * @param leftTextures textures when looking left
     * @param rightTextures textures wnen looking right
     * @param bounds model draw bounds, if null, will be computed from the texture
     * @param stayDurationMs anim cycle duration when standing
     * @param weaponCenter weapon rotation center
     */
    public AgentModel(AgentTextures leftTextures, AgentTextures rightTextures, double scale,
            long stayDurationMs, Vector2D weaponCenter) {
        super(scale);
        Validate.isTrue((leftTextures != null) || (rightTextures != null), "both textures are null");
        Validate.notNull(weaponCenter, "weaponCenter is null");
        this.stayDurationMs = stayDurationMs;
        this.leftTextures = leftTextures;
        this.rightTextures = rightTextures;
        this.weaponCenter = weaponCenter;
    }

    @Override
    public Set<TileId> getAllTileIds() {
        Set<TileId> ret = new HashSet<TileId>();
        if (leftTextures != null) {
            ret.addAll(leftTextures.getStayIds());
            ret.addAll(leftTextures.getMoveIds());
            ret.addAll(leftTextures.getJumpIds());
            ret.addAll(leftTextures.getDeathIds());
        }
        if (rightTextures != null) {
            ret.addAll(rightTextures.getStayIds());
            ret.addAll(rightTextures.getMoveIds());
            ret.addAll(rightTextures.getJumpIds());
            ret.addAll(rightTextures.getDeathIds());
        }
        return ret;
    }

    public long getStayDurationMs() {
        return stayDurationMs;
    }

    public Vector2D getWeaponCenter() {
        return weaponCenter;
    }

    public AgentTextures getLeftTextures() {
        return leftTextures;
    }

    public AgentTextures getRightTextures() {
        return rightTextures;
    }

    @Override
    public ModelType getType() {
        return ModelType.AGENT;
    }

    @Override
    public Rectangle2D getMaxBounds() {
        return MAX_BOUNDS;
    }

    @Override
    public boolean hasLinkedTextures() {
        return ((leftTextures != null) && leftTextures.hasLinkedTextures())
                || ((rightTextures != null) && rightTextures.hasLinkedTextures());
    }

    @Override
    protected Rectangle2D linkTexturesInternal(SAMGraphics graphics) {
        Rectangle2D modelBounds = null;
        if (leftTextures != null) {
            leftTextures.linkTextures(graphics);
            if (leftTextures.getStayTextures() != null) {
                modelBounds = getModelBounds(leftTextures.getStayTextures().get(0).getTileBounds());
            }
        }
        if (rightTextures != null) {
            rightTextures.linkTextures(graphics);
            if (rightTextures.getStayTextures() != null) {
                modelBounds = getModelBounds(rightTextures.getStayTextures().get(0).getTileBounds());
            }
        }
        return modelBounds;
    }

    @Override
    protected Collection<TextureToDraw> getAllTextures() {
        List<TextureToDraw> ret = new ArrayList<TextureToDraw>();
        if (leftTextures != null) {
            ret.addAll(leftTextures.getAllTextures());
        }
        if (rightTextures != null) {
            ret.addAll(rightTextures.getAllTextures());
        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((leftTextures == null) ? 0 : leftTextures.hashCode());
        result = prime * result + ((rightTextures == null) ? 0 : rightTextures.hashCode());
        result = prime * result + (int) (stayDurationMs ^ (stayDurationMs >>> 32));
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
        AgentModel other = (AgentModel) obj;
        if (leftTextures == null) {
            if (other.leftTextures != null) {
                return false;
            }
        } else if (!leftTextures.equals(other.leftTextures)) {
            return false;
        }
        if (rightTextures == null) {
            if (other.rightTextures != null) {
                return false;
            }
        } else if (!rightTextures.equals(other.rightTextures)) {
            return false;
        }
        if (stayDurationMs != other.stayDurationMs) {
            return false;
        }
        return true;
    }

}
