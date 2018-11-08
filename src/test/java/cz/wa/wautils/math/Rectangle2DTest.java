package cz.wa.wautils.math;

import static junit.framework.Assert.*;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

/**
 * Tests the rectangle. 
 * 
 * @author Ondrej Milenovsky
 */
public class Rectangle2DTest {
    @Test
    public void testMove() {
        Rectangle2D r;
        r = new Rectangle2D(10.5, -20.6);
        r = r.move(Vector2D.ZERO);
        assertEquals(new Rectangle2D(10.5, -20.6), r);

        r = new Rectangle2D(10.5, -20.6);
        r = r.move(new Vector2D(6, 8));
        assertEquals(new Rectangle2D(6, 8, 10.5, -20.6), r);
    }

    @Test
    public void testIntersectingDist() {
        Rectangle2D r1, r2;

        r1 = new Rectangle2D(10, 5);
        r2 = new Rectangle2D(9, 0, 10, 10);
        assertEquals(r1.getIntersectingDist(r2), 1.0);

        r1 = new Rectangle2D(10, 5);
        r2 = new Rectangle2D(-2, 3, 10, 10);
        assertEquals(r1.getIntersectingDist(r2), 2.0);
    }
}
