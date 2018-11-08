package cz.wa.wautils.math;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Utils for matrices.
 * 
 * @author Ondrej Milenovsky
 */
public final class MatrixUtils {
    private MatrixUtils() {
    }

    /**
     * Joins two matrices or vectors. Available classes: double[], double[][], RealMatrix
     * @param m1
     * @param m2
     * @return [m1, m2]
     */
    public static RealMatrix join(Object m1, Object m2) {
        double[][] d1 = toArray(m1);
        double[][] d2 = toArray(m2);
        if (d1.length != d2.length) {
            throw new IllegalArgumentException("Cannot join sizes " + d1.length + " and " + d2.length);
        }
        RealMatrix ret = new Array2DRowRealMatrix(d1.length, d1[0].length + d2[0].length);
        ret.setSubMatrix(d1, 0, 0);
        ret.setSubMatrix(d2, 0, d1.length);
        return ret;
    }

    /**
     * Converts matrix or vector to 2d array. Avaible classes: double[], double[][], RealMatrix
     * @param m
     * @return
     */
    public static double[][] toArray(Object m) {
        Validate.notNull(m);
        // 2d array, return
        if (m instanceof double[][]) {
            if (((double[][]) m).length == 0) {
                throw new IllegalArgumentException("Empty matrix");
            }
            return (double[][]) m;
        }
        // matrix, get array
        if (m instanceof RealMatrix) {
            return ((RealMatrix) m).getData();
        }
        // 1d array, vector to matrix array
        if (m instanceof double[]) {
            double[] d = (double[]) m;
            if (d.length == 0) {
                throw new IllegalArgumentException("Empty matrix");
            }
            double[][] ret = new double[d.length][1];
            for (int i = 0; i < d.length; i++) {
                ret[i][0] = d[i];
            }
            return ret;
        }
        // unknown
        throw new IllegalArgumentException("Unexpected class: " + m.getClass().getSimpleName());
    }

    /**
     * Multiplies each dimension of the vectors and returns the result.
     * @param v1
     * @param v2
     * @return
     */
    public static Vector3D multiply(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.getX() * v2.getX(), v1.getY() * v2.getY(), v1.getZ() * v2.getZ());
    }

    /**
     * Multiplies the vector by a constant.
     * @param v1
     * @param d
     * @return
     */
    public static Vector3D multiply(Vector3D v1, double d) {
        return new Vector3D(v1.getX() * d, v1.getY() * d, v1.getZ() * d);
    }
}
