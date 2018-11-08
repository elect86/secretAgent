package cz.wa.wautils.math;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

/**
 * Immutable angle in 3D, better representation than 3D rotation.
 *
 * @author Ondrej Milenovsky
 */
public class Angle3D extends Vector3D {
    private static final long serialVersionUID = 6001788111594342113L;

    public Angle3D() {
        super(0, 0, 0);
    }

    public Angle3D(double a1, double a2, double a3) {
        super(normalize(a1), normalize(a2), normalize(a3));
    }

    /**
     * Creates vector of length 1 pointing at the direction.
     * angle.x - yaw
     * angle.y - pitch
     * angle.z - roll (ignored)
     * @param angle
     */
    public Vector3D getDirectionYPR() {
        double cosY = FastMath.cos(getY());
        double x = FastMath.cos(getX()) * cosY;
        double y = FastMath.sin(getX()) * cosY;
        double z = FastMath.sin(getY());
        return new Vector3D(x, y, z);
    }

    /**
     * Creates rotation matrix
     * angle.x - yaw
     * angle.y - pitch
     * angle.z - roll (ignored)
     * @return
     */
    public Rotation getRotationYPR() {
        return new Rotation(RotationOrder.ZYX, getX(), getY(), getZ());
    }

    /**
     * Makes the angle in bounds: <-PI..PI)
     * @param angle
     * @return
     */
    public static double normalize(double angle) {
        while (angle > FastMath.PI) {
            angle -= FastMath.PI * 2;
        }
        while (angle <= -FastMath.PI) {
            angle += FastMath.PI * 2;
        }
        return angle;
    }

    public Angle3D add(Angle3D angle) {
        return new Angle3D(getX() + angle.getX(), getY() + angle.getY(), getZ() + angle.getZ());
    }

    public Angle3D substract(Angle3D angle) {
        return new Angle3D(getX() - angle.getX(), getY() - angle.getY(), getZ() - angle.getZ());
    }
}
