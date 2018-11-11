//package cz.wa.secretagent.io.campaign.model;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
//import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
//import secretAgent.view.model.AnimatedModel;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.GLModel;
//
///**
// * Creates animated model.
// *
// * @author Ondrej Milenovsky
// */
//public class AnimatedModelCreator extends AbstractModelCreator {
//    private static final long serialVersionUID = -5648071321917849247L;
//
//    private static final Logger logger = LoggerFactory.getLogger(AnimatedModelCreator.class);
//
//    private static final long DEFAULT_DURATION_MS = 1000;
//
//    private static final String FRAMES_PROPERTY = "frames";
//    private static final String DURATION_PROPERTY = "duration";
//
//    @Override
//    public AnimatedModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
//        // frames
//        List<TileId> tileIds = modelProperties.getProperties().get(FRAMES_PROPERTY);
//        if (tileIds == null) {
//            logger.warn("Animated model '" + modelName + "' must have property: " + FRAMES_PROPERTY);
//            return null;
//        }
//        // duration
//        long durationMs = DEFAULT_DURATION_MS;
//        TileId durTile = getSingleTileId(modelProperties.getProperties(), DURATION_PROPERTY, modelName);
//        if (durTile != null) {
//            durationMs = durTile.getTileId();
//        }
//        // scale
//        double scale = 0;
//        TileId scaleTile = getSingleTileId(modelProperties.getProperties(), SCALE_PROPERTY, modelName);
//        if (scaleTile != null) {
//            scale = tileToDouble(scaleTile);
//        }
//        return new AnimatedModel(tileIds, scale, durationMs);
//    }
//
//    @Override
//    public ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName) {
//        List<TileId> tileIds = ((AnimatedModel) model).getTileIds();
//        int offset = tileIds.indexOf(tileId);
//        if (offset < 0) {
//            offset = 0;
//        }
//        return new ModelInfo(offset, model);
//    }
//
//}
