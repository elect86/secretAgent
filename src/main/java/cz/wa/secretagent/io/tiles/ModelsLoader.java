package cz.wa.secretagent.io.tiles;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.campaign.model.ModelFactory;
import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;

/**
 * Loads models from file. 
 * 
 * @author Ondrej Milenovsky
 */
public class ModelsLoader {
    private static final Logger logger = LoggerFactory.getLogger(ModelsLoader.class);

    private final File file;
    private final ModelFactory modelFactory;

    public ModelsLoader(File file, ModelFactory modelFactory) {
        this.file = file;
        this.modelFactory = modelFactory;
    }

    public Map<String, ObjectModel> loadModels() throws IOException {
        TilesProperties tilesPr = new TilesPropertiesParser(file, 0).parse(true);
        return createModels(tilesPr);
    }

    private Map<String, ObjectModel> createModels(TilesProperties tilesPr) {
        Map<String, ObjectModel> ret = new HashMap<String, ObjectModel>(tilesPr.getModels().size());
        for (Map.Entry<String, ModelProperties> entry : tilesPr.getModels().entrySet()) {
            String name = entry.getKey();
            ModelInfo modelInfo = modelFactory.getModel(entry.getValue(), new TileId(0, 0), name);
            if (modelInfo != null) {
                ret.put(name, modelInfo.getModel());
            } else {
                logger.warn("Failed to create model '" + name + "': " + file.getAbsolutePath());
            }
        }
        return ret;
    }
}
