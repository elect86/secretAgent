package cz.wa.wautils.math;

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

/**
 * Immutable 2D integer vector.
 *
 * @author Ondrej Milenovsky
 */
public class Vector2I implements Serializable {
    private static final long serialVersionUID = -8843472685752299281L;

    public static final Vector2I ZERO = new Vector2I(0, 0);

    private final int x;
    private final int y;

    public Vector2I(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2I(Vector2D v) {
        x = (int) FastMath.round(v.getX());
        y = (int) FastMath.round(v.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2I add(Vector2I p) {
        return new Vector2I(x + p.getX(), y + p.getY());
    }

    public Vector2I substract(Vector2I p) {
        return new Vector2I(x - p.getX(), y - p.getY());
    }

    public Vector2I multiply(double a) {
        return new Vector2I((int) FastMath.round(x * a), (int) FastMath.round(y * a));
    }

    public double getDistance(Vector2I p) {
        return substract(p).getNorm();
    }

    public double getNorm() {
        return FastMath.sqrt(x * x + y * y);
    }

    public Vector2D toVector2D() {
        return new Vector2D(x, y);
    }

    public double getAngle(Vector2I second) {
        return FastMath.atan2(second.getY() - y, second.getX() - x);
    }

    /**
     * Flips x and y
     * @return
     */
    public Vector2I flip() {
        return new Vector2I(y, x);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Vector2I other = (Vector2I) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public static Vector2I createRounded(double x, double y) {
        return new Vector2I((int) FastMath.round(x), (int) FastMath.round(y));
    }
}
