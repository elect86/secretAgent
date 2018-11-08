package cz.wa.secretagent.io.campaign.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.view.TileId;

/**
 * Abstract model creator with util methods. 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class AbstractModelCreator implements GLModelCreator {
    private static final long serialVersionUID = 4249585509120419992L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractModelCreator.class);

    protected static final String SCALE_PROPERTY = "scale";

    /**
     * Converts tile id to double
     */
    protected static double tileToDouble(TileId tileId) {
        return Double.parseDouble(tileId.getTileSetId() + "." + tileId.getTileId());
    }

    /**
     * If the properties contains the property with at least 2 values, creates vector.
     * If the property has different number of values, prints warnings.
     * If the vector cannot be parsed, returns zero vector.
     */
    protected static Vector2D getVector(Map<String, List<TileId>> properties, String property,
            String modelName) {
        Vector2D ret = Vector2D.ZERO;
        if (properties.containsKey(property)) {
            List<TileId> list = properties.get(property);
            if (list.size() != 2) {
                logger.warn("Weapon model'" + modelName + "'." + property + " must have 2 value, but has "
                        + list.size());
            }
            if (list.size() >= 2) {
                ret = new Vector2D(tileToDouble(list.get(0)), tileToDouble(list.get(1)));
            }
        }
        return ret;
    }

    /**
     * Extracts first tile from the property list, logs warnings, returns null if no value
     */
    protected static TileId getSingleTileId(Map<String, List<TileId>> properties, String property,
            String modelName) {
        List<TileId> list = properties.get(property);
        if (list != null) {
            if (list.size() != 1) {
                logger.warn("Weapon model'" + modelName + "'." + property + " must have 1 value, but has "
                        + list.size());
            }
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }
}
