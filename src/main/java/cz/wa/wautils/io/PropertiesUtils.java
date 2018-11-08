package cz.wa.wautils.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;

/**
 * Utils for properties.
 *
 * @author Ondrej Milenovsky
 */
public class PropertiesUtils {
    private PropertiesUtils() {
    }

    public static Properties loadProperties(File file) throws IOException {
        Validate.notNull(file, "file is null");
        Validate.isTrue(file.isFile(), "File not found: " + file.getAbsolutePath());
        Properties p = new Properties();
        p.load(new FileReader(file));
        return p;
    }

    public static Properties loadProperties(InputStream input) throws IOException {
        Validate.notNull(input, "file is null");
        Properties p = new Properties();
        p.load(input);
        return p;
    }

    /**
     * Tries to get all properties and inject to public objects fields.
     * @param src input properties
     * @param target target object
     * @param removeFromProperties if true, then removes all injected properties from the input
     * @return set of all public fields of the target that have not been injected
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Collection<String> injectToObject(Properties src, Object target,
            boolean removeFromProperties) {
        Set<String> remaining = new HashSet<String>();
        for (Field field : target.getClass().getFields()) {
            remaining.add(field.getName());
        }

        for (Iterator<Map.Entry<Object, Object>> it = src.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Object, Object> entry = it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            try {
                Field field = target.getClass().getField(key);
                boolean injected = false;
                if (field.getType() == String.class) {
                    field.set(target, value);
                    injected = true;
                } else if ((field.getType() == Boolean.TYPE) || (field.getType() == Boolean.class)) {
                    field.set(target, Boolean.parseBoolean(value));
                    injected = true;
                } else if ((field.getType() == Integer.TYPE) || (field.getType() == Integer.class)) {
                    field.set(target, Integer.decode(value));
                    injected = true;
                } else if ((field.getType() == Double.TYPE) || (field.getType() == Double.class)) {
                    field.set(target, Double.parseDouble(value));
                    injected = true;
                } else if (field.getType().isEnum()) {
                    field.set(target, Enum.valueOf((Class) field.getType(), value));
                    injected = true;
                } else if (field.getType() == Set.class) {
                    Set<String> set = strToSet(value);
                    field.set(target, set);
                    injected = true;
                }
                if (injected) {
                    remaining.remove(key);
                    if (removeFromProperties) {
                        it.remove();
                    }
                }
            } catch (Exception e) {
                // continue on all exceptions
            }
        }
        return remaining;
    }

    /**
     * Creates map field->value from the object.
     * The map consist of all public fields from the object and values are created by toString(). 
     * @param src source object
     * @return new LinkedHashMap
     */
    public static Map<String, String> createMap(Object src) {
        Map<String, String> ret = new LinkedHashMap<String, String>();
        for (Field field : src.getClass().getFields()) {
            try {
                Object value = field.get(src);
                if (value != null) {
                    ret.put(field.getName(), value.toString());
                }
            } catch (Exception e) {
                // continue on all exceptions
            }
        }
        return ret;
    }

    /**
     * Saves the map to file with properties format.
     * @param file target file
     * @param map map to save
     * @throws IOException
     */
    public static void saveMapAsProperties(File file, Map<String, String> map) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        FileUtils.write(file, sb, "ISO-8859-1");
    }

    /**
     * Returns all strings separated by white spaces from input in LinkedHashSet
     * @param str
     * @return
     */
    public static LinkedHashSet<String> strToSet(String str) {
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        for (String s : str.split("\\s+")) {
            if (!s.isEmpty()) {
                set.add(s);
            }
        }
        return set;
    }

}
