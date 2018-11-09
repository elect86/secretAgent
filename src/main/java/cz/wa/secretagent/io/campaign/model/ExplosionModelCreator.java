package cz.wa.secretagent.io.campaign.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
import secretAgent.view.model.ExplosionModel;
import secretAgent.world.GLModel;

/**
 * Creates explosion model. 
 * 
 * @author Ondrej Milenovsky
 */
public class ExplosionModelCreator extends AbstractModelCreator {
    private static final long serialVersionUID = 4746311062870890398L;

    private static final Logger logger = LoggerFactory.getLogger(ExplosionModelCreator.class);

    private static final String FRAMES_PROPERTY = "frames";

    @Override
    public ExplosionModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
        // frames
        List<TileId> tileIds = modelProperties.getProperties().get(FRAMES_PROPERTY);
        if (tileIds == null) {
            logger.warn("Explosion model '" + modelName + "' must have property: " + FRAMES_PROPERTY);
            return null;
        }
        return new ExplosionModel(tileIds);
    }

    @Override
    public ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName) {
        return new ModelInfo(0, model);
    }

}
