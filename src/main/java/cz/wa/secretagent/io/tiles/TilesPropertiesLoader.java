//package cz.wa.secretagent.io.tiles;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.secretagent.io.tiles.singleproperties.EntityProperties;
//import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
//import cz.wa.secretagent.io.tiles.singleproperties.TileProperties;
//import cz.wa.secretagent.worldinfo.graphics.EntityInfo;
//import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
//import cz.wa.secretagent.worldinfo.graphics.TileInfo;
//import cz.wa.secretagent.worldinfo.graphics.TilesInfo;
//import secretAgent.io.campaign.model.ModelFactory;
//import secretAgent.view.renderer.TileId;
//
///**
// * Loads tiles properties and generates TilesInfo
// *
// * @author Ondrej Milenovsky
// */
//public class TilesPropertiesLoader {
//    private static final Logger logger = LoggerFactory.getLogger(TilesPropertiesLoader.class);
//
//    private final File file;
//    private final int tileSetId;
//    private final ModelFactory modelFactory;
//
//    public TilesPropertiesLoader(File file, int tileSetId, ModelFactory modelFactory) {
//        this.file = file;
//        this.tileSetId = tileSetId;
//        this.modelFactory = modelFactory;
//    }
//
//    public TilesInfo loadInfo() throws IOException {
//        logger.info("Loading tiles definition: " + file.getAbsolutePath());
//
//        TilesProperties tilesPr = new TilesPropertiesParser(file, tileSetId).parse(false);
//        return createTilesInfo(tilesPr);
//    }
//
//    /**
//     * Creates single tile set info from parsed tile properties.
//     */
//    private TilesInfo createTilesInfo(TilesProperties tilesPr) {
//        Map<Integer, TileInfo> tiles = new HashMap<Integer, TileInfo>(tilesPr.getTiles().size());
//        for (Map.Entry<TileId, TileProperties> entry : tilesPr.getTiles().entrySet()) {
//            TileId tileId = entry.getKey();
//            TileInfo tileInfo = createTileInfo(tileId, entry.getValue(), tilesPr.getModels());
//            tiles.put(tileId.getTileId(), tileInfo);
//        }
//        return new TilesInfo(tilesPr.getTileSize(), tiles);
//    }
//
//    /**
//     * Creates tile info for single tile.
//     */
//    private TileInfo createTileInfo(TileId tileId, TileProperties tilePr, Map<String, ModelProperties> models) {
//        ModelInfo model = null;
//        String modelRef = tilePr.getModelRef();
//        boolean hasModelRef = !StringUtils.isEmpty(modelRef);
//        if (hasModelRef) {
//            if (models.containsKey(modelRef)) {
//                model = modelFactory.getModel(models.get(modelRef), tileId, modelRef);
//            } else {
//                logger.warn("No model definition: '" + modelRef + "' for tile: " + tileId);
//            }
//        }
//        if (model == null) {
//            if (hasModelRef) {
//                logger.warn("Failed to create model '" + modelRef + "' for tile " + tileId
//                        + ", creating simple model");
//            }
//            model = modelFactory.getSimpleModel(tileId);
//        }
//        return new TileInfo(tilePr.getTileType(), createEntityInfo(tilePr.getEntityProperties()), model);
//    }
//
//    private EntityInfo createEntityInfo(EntityProperties entityProperties) {
//        if (entityProperties == null) {
//            return null;
//        } else {
//            return new EntityInfo(entityProperties);
//        }
//    }
//
//}
