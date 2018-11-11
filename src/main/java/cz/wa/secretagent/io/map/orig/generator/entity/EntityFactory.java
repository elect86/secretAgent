//package cz.wa.secretagent.io.map.orig.generator.entity;
//
//import java.util.LinkedList;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.EntityType;
//import cz.wa.secretagent.worldinfo.graphics.EntityInfo;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.ObjectModel;
//
///**
// * Creates entities from parsed info. Has map of creators for each entity type.
// *
// * @author Ondrej Milenovsky
// */
//public class EntityFactory extends TypeEntityCreator<Entity> {
//    private static final long serialVersionUID = 867646705640136421L;
//
//    private static final Logger logger = LoggerFactory.getLogger(EntityFactory.class);
//
//    public Entity createEntity(EntityInfo entityInfo, Vector2D pos, TileId tileId, ObjectModel model) {
//        LinkedList<String> args = new LinkedList<String>(entityInfo.getArgs());
//        Entity entity = createEntity(args, pos, tileId, model);
//        if (!args.isEmpty()) {
//            logger.warn("Entity definition " + tileId + " has too many arguments, ignoring: " + args);
//        }
//        return entity;
//    }
//
//    @Override
//    protected Object getEnum(String arg0) {
//        return EntityType.valueOf(arg0);
//    }
//
//}
