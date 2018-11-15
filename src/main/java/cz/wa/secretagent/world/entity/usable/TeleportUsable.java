//package cz.wa.secretagent.world.entity.usable;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import secretAgent.world.ObjectModel;
//
///**
// * Teleport pointing to some position.
// *
// * @author Ondrej Milenovsky
// */
//public class TeleportUsable extends UsableEntity {
//
//    private Vector2D destination;
//
//    public TeleportUsable(ObjectModel model, Vector2D pos) {
//        super(model, pos, false);
//    }
//
//    @Override
//    public UsableType getSecondType() {
//        return UsableType.TELEPORT;
//    }
//
//    public Vector2D getDestination() {
//        return destination;
//    }
//
//    public void setDestination(Vector2D destination) {
//        this.destination = destination;
//    }
//
//}
