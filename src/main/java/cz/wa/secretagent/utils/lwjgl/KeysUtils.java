package cz.wa.secretagent.utils.lwjgl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.wautils.io.PropertiesUtils;

/**
 * Parses and saves keys definition from a properties file. 
 * 
 * @author Ondrej Milenovsky
 */
public class KeysUtils {
    private static final Logger logger = LoggerFactory.getLogger(KeysUtils.class);

    private KeysUtils() {
    }

    public static void loadKeys(File file, Object keys) throws IOException {
        Properties pr = new Properties();
        pr.load(new FileReader(file));
        for (Object key : new ArrayList<Object>(pr.keySet())) {
            convertKeyToInt(pr, (String) key);
        }

        PropertiesUtils.injectToObject(pr, keys, false);
    }

    public static void saveKeys(File file, Object keys) throws IOException {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Field field : keys.getClass().getFields()) {
            try {
                Object value = field.get(keys);
                if (value != null) {
                    map.put(field.getName(), Keyboard.getKeyName((int) value));
                }
            } catch (Exception e) {
                logger.error("Error saving keys to " + file.getAbsolutePath(), e);
            }
        }
        PropertiesUtils.saveMapAsProperties(file, map);
    }

    private static void convertKeyToInt(Properties pr, String key) {
        String value = (String) pr.remove(key);
        int code = Keyboard.getKeyIndex(value);
        if (code == Keyboard.KEY_NONE) {
            logger.warn(key + " - unknown key: " + value);
        } else {
            pr.setProperty(key, code + "");
        }
    }

}
