//package cz.wa.secretagent.io.tiles;
//
//import java.util.Map;
//
//import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
//import cz.wa.secretagent.io.tiles.singleproperties.TileProperties;
//import cz.wa.wautils.math.Vector2I;
//import secretAgent.view.renderer.TileId;
//
///**
// * Parsed tiles info.
// *
// * @author Ondrej Milenovsky
// */
//public class TilesProperties {
//    private final Vector2I tileSize;
//    private final Map<TileId, TileProperties> tiles;
//    private final Map<String, ModelProperties> models;
//
//    public TilesProperties(Vector2I tileSize, Map<TileId, TileProperties> tiles,
//            Map<String, ModelProperties> models) {
//        this.tileSize = tileSize;
//        this.tiles = tiles;
//        this.models = models;
//    }
//
//    public Vector2I getTileSize() {
//        return tileSize;
//    }
//
//    public Map<TileId, TileProperties> getTiles() {
//        return tiles;
//    }
//
//    public Map<String, ModelProperties> getModels() {
//        return models;
//    }
//}
