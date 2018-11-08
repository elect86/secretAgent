package cz.wa.wautils.collection;

import java.io.Serializable;

import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;

/**
 * Interface to unmodifiable Array2D 
 * 
 * @author Ondrej Milenovsky
 */
public interface Array2DView<E> extends Serializable {
    boolean isInside(Vector2I p);

    E get(Vector2I p);

    Rectangle2I getBounds();

    /**
     * @return iterable of indices, iterates by lines from up
     */
    Iterable<Vector2I> getIndices();

}
