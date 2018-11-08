package cz.wa.wautils.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Utils for maps. 
 * 
 * @author Ondrej Milenovsky
 */
public class MapUtils {

    private static final int INITIAL_CAPACITY = 20;
    private static final float LOAD_FACTOR = 0.35f;

    private MapUtils() {
    }

    /**
     * Creates map with fast access, the map will be big.
     * @param map
     * @return
     */
    public static <K, V> Map<K, V> createMinLoadMap(Map<K, V> map) {
        Map<K, V> ret = new HashMap<K, V>(map.size(), LOAD_FACTOR);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    /**
     * Creates map with fast access, the map will be big.
     * @return
     */
    public static <K, V> Map<K, V> createMinLoadMap() {
        return createMinLoadMap(INITIAL_CAPACITY);
    }

    /**
     * Creates map with fast access, the map will be big.
     * @param initialCapacity
     * @return
     */
    public static <K, V> Map<K, V> createMinLoadMap(int initialCapacity) {
        return new HashMap<K, V>(initialCapacity, LOAD_FACTOR);
    }
}
