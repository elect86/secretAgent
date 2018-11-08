package cz.wa.wautils.math;

/**
 * Methods for number validation.
 *
 * @author Ondrej Milenovsky
 */
public class NumValidate {
    private NumValidate() {
    }

    public static void isPositive(int n, String name) {
        if (n <= 0) {
            throw new IllegalArgumentException(name + " must be > 0, but is " + n);
        }
    }

    public static void isPositive(double n, String name) {
        if (n <= 0) {
            throw new IllegalArgumentException(name + " must be > 0.0, but is " + n);
        }
    }

    public static void isInRange(int n, int min, int max, String name) {
        if ((n < min) || (n >= max)) {
            throw new IllegalArgumentException(name + " must be <" + min + ".." + max + "), but is " + n);
        }
    }

    public static void isInRange(double n, int min, int max, String name) {
        if ((n < min) || (n >= max)) {
            throw new IllegalArgumentException(name + " must be <" + min + ".." + max + "), but is " + n);
        }
    }

}
