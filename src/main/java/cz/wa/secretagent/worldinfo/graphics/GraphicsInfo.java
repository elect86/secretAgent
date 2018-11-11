package cz.wa.secretagent.worldinfo.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import secretAgent.view.SAMGraphics;
import secretAgent.view.renderer.TileId;
import secretAgent.world.ObjectModel;

/**
 * Information about tile sets. 
 * 
 * @author Ondrej Milenovsky
 */
public class GraphicsInfo {
    public static final int ORIG_LEVEL_TILES_ID = 0;
    public static final int ORIG_GUI_SPRITES_ID = -1;

    private Color bgColor = Color.BLACK;
    /** info used to create map tiles, tile set id -> info */
    private final Map<Integer, TilesInfo> tileSets;
    /** models that are not created when loading map, model name -> model */
    private final Map<String, ObjectModel> models;

    public GraphicsInfo() {
        tileSets = new HashMap<Integer, TilesInfo>();
        models = new HashMap<String, ObjectModel>();
    }

    public GraphicsInfo(Map<Integer, TilesInfo> tileSets, Map<String, ObjectModel> models) {
        this.tileSets = tileSets;
        this.models = models;
    }

    public Map<Integer, TilesInfo> getTileSets() {
        return tileSets;
    }

    public Map<String, ObjectModel> getModels() {
        return models;
    }

    public TilesInfo getTileSet(int tileSetId) {
        return tileSets.get(tileSetId);
    }

    public TileInfo getTileInfo(TileId tileId) {
        return tileSets.get(tileId.getTileSetId()).getTile(tileId.getTileId());
    }

    public boolean containsSet(int tileSetId) {
        return tileSets.containsKey(tileSetId);
    }

    public void linkTexturesToModels(SAMGraphics graphics) {
        for (Map.Entry<Integer, TilesInfo> entry1 : tileSets.entrySet()) {
            for (Map.Entry<Integer, TileInfo> entry2 : entry1.getValue().getTiles().entrySet()) {
                entry2.getValue().getModelInfo().getModel().linkTextures(graphics);
            }
        }
        for (ObjectModel model : models.values()) {
            model.linkTextures(graphics);
        }
    }

    public void update(GraphicsInfo gr2) {
        bgColor = gr2.bgColor;
        tileSets.putAll(gr2.tileSets);
        models.putAll(gr2.models);
    }

    /**
     * @param name name of the model
     * @return stored model by the name
     */
    public ObjectModel getModel(String name) {
        return models.get(name);
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }
}
