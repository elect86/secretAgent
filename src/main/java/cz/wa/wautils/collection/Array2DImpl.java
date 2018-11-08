package cz.wa.wautils.collection;

import java.util.Iterator;

import org.apache.commons.lang.Validate;

import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;

/**
 * Wrapper for 2D array.
 *
 * @author Ondrej Milenovsky
 */
public class Array2DImpl<E> implements Array2D<E> {

    private static final long serialVersionUID = -932499233941102422L;

    private Object[][] grid;
    private Rectangle2I bounds;

    public Array2DImpl(Rectangle2I bounds) {
        Validate.notNull(bounds, "bounds are null");
        grid = new Object[bounds.getWidth()][bounds.getHeight()];
        this.bounds = bounds;
    }

    @Override
    public boolean isInside(Vector2I p) {
        return bounds.isInside(p);
    }

    @Override
    public void set(Vector2I p, E e) {
        checkIndices(p);
        p = getInternalPos(p);
        grid[p.getX()][p.getY()] = e;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(Vector2I p) {
        checkIndices(p);
        p = getInternalPos(p);
        return (E) grid[p.getX()][p.getY()];
    }

    private Vector2I getInternalPos(Vector2I p) {
        return new Vector2I(p.getX() - bounds.getX(), p.getY() - bounds.getY());
    }

    @Override
    public Rectangle2I getBounds() {
        return bounds;
    }

    /**
     * Resize the array with new bounds.
     * @param newBounds new bounds
     * @param fillWith fill empty cells with this element
     */
    @Override
    public void resize(Rectangle2I newBounds, E fillWith) {
        Validate.notNull(newBounds, "bounds are null");
        Array2DImpl<E> tmp = new Array2DImpl<E>(newBounds);
        for (int y = newBounds.getY(); y < newBounds.getY2(); y++) {
            for (int x = newBounds.getX(); x < newBounds.getX2(); x++) {
                Vector2I p = new Vector2I(x, y);
                if (bounds.isInside(p)) {
                    tmp.set(p, get(p));
                } else {
                    tmp.set(p, fillWith);
                }
            }
        }
        grid = tmp.grid;
        bounds = tmp.bounds;
    }

    private void checkIndices(Vector2I p) {
        if (!bounds.isInside(p)) {
            throw new IndexOutOfBoundsException("p = " + p + ", bounds = " + bounds);
        }
    }

    /**
     * @return iterable of indices, iterates by lines from up
     */
    @Override
    public Iterable<Vector2I> getIndices() {
        return new Iterable<Vector2I>() {
            @Override
            public Iterator<Vector2I> iterator() {
                return new Iterator<Vector2I>() {
                    int x = bounds.getX();
                    int y = bounds.getY();

                    @Override
                    @Deprecated
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Vector2I next() {
                        Vector2I p = new Vector2I(x, y);
                        x++;
                        if (x >= bounds.getX2()) {
                            x = bounds.getX();
                            y++;
                        }
                        return p;
                    }

                    @Override
                    public boolean hasNext() {
                        return (x < bounds.getX2()) && (y < bounds.getY2());
                    }
                };
            }
        };
    }

}
