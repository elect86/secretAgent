package cz.wa.wautils.collection;

import java.io.Serializable;

/**
 * Map that uses TypedKey to access elements, takes care of casting.
 * 
 * @author Ondrej Milenovsky
 */
public interface TypedMap extends Serializable {

    /**
     * @return if nulls are allowed in the map,
     *          if true, throws an exception when putting null as value
     */
    boolean isAllowNull();

    /**
     * @param allowNull if nulls are allowed in the map,
     *          if true, throws an exception when putting null as value
     */
    void setAllowNull(boolean allowNull);

    /**
     * @return if true, throws an exception when retrieving key,
     *                  that is not present in the map
     */
    boolean isExceptionWhenEmpty();

    /**
     * @param exceptionWhenEmpty if true, throws an exception when retrieving key,
     *                  that is not present in the map
     */
    void setExceptionWhenEmpty(boolean exceptionWhenEmpty);

    /**
     * @param key key
     * @return casted value for the key
     */
    <C> C get(TypedKey<C> key);

    /**
     * @param key key to remove
     * @return casted removed value or null
     */
    <C> C remove(TypedKey<C> key);

    /**
     * @param key key
     * @param value new value
     * @return casted old value
     */
    <C> C put(TypedKey<C> key, C value);

    /**
     * @param key key
     * @return true if there is entry for the key
     */
    boolean containsKey(TypedKey<?> key);

}
