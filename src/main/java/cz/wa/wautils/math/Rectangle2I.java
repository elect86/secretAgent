package cz.wa.wautils.math;

import java.awt.Rectangle;
import java.io.Serializable;

import org.apache.commons.math3.util.FastMath;

/**
 * Immutable 2D integer rectangle.
 *
 * @author Ondrej Milenovsky
 */
public class Rectangle2I implements Serializable {
    private static final long serialVersionUID = -2611972020154670757L;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Rectangle2I(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    public Rectangle2I(int width, int height) {
        this(0, 0, width, height);
    }

    public Rectangle2I(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = FastMath.abs(width);
        this.height = FastMath.abs(height);
    }

    public Rectangle2I(Vector2I p1, Vector2I p2) {
        x = FastMath.min(p1.getX(), p2.getX());
        y = FastMath.min(p1.getY(), p2.getY());
        width = FastMath.abs(p1.getX() - p2.getX());
        height = FastMath.abs(p1.getY() - p2.getY());
    }

    public Rectangle2I(Rectangle2D rect) {
        x = (int) FastMath.round(rect.getX());
        y = (int) FastMath.round(rect.getY());
        width = (int) FastMath.round(rect.getWidth());
        height = (int) FastMath.round(rect.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getX2() {
        return x + width;
    }

    public int getY2() {
        return y + height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInside(Vector2I p) {
        return (p.getX() >= x) && (p.getX() < x + width) && (p.getY() >= y) && (p.getY() < y + height);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + width;
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
        Rectangle2I other = (Rectangle2I) obj;
        if (height != other.height) {
            return false;
        }
        if (width != other.width) {
            return false;
        }
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
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }

}
