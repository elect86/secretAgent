//package cz.wa.secretagent.io.campaign.model;
//
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
//import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
//import secretAgent.view.model.HealthBarModel;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.GLModel;
//
///**
// * Creates simple model.
// *
// * @author Ondrej Milenovsky
// */
//public class HealthBarModelCreator extends AbstractModelCreator {
//    private static final long serialVersionUID = 6809068456150092998L;
//
//    private static final Logger logger = LoggerFactory.getLogger(HealthBarModelCreator.class);
//
//    private static final String FRAME_PROPERTY = "frame";
//    private static final String HEALTH_PROPERTY = "health";
//
//    @Override
//    public HealthBarModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
//        Map<String, List<TileId>> properties = modelProperties.getProperties();
//        TileId frame = getTileId(properties, modelName, FRAME_PROPERTY);
//        TileId health = getTileId(properties, modelName, HEALTH_PROPERTY);
//        if ((frame != null) && (health != null)) {
//            return new HealthBarModel(frame, health, 1);
//        } else {
//            return null;
//        }
//    }
//
//    private TileId getTileId(Map<String, List<TileId>> properties, String modelName, String propertyName) {
//        if (properties.containsKey(propertyName)) {
//            List<TileId> list = properties.get(propertyName);
//            if (list.size() != 1) {
//                logger.warn("Health bar model '" + modelName + "' defines property " + FRAME_PROPERTY
//                        + ", required number of tiles is 1, actual: " + list.size());
//            }
//            return list.get(0);
//        } else {
//            logger.warn("Health bar model '" + modelName + "' requires property '" + FRAME_PROPERTY + "'");
//            return null;
//        }
//    }
//
//    @Override
//    public ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName) {
//        return new ModelInfo(model);
//    }
//
//}
