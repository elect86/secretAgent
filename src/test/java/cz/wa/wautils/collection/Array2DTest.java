package cz.wa.wautils.collection;

import static junit.framework.Assert.*;

import org.junit.Test;

import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;

public class Array2DTest {

    @Test
    public void testElements() {
        Array2D<String> a = new Array2DImpl<String>(new Rectangle2I(3, 2));

        a.set(new Vector2I(0, 0), "a");
        a.set(new Vector2I(0, 1), "b");
        a.set(new Vector2I(2, 1), "c");

        assertEquals("a", a.get(new Vector2I(0, 0)));
        assertEquals("b", a.get(new Vector2I(0, 1)));
        assertEquals("c", a.get(new Vector2I(2, 1)));

        a.set(new Vector2I(0, 1), "d");
        assertEquals("d", a.get(new Vector2I(0, 1)));
    }

    @Test
    public void testIndices() {
        Array2D<String> a = new Array2DImpl<String>(new Rectangle2I(3, 2));
        String[] expected = new String[] { "[0, 0]", "[1, 0]", "[2, 0]", "[0, 1]", "[1, 1]", "[2, 1]" };
        int i = 0;
        for (Vector2I ind : a.getIndices()) {
            assertEquals(expected[i], ind.toString());
            i++;
        }
        assertEquals(expected.length, i);

        a = new Array2DImpl<String>(new Rectangle2I(3, 3));
        expected = new String[] { "[0, 0]", "[1, 0]", "[2, 0]", "[0, 1]", "[1, 1]", "[2, 1]", "[0, 2]",
                "[1, 2]", "[2, 2]" };
        i = 0;
        for (Vector2I ind : a.getIndices()) {
            assertEquals(expected[i], ind.toString());
            i++;
        }
        assertEquals(expected.length, i);

    }

}
