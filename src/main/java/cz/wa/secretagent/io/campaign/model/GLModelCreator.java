package cz.wa.secretagent.io.campaign.model;

import java.io.Serializable;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
import secretAgent.world.GLModel;

/**
 * Creates model from parsed information. 
 * 
 * @author Ondrej Milenovsky
 */
public interface GLModelCreator extends Serializable {

    /**
     * Creates model from properties and tileId.
     * @param modelProperties model properties
     * @param tileId tile id from which is the model created
     * @param modelName model name used for error messages
     * @return new model or null
     */
    GLModel createModel(ModelProperties modelProperties, TileId tileId, String modelName);

    /**
     * Creates model info from model and tileId
     * @param tileId tile id from which is the model created
     * @param model model to use
     * @param modelName model name used for error messages
     * @return new model info or null
     */
    ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName);
}
