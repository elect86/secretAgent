package cz.wa.secretagent.view.model;

import java.awt.Color;
import java.util.List;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.view.TileId;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Model of laser, same as animated but is drawn different way.
 * The model returns MAX_BOUNDS, that means the model itself is always in view.
 * LaserModelDrawer should decide, if the model is really in the view, but it is not possible now.
 *
 * @author Ondrej Milenovsky
 */
public class LaserModel extends AnimatedModel {

    private Color color;

    public LaserModel(List<TileId> tileIds, long durationMs, Color color) {
        super(tileIds, 1, durationMs);
        setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        Validate.notNull(color, "color is null");
        this.color = color;
    }

    @Override
    public Rectangle2D getMaxBounds() {
        return MAX_BOUNDS;
    }

    @Override
    public ModelType getType() {
        return ModelType.LASER;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((color == null) ? 0 : color.hashCode());
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
        LaserModel other = (LaserModel) obj;
        if (color == null) {
            if (other.color != null) {
                return false;
            }
        } else if (!color.equals(other.color)) {
            return false;
        }
        return true;
    }

    public LaserModel copyWithColor(Color color) {
        LaserModel ret = new LaserModel(getTileIds(), getDurationMs(), color);
        ret.setTextures(getTextures());
        return ret;
    }

}
