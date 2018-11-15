//package cz.wa.secretagent.world.entity;
//
//import java.util.Comparator;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
///**
// * Sorts by lines from up to down and left to right on each line.
// *
// * @author Ondrej Milenovsky
// */
//public class EntityComparator implements Comparator<Entity> {
//
//    public static final EntityComparator INSTANCE = new EntityComparator();
//
//    protected EntityComparator() {
//    }
//
//    @Override
//    public int compare(Entity o1, Entity o2) {
//        Vector2D p1 = o1.getPos();
//        Vector2D p2 = o2.getPos();
//        if (p1.getY() < p2.getY()) {
//            return -1;
//        } else if (p1.getY() > p2.getY()) {
//            return 1;
//        } else {
//            return Double.compare(p1.getX(), p2.getX());
//        }
//    }
//
//}
