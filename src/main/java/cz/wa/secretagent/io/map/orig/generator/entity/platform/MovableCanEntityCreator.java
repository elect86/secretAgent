//package cz.wa.secretagent.io.map.orig.generator.entity.platform;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
//import cz.wa.secretagent.world.entity.platform.MovableCan;
//import cz.wa.wautils.math.Rectangle2D;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.ObjectModel;
//
///**
// * Creates movable can.
// *
// * @author Ondrej Milenovsky
// */
//public class MovableCanEntityCreator implements EntityCreator<MovableCan> {
//
//    private static final long serialVersionUID = -6412487887560920954L;
//
//    private Rectangle2D bounds;
//
//    @Override
//    public MovableCan createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
//        MovableCan can = new MovableCan(model, pos, bounds);
//        return can;
//    }
//
//    public Rectangle2D getBounds() {
//        return bounds;
//    }
//
//    @Required
//    public void setBounds(Rectangle2D bounds) {
//        this.bounds = bounds;
//    }
//
//}
