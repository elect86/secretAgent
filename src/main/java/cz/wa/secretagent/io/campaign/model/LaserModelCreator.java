package cz.wa.secretagent.io.campaign.model;

import java.awt.Color;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import secretAgent.view.model.AnimatedModel;
import secretAgent.view.model.LaserModel;
import secretAgent.view.renderer.TileId;

/**
 * Creates laser model, same as animated model.
 * 
 * @author Ondrej Milenovsky
 */
public class LaserModelCreator extends AnimatedModelCreator {
    private static final long serialVersionUID = -6371384148477865812L;

    private static final Logger logger = LoggerFactory.getLogger(LaserModelCreator.class);

    private static final String COLOR_PROPERTY = "color";

    @Override
    public LaserModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
        AnimatedModel model = super.createModel(modelProperties, tileId, modelName);
        List<TileId> list = modelProperties.getProperties().get(COLOR_PROPERTY);
        Color color = Color.WHITE;
        if ((list != null) && !list.isEmpty()) {
            if (list.size() > 1) {
                logger.warn("Laser model '" + modelName + "' should define single color: " + tileId);
            }
            color = new Color(list.get(0).getTileId());
        } else {
            logger.warn("Laser model '" + modelName + "' does not define color: " + tileId);
        }
        return new LaserModel(model.getTileIds(), model.getDurationMs(), color);
    }
}
