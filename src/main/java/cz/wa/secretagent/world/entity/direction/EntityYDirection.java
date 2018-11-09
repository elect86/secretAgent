//package cz.wa.secretagent.world.entity.direction;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
///**
// * Direction y of an entity.
// *
// * @author Ondrej Milenovsky
// */
//public enum EntityYDirection implements Direction {
//    NONE(Vector2D.ZERO),
//    UP(new Vector2D(0, -1)),
//    DOWN(new Vector2D(0, 1));
//
//    private final Vector2D dir;
//
//    private EntityYDirection(Vector2D dir) {
//        this.dir = dir;
//    }
//
//    @Override
//    public Vector2D getVector() {
//        return dir;
//    }
//
//    public EntityDirection getDirection() {
//        if (this == UP) {
//            return EntityDirection.UP;
//        } else if (this == DOWN) {
//            return EntityDirection.DOWN;
//        } else {
//            return EntityDirection.NONE;
//        }
//    }
//}
