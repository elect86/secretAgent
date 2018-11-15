//package cz.wa.secretagent.world.entity.platform;
//
//import cz.wa.wautils.math.Rectangle2D;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import secretAgent.world.ObjectModel;
//import secretAgent.world.entity.Entity;
//import secretAgent.world.entity.EntityType;
//
///**
// * Moving platform (elevator).
// *
// * @author Ondrej Milenovsky
// */
//public abstract class PlatformEntity extends Entity {
//
//    public PlatformEntity(ObjectModel model, Vector2D pos, Rectangle2D bounds) {
//        super(model, pos, bounds, Vector2D.ZERO, false);
//    }
//
//    @Override
//    public EntityType getType() {
//        return EntityType.PLATFORM;
//    }
//
//    @Override
//    public abstract PlatformType getSecondType();
//
//}
