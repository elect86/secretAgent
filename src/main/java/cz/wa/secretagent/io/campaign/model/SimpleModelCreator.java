package cz.wa.secretagent.io.campaign.model;

import java.util.List;
import java.util.Map;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
import secretAgent.view.model.SimpleModel;
import secretAgent.world.GLModel;

/**
 * Creates simple model. 
 * 
 * @author Ondrej Milenovsky
 */
public class SimpleModelCreator extends AbstractModelCreator {
    private static final long serialVersionUID = 620155121207717964L;

    private static final String FRAME_PROPERTY = "frame";

    @Override
    public SimpleModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
        Map<String, List<TileId>> properties = modelProperties.getProperties();
        // frame
        TileId tileId2 = getSingleTileId(properties, FRAME_PROPERTY, modelName);
        if (tileId2 != null) {
            tileId = tileId2;
        }
        // scale
        double scale = 0;
        TileId scaleTile = getSingleTileId(properties, SCALE_PROPERTY, modelName);
        if (scaleTile != null) {
            scale = tileToDouble(scaleTile);
        }
        return new SimpleModel(tileId, scale);
    }

    @Override
    public ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName) {
        return new ModelInfo(model);
    }

}
