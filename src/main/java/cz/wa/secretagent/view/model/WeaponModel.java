package cz.wa.secretagent.view.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Model of weapon, consists of two models, one for wielding the weapon, second for firing 
 * 
 * @author Ondrej Milenovsky
 */
public class WeaponModel extends AbstractModel {
    private final AnimatedModel wieldModel;
    private final Vector2D wieldModelCenter;
    private final AnimatedModel fireModel;
    private final Vector2D fireModelCenter;

    private transient int hash;

    public WeaponModel(AnimatedModel wieldModel, Vector2D wieldModelCenter, AnimatedModel fireModel,
            Vector2D fireModelCenter, double scale) {
        super(scale);
        this.wieldModel = wieldModel;
        this.wieldModelCenter = wieldModelCenter;
        this.fireModel = fireModel;
        this.fireModelCenter = fireModelCenter;
    }

    @Override
    public Rectangle2D getMaxBounds() {
        return MAX_BOUNDS;
    }

    public AnimatedModel getModel(boolean firing) {
        if (firing) {
            return fireModel;
        } else {
            return wieldModel;
        }
    }

    public Vector2D getCenter(boolean firing) {
        if (firing) {
            return fireModelCenter;
        } else {
            return wieldModelCenter;
        }
    }

    @Override
    public ModelType getType() {
        return ModelType.WEAPON;
    }

    @Override
    public Set<TileId> getAllTileIds() {
        Set<TileId> ret = new HashSet<TileId>();
        ret.addAll(wieldModel.getAllTileIds());
        ret.addAll(fireModel.getAllTileIds());
        return ret;
    }

    @Override
    public boolean hasLinkedTextures() {
        return wieldModel.hasLinkedTextures() && fireModel.hasLinkedTextures();
    }

    @Override
    protected Rectangle2D linkTexturesInternal(SAMGraphics graphics) {
        wieldModel.linkTextures(graphics);
        fireModel.linkTextures(graphics);
        return wieldModel.getBounds();
    }

    @Override
    protected Collection<TextureToDraw> getAllTextures() {
        List<TextureToDraw> ret = new ArrayList<TextureToDraw>();
        ret.addAll(wieldModel.getAllTextures());
        ret.addAll(fireModel.getAllTextures());
        return ret;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((fireModel == null) ? 0 : fireModel.hashCode());
            result = prime * result + ((fireModelCenter == null) ? 0 : fireModelCenter.hashCode());
            result = prime * result + ((wieldModel == null) ? 0 : wieldModel.hashCode());
            result = prime * result + ((wieldModelCenter == null) ? 0 : wieldModelCenter.hashCode());
            hash = result;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        WeaponModel other = (WeaponModel) obj;
        if (fireModel == null) {
            if (other.fireModel != null)
                return false;
        } else if (!fireModel.equals(other.fireModel))
            return false;
        if (fireModelCenter == null) {
            if (other.fireModelCenter != null)
                return false;
        } else if (!fireModelCenter.equals(other.fireModelCenter))
            return false;
        if (wieldModel == null) {
            if (other.wieldModel != null)
                return false;
        } else if (!wieldModel.equals(other.wieldModel))
            return false;
        if (wieldModelCenter == null) {
            if (other.wieldModelCenter != null)
                return false;
        } else if (!wieldModelCenter.equals(other.wieldModelCenter))
            return false;
        return true;
    }

}
