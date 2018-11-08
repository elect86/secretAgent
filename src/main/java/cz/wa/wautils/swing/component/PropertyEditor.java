package cz.wa.wautils.swing.component;

import java.util.List;
import java.util.Map;

/**
 * interface for PropertyEditorTable
 *
 * @author Ondrej Milenovsky
 */
public interface PropertyEditor {

    void init(List<String> properties, String defaultValue);

    void setHeaderNames(String property, String value);

    void init(Map<String, String> properties);

    boolean containsKey(String key);

    String getValue(String key);

    int getIntValue(String key);

    double getDoubleValue(String key);

    boolean getBooleanValue(String key);

    void setValue(String key, String value);

}
