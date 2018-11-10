package cz.wa.secretagent.io.tiles.singleproperties;

import secretAgent.view.renderer.TileId;
import secretAgent.world.ModelType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parsed properties for model. 
 * 
 * @author Ondrej Milenovsky
 */
public class ModelProperties {
    private final String name;
    private final ModelType type;
    /** property name -> list of tile ids */
    private final Map<String, List<TileId>> properties;

    public ModelProperties(String name, ModelType type) {
        this.name = name;
        this.type = type;
        properties = new HashMap<String, List<TileId>>();
    }

    public String getName() {
        return name;
    }

    public ModelType getType() {
        return type;
    }

    public Map<String, List<TileId>> getProperties() {
        return properties;
    }
}
