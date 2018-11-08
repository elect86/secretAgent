package cz.wa.wautils.math;

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

/**
 * Immutable 2D float vector with public fields (used for opeGL).
 *
 * @author Ondrej Milenovsky
 */
public class Vector2F implements Serializable {
    private static final long serialVersionUID = -7793609390172882619L;

    public static final Vector2F ZERO = new Vector2F(0, 0);

    public final float x;
    public final float y;

    public Vector2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2F(Vector2D v) {
        x = (float) v.getX();
        y = (float) v.getY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2F add(Vector2F p) {
        return new Vector2F(x + p.getX(), y + p.getY());
    }

    public Vector2F substract(Vector2F p) {
        return new Vector2F(x - p.getX(), y - p.getY());
    }

    public Vector2F multiply(double a) {
        return new Vector2F((float) (x * a), (float) (y * a));
    }

    public double getDistance(Vector2F p) {
        return substract(p).getNorm();
    }

    public double getNorm() {
        return FastMath.sqrt(x * x + y * y);
    }

    public Vector2F toVector2D() {
        return new Vector2F(x, y);
    }

    public double getAngle(Vector2F second) {
        return FastMath.atan2(second.getY() - y, second.getX() - x);
    }

    /**
     * Flips x and y
     * @return
     */
    public Vector2F flip() {
        return new Vector2F(y, x);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
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
        Vector2F other = (Vector2F) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
