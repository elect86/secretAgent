package cz.wa.secretagent.io.campaign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.model.GLModel;
import cz.wa.secretagent.view.model.ModelType;
import cz.wa.secretagent.view.model.SimpleModel;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;

/**
 * Creates model infos that are immutable singletons. 
 * 
 * @author Ondrej Milenovsky
 */
public class ModelFactory implements Serializable {
    private static final long serialVersionUID = -4885331968864703472L;

    private static final Logger logger = LoggerFactory.getLogger(ModelFactory.class);

    private Map<ModelType, GLModelCreator> modelCreators;

    private transient Map<ModelInfo, ModelInfo> infoCache;
    private transient Map<GLModel, GLModel> modelCache;

    public ModelFactory() {
        init();
    }

    private void init() {
        infoCache = new HashMap<ModelInfo, ModelInfo>();
        modelCache = new HashMap<GLModel, GLModel>();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    public Map<ModelType, GLModelCreator> getModelCreators() {
        return modelCreators;
    }

    @Required
    public void setModelCreators(Map<ModelType, GLModelCreator> modelCreators) {
        this.modelCreators = modelCreators;
    }

    public ModelInfo getModel(ModelProperties modelProperties, TileId tileId, String modelName) {
        ModelType type = modelProperties.getType();
        if (modelCreators.containsKey(type)) {
            GLModelCreator creator = modelCreators.get(type);
            GLModel model = creator.createModel(modelProperties, tileId, modelName);
            if (model == null) {
                return null;
            }
            ModelInfo info = creator.createModelInfo(tileId, getCachedModel(model), modelName);
            if (info == null) {
                return null;
            }
            return getCachedInfo(info);
        } else {
            logger.warn("No model creator for type: " + type);
            return null;
        }
    }

    public ModelInfo getSimpleModel(TileId tileId) {
        return getModel(new ModelProperties("", ModelType.SIMPLE), tileId, "");
    }

    private ModelInfo getCachedInfo(ModelInfo info) {
        if (infoCache.containsKey(info)) {
            return infoCache.get(info);
        } else {
            infoCache.put(info, info);
            return info;
        }
    }

    public ObjectModel createSimpleModel(TileId tileId) {
        SimpleModel model = new SimpleModel(tileId);
        return getCachedModel(model);
    }

    private GLModel getCachedModel(GLModel model) {
        if (modelCache.containsKey(model)) {
            return modelCache.get(model);
        } else {
            modelCache.put(model, model);
            return model;
        }
    }

}
