package cz.wa.wautils.math;

import java.awt.Rectangle;
import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

/**
 * Immutable 2D double rectangle.
 *
 * @author Ondrej Milenovsky
 */
public class Rectangle2D implements Serializable {
    private static final long serialVersionUID = 8162943873842261150L;

    /** Rectangle with all values 0 */
    public static final Rectangle2D ZERO = new Rectangle2D(0, 0);

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    /**
     * Parses bounds from the input text.
     * @param text
     * @throws IllegalArgumentException
     */
    public Rectangle2D(String text) {
        String[] split = text.split("[,\\s]+");
        if (split.length == 2) {
            x = 0;
            y = 0;
            width = Double.parseDouble(split[0]);
            height = Double.parseDouble(split[1]);
        } else if (split.length == 4) {
            x = Double.parseDouble(split[0]);
            y = Double.parseDouble(split[1]);
            width = Double.parseDouble(split[2]);
            height = Double.parseDouble(split[3]);
        } else {
            throw new IllegalArgumentException(
                    "Input string must contain 2 or 4 doubles separated by ',' or white spaces: " + text);
        }
    }

    public Rectangle2D(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    public Rectangle2D(double width, double height) {
        this(0, 0, width, height);
    }

    public Rectangle2D(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = FastMath.abs(width);
        this.height = FastMath.abs(height);
    }

    public Rectangle2D(Vector2I p1, Vector2I p2) {
        x = FastMath.min(p1.getX(), p2.getX());
        y = FastMath.min(p1.getY(), p2.getY());
        width = FastMath.abs(p1.getX() - p2.getX());
        height = FastMath.abs(p1.getY() - p2.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getX2() {
        return x + width;
    }

    public double getY2() {
        return y + height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isInside(Vector2D p) {
        return (p.getX() >= x) && (p.getX() < x + width) && (p.getY() >= y) && (p.getY() < y + height);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(height);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Rectangle2D other = (Rectangle2D) obj;
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }

    /**
     * @param r2 other rectangle
     * @return true if the rectangles intersect, false if they touch or are far apart
     */
    public boolean intersects(Rectangle2D r2) {
        return (getX() < r2.getX2()) && (getX2() > r2.getX()) && (getY() < r2.getY2())
                && (getY2() > r2.getY());
    }

    public boolean intersectsOrTouches(Rectangle2D r2) {
        return (getX() <= r2.getX2()) && (getX2() >= r2.getX()) && (getY() <= r2.getY2())
                && (getY2() >= r2.getY());
    }

    /**
     * Adds the vector to rectangle position
     * @param p coords to add
     * @return new moved rectangle
     */
    public Rectangle2D move(Vector2D p) {
        return new Rectangle2D(x + p.getX(), y + p.getY(), width, height);
    }

    /**
     * Computes min intersecting distance with other rectangle. The method does not chech if they intersect or not.
     * @param r2 other rectangle
     * @return distance or something useless if they don't intersect
     */
    public double getIntersectingDist(Rectangle2D r2) {
        double min = Double.MAX_VALUE;
        double d;
        d = r2.getX2() - getX();
        if (d > 0) {
            min = FastMath.min(min, d);
        }
        d = r2.getY2() - getY();
        if (d > 0) {
            min = FastMath.min(min, d);
        }
        d = getX2() - r2.getX();
        if (d > 0) {
            min = FastMath.min(min, d);
        }
        d = getY2() - r2.getY();
        if (d > 0) {
            min = FastMath.min(min, d);
        }
        if (d == Double.MAX_VALUE) {
            return Double.NaN;
        } else {
            return min;
        }
    }

    /**
     * Expands the rectangle to left and right by v.x, up and down by v.y,
     * the final width is +2 * v.x, the final height is +2 * v.y.
     * @param v expand vector
     * @return expanded rectangle
     */
    public Rectangle2D expand(Vector2D v) {
        return new Rectangle2D(x - v.getX(), y - v.getY(), width + v.getX() * 2, height + v.getY() * 2);
    }

    /**
     * Multiplies all values by the scale
     * @param scale scale to multiply
     * @return new rectangle
     */
    public Rectangle2D multiply(double scale) {
        return new Rectangle2D(x * scale, y * scale, width * scale, height * scale);
    }

    /**
     * Multiplies every coord by the scalar.
     * @param a
     * @return
     */
    public Rectangle2D scalarMultiply(int a) {
        return new Rectangle2D(x * a, y * a, width * a, height * a);
    }
}
