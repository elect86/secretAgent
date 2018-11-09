package cz.wa.secretagent.io.campaign.model;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.model.AnimatedModel;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
import secretAgent.view.model.WeaponModel;
import secretAgent.world.GLModel;

/**
 * Creates weapon model.
 *
 * @author Ondrej Milenovsky
 */
public class WeaponModelCreator extends AbstractModelCreator {
    private static final long serialVersionUID = 8116144786714680816L;

    private static final Logger logger = LoggerFactory.getLogger(WeaponModelCreator.class);

    private static final long DEFAULT_DURATION_MS = 1000;
    private static final double DEFAULT_SCALE = 1;

    private static final String WIELD_FRAMES_PROPERTY = "wieldFrames";
    private static final String WIELD_DURATION_PROPERTY = "wieldDuration";
    private static final String WIELD_CENTER_PROPERTY = "wieldCenter";
    private static final String FIRE_FRAMES_PROPERTY = "fireFrames";
    private static final String FIRE_CENTER_PROPERTY = "fireCenter";

    @Override
    public WeaponModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
        // wield frames
        List<TileId> wieldTileIds = modelProperties.getProperties().get(WIELD_FRAMES_PROPERTY);
        if (wieldTileIds == null) {
            logger.warn("Weapon model '" + modelName + "' must have property: " + WIELD_FRAMES_PROPERTY);
            return null;
        }
        // fire frames
        List<TileId> fireTileIds = modelProperties.getProperties().get(FIRE_FRAMES_PROPERTY);
        if (fireTileIds == null) {
            logger.warn("Weapon model '" + modelName + "' must have property: " + FIRE_FRAMES_PROPERTY);
            return null;
        }
        // duration
        long durationMs = DEFAULT_DURATION_MS;
        TileId duration = getSingleTileId(modelProperties.getProperties(), WIELD_DURATION_PROPERTY,
                modelName);
        if (duration != null) {
            durationMs = duration.getTileId();
        }
        // wield center
        Vector2D wieldCenter = getVector(modelProperties.getProperties(), WIELD_CENTER_PROPERTY, modelName);
        // fire center
        Vector2D fireCenter = getVector(modelProperties.getProperties(), FIRE_CENTER_PROPERTY, modelName);
        // scale
        double scale = DEFAULT_SCALE;
        TileId sTile = getSingleTileId(modelProperties.getProperties(), SCALE_PROPERTY, modelName);
        if (sTile != null) {
            scale = tileToDouble(sTile);
        }
        // the fire model does not need duration (duration = reload time)
        AnimatedModel fireModel = new AnimatedModel(fireTileIds, scale, durationMs);
        AnimatedModel wieldModel = new AnimatedModel(wieldTileIds, scale, durationMs);
        return new WeaponModel(wieldModel, wieldCenter, fireModel, fireCenter, scale);
    }

    @Override
    public ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName) {
        return new ModelInfo(model);
    }

}
