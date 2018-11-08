package cz.wa.wautils.collection;

import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;

/**
 * Interface for Array2D
 * 
 * @author Ondrej Milenovsky
 */
public interface Array2D<E> extends Array2DView<E> {

    void set(Vector2I p, E e);

    /**
     * Resize the array with new bounds.
     * @param newBounds new bounds
     * @param fillWith fill empty cells with this element
     */
    void resize(Rectangle2I newBounds, E fillWith);

}
