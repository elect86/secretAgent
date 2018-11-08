package cz.wa.wautils.math;

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

/**
 * Immutable 2D float vector with public fields (used for opeGL).
 *
 * @author Ondrej Milenovsky
 */
public class Vector3F implements Serializable {
    private static final long serialVersionUID = -8194155739958674168L;

    private final float x;
    private final float y;
    private final float z;

    public Vector3F(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3F(Vector3D v) {
        x = (float) v.getX();
        y = (float) v.getY();
        z = (float) v.getZ();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3F add(Vector3F p) {
        return new Vector3F(x + p.getX(), y + p.getY(), z + p.getZ());
    }

    public Vector3F substract(Vector3F p) {
        return new Vector3F(x - p.getX(), y - p.getY(), z - p.getZ());
    }

    public Vector3F multiply(double a) {
        return new Vector3F((float) (x * a), (float) (y * a), (float) (z * a));
    }

    public double getDistance(Vector3F p) {
        return substract(p).getNorm();
    }

    public double getNorm() {
        return FastMath.sqrt(x * x + y * y + z * z);
    }

    public Vector3D toVector3D() {
        return new Vector3D(x, y, z);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
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
        Vector3F other = (Vector3F) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
