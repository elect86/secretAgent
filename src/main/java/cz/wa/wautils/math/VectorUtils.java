package cz.wa.wautils.math;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

/**
 * Utils for vectors.
 * 
 * @author Ondrej Milenovsky
 */
public final class VectorUtils {
    private VectorUtils() {
    }

    public static double getAngle(Vector2D center, Vector2D pos) {
        return FastMath.atan2(pos.getY() - center.getY(), pos.getX() - center.getX());
    }

    public static double getAngle(Vector2D v) {
        return FastMath.atan2(v.getY(), v.getX());
    }

    /**
     * @param center
     * @param pos
     * @return angle from center to pos
     */
    public static double getGroundAngle(Vector3D center, Vector3D pos) {
        return FastMath.atan2(pos.getY() - center.getY(), pos.getX() - center.getX());
    }

    public static double getGroundAngle(Vector3D v) {
        return FastMath.atan2(v.getY(), v.getX());
    }

    /**
     * @param center
     * @param pos
     * @return distance ignoring third dimension
     */
    public static double getGroundDist(Vector3D center, Vector3D pos) {
        return FastMath.sqrt(pow2(pos.getY() - center.getY()) + pow2(pos.getX() - center.getX()));
    }

    private static double pow2(double d) {
        return d * d;
    }

    public static Vector3D getCenter(Vector3D p1, Vector3D p2) {
        return new Vector3D((p1.getX() + p2.getX()) / 2.0, (p1.getY() + p2.getY()) / 2.0,
                (p1.getZ() + p2.getZ()) / 2.0);
    }

    /**
     * @param point the point
     * @param lineP1 line point 1
     * @param lineP2 line point 2
     * @return distance point from line
     */
    public static double getDist(Vector2D point, Vector2D lineP1, Vector2D lineP2) {
        double x0 = point.getX();
        double y0 = point.getY();
        double x1 = lineP1.getX();
        double y1 = lineP1.getY();
        double x2 = lineP2.getX();
        double y2 = lineP2.getY();
        double up = (x2 - x1) * (y1 - y0) - (x1 - x0) * (y2 - y1);
        double down = sqr(x2 - x1) + sqr(y2 - y1);
        return FastMath.abs(up) / FastMath.sqrt(down);
    }

    /**
     * Creates 2D vector from x, y, throws away z
     * @param p
     * @return
     */
    public static Vector2D getVector2D(Vector3D p) {
        return new Vector2D(p.getX(), p.getY());
    }

    public static Vector3D getVector3D(Vector2D p, double z) {
        return new Vector3D(p.getX(), p.getY(), z);
    }

    public static Vector3D getVector3D(Vector2D p) {
        return new Vector3D(p.getX(), p.getY(), 0);
    }

    public static boolean isInsideRect2D(Vector3D rectCenter, Vector2D rectSize, double rectAngle, Vector3D p) {
        p = p.subtract(rectCenter);
        p = new Rotation(new Vector3D(0, 0, 1), -rectAngle).applyTo(p);

        double sx = rectSize.getX() / 2.0;
        double sy = rectSize.getY() / 2.0;

        return (p.getX() < sx) && (p.getY() < sy) && (p.getX() > -sx) && (p.getY() > -sy);
    }

    /**
     * Finds single intersection point of two lines, the point is always on the lines (is between p1 and p2).
     * http://en.wikipedia.org/wiki/Line-line_intersection
     * @param line1p1 line 1 point 1
     * @param line1p2 line 1 point 2
     * @param line2p1 line 2 point 1
     * @param line2p2 line 2 point 2
     * @return intersection point or null if not on both lines or parallel lines
     */
    public static Vector2D intersection(Vector2D line1p1, Vector2D line1p2, Vector2D line2p1, Vector2D line2p2) {
        double x1 = line1p1.getX();
        double y1 = line1p1.getY();
        double x2 = line1p2.getX();
        double y2 = line1p2.getY();

        double x3 = line2p1.getX();
        double y3 = line2p1.getY();
        double x4 = line2p2.getX();
        double y4 = line2p2.getY();

        // check parallel
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        // compute point
        double a = x1 * y2 - y1 * x2;
        double b = x3 * y4 - y3 * x4;
        double x = a * (x3 - x4) - (x1 - x2) * b;
        double y = a * (y3 - y4) - (y1 - y2) * b;
        x /= d;
        y /= d;
        Vector2D p = new Vector2D(x, y);

        // check in lines
        if (!isInside(p, line1p1, line1p2)) {
            return null;
        }
        if (!isInside(p, line2p1, line2p2)) {
            return null;
        }
        return p;
    }

    /**
     * Checks if the point is inside rectangle defined by two points.
     * @param p the point
     * @param c1 corner 1
     * @param c2 corner 2
     * @return true if is inside
     */
    public static boolean isInside(Vector2D p, Vector2D c1, Vector2D c2) {
        double x1 = FastMath.min(c1.getX(), c2.getX());
        double y1 = FastMath.min(c1.getY(), c2.getY());
        double x2 = FastMath.max(c1.getX(), c2.getX());
        double y2 = FastMath.max(c1.getY(), c2.getY());
        return (p.getX() >= x1) && (p.getY() >= y1) && (p.getX() <= x2) && (p.getY() <= y2);
    }

    private static double sqr(double d) {
        return d * d;
    }
}
