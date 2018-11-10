//package cz.wa.secretagent.view.texture;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.wautils.math.Rectangle2D;
//
///**
// * Bounds on canvas of an object that is about to be drawn.
// *
// * @author Ondrej Milenovsky
// */
//public class DrawBounds {
//    private final double x1;
//    private final double y1;
//    private final double x2;
//    private final double y2;
//
//    public DrawBounds(double x1, double y1, double x2, double y2) {
//        this.x1 = x1;
//        this.y1 = y1;
//        this.x2 = x2;
//        this.y2 = y2;
//    }
//
//    public DrawBounds(Rectangle2D rect) {
//        this(rect.getX(), rect.getY(), rect.getX2(), rect.getY2());
//    }
//
//    public DrawBounds(Rectangle2D rect, boolean flipX) {
//        this((flipX ? rect.getX2() : rect.getX()), rect.getY(), (flipX ? rect.getX() : rect.getX2()), rect
//                .getY2());
//    }
//
//    public DrawBounds(Vector2D v1, Vector2D v2) {
//        this(v1.getX(), v1.getY(), v2.getX(), v2.getY());
//    }
//
//    public double getX1() {
//        return x1;
//    }
//
//    public double getY1() {
//        return y1;
//    }
//
//    public double getX2() {
//        return x2;
//    }
//
//    public double getY2() {
//        return y2;
//    }
//}
