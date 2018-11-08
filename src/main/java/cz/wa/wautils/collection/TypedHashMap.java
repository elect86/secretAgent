package cz.wa.wautils.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap<String, Object> extended by TypedKey with casting by type of key.
 * 
 * @author Ondrej Milenovsky
 */
public class TypedHashMap extends HashMap<String, Object> implements TypedMap {

    private static final long serialVersionUID = -3710445545254690983L;

    private boolean allowNull = true;
    private boolean exceptionWhenEmpty = false;

    public TypedHashMap() {
        super();
    }

    public TypedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TypedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TypedHashMap(Map<String, ?> m) {
        super(m);
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#isAllowNull()
     */
    @Override
    public boolean isAllowNull() {
        return allowNull;
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#setAllowNull(boolean)
     */
    @Override
    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#isExceptionWhenEmpty()
     */
    @Override
    public boolean isExceptionWhenEmpty() {
        return exceptionWhenEmpty;
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#setExceptionWhenEmpty(boolean)
     */
    @Override
    public void setExceptionWhenEmpty(boolean exceptionWhenEmpty) {
        this.exceptionWhenEmpty = exceptionWhenEmpty;
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#get(wa.wautils.collection.TypedKey)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C> C get(TypedKey<C> key) {
        if (exceptionWhenEmpty && !containsKey(key)) {
            throw new IllegalStateException("Map does not contain " + key);
        }
        return (C) super.get(key.key);
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#remove(wa.wautils.collection.TypedKey)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C> C remove(TypedKey<C> key) {
        if (exceptionWhenEmpty && !containsKey(key)) {
            throw new IllegalStateException("Map does not contain " + key);
        }
        return (C) super.remove(key.key);
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#put(wa.wautils.collection.TypedKey, C)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C> C put(TypedKey<C> key, C value) {
        if (!allowNull && (value == null)) {
            throw new IllegalArgumentException("Value for " + key + " is null");
        }
        return (C) super.put(key.key, value);
    }

    /* (non-Javadoc)
     * @see wa.wautils.collection.TypedMap#containsKey(wa.wautils.collection.TypedKey)
     */
    @Override
    public boolean containsKey(TypedKey<?> key) {
        return super.containsKey(key.key);
    }
}
