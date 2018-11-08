package cz.wa.secretagent.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.wautils.io.FileInputStreamNamed;
import cz.wa.wautils.io.PropertiesUtils;

/**
 * Util methods loading properties from resource to object.
 *
 * @author Ondrej Milenovsky
 */
public class PropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    private static final String CLASSPATH_PREFIX = "classpath:";

    public PropertiesLoader() {
    }

    public <C> C loadProperties(String file, Class<C> clazz) {
        try {
            C ret = clazz.newInstance();
            loadPropertiesToObject(file, ret);
            return ret;
        } catch (Throwable e) {
            logger.error("", e);
            return null;
        }
    }

    public static void loadPropertiesToObject(String file, Object obj) {
        loadPropertiesToObject(getInputStream(file), obj);
    }

    /**
     * Loads the file to properties, then injects its properties to public fields of the object.
     * Logs all missing and extra properties. 
     * @throws RuntimeException wrapping IOException
     * @throws IllegalArgumentException for null input
     * @throws URISyntaxException
     * @param input input file
     * @param obj object to be injected
     */
    public static void loadPropertiesToObject(InputStream input, Object obj) {
        Validate.notNull(input, "input is null");
        Validate.notNull(obj, "object is null");

        try {
            Properties pr = PropertiesUtils.loadProperties(input);
            Collection<String> remaining = PropertiesUtils.injectToObject(pr, obj, true);
            if (!pr.isEmpty() || !remaining.isEmpty()) {
                logger.warn("FileSettings: " + input.toString());
                for (Map.Entry<Object, Object> entry : pr.entrySet()) {
                    logger.warn("Unknown property: " + entry.getKey() + " = " + entry.getValue());
                }
                for (String key : remaining) {
                    logger.warn("Missing property: " + key);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns input stream for system file or classpath resource, if the path starts with classpath:, then it is resource.
     * If file or resource does not exist, then throws exceptions.
     * @param resourcePath
     * @return
     */
    public static InputStream getInputStream(String resourcePath) {
        if (resourcePath.toLowerCase().startsWith(CLASSPATH_PREFIX)) {
            resourcePath = resourcePath.substring(CLASSPATH_PREFIX.length());
            InputStream is = ClassLoader.getSystemResourceAsStream(resourcePath);
            if (is == null) {
                throw new RuntimeException("Missing resource: " + resourcePath);
            }
            return is;
        }
        if (new File(resourcePath).isFile()) {
            try {
                return new FileInputStreamNamed(resourcePath);
            } catch (IOException e) {
                throw new RuntimeException("Wrong file name: " + resourcePath, e);
            }
        } else {
            throw new RuntimeException("File not found: " + resourcePath);
        }
    }
}
