package cz.wa.wautils.swing.component;

import java.awt.Component;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import org.apache.commons.lang.Validate;

import cz.wa.wautils.swing.listener.ClickListener;

/**
 * Table to edit properties
 *
 * @author Ondrej Milenovsky
 */
public class PropertyEditorTable extends JTable implements PropertyEditor {

    private static final long serialVersionUID = 8262681583836332514L;

    private Map<String, Integer> map;

    private TableModel model;

    public PropertyEditorTable() {
    }

    @Override
    public void init(List<String> properties, String defaultValue) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String p : properties) {
            map.put(p, defaultValue);
        }
        init(map);
    }

    @Override
    public void setHeaderNames(String property, String value) {
        getTableHeader().getColumnModel().getColumn(0).setHeaderValue(property);
        getTableHeader().getColumnModel().getColumn(1).setHeaderValue(value);
    }

    @Override
    @SuppressWarnings("serial")
    public void init(Map<String, String> properties) {
        map = new LinkedHashMap<String, Integer>();
        Object[][] array = new Object[properties.size()][2];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            array[i][0] = entry.getKey();
            array[i][1] = entry.getValue();
            map.put(entry.getKey(), i);
            i++;
        }
        setCellSelectionEnabled(true);
        getTableHeader().setReorderingAllowed(false);
        model = new DefaultTableModel(array, new String[] { "Property", "Value" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };
        setModel(model);
        getColumn(getTableHeader().getColumnModel().getColumn(1).getHeaderValue()).setCellEditor(
                new TextCellEditor());
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
    * Returns value from the text component for the key
    * @param key property name, throws exception if no property
    * @return value
    */
    @Override
    public String getValue(String key) {
        Validate.isTrue(map.containsKey(key), "No property " + key);
        String value = (String) model.getValueAt(map.get(key), 1);
        if (value == null) {
            value = "";
        }
        return value;
    }

    @Override
    public int getIntValue(String key) {
        String value = getValue(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid int value: " + value);
        }
    }

    @Override
    public double getDoubleValue(String key) {
        String value = getValue(key);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid double value: " + value);
        }
    }

    @Override
    public boolean getBooleanValue(String key) {
        return Boolean.parseBoolean(getValue(key));
    }

    @Override
    public void setValue(String key, String value) {
        Validate.isTrue(map.containsKey(key), "No property " + key);
        model.setValueAt(value, map.get(key), 1);
    }

    /**
     * Cell editor
     *
     * @author Ondrej Milenovsky
     */
    private class TextCellEditor implements TableCellEditor {

        private final JTextField tf;
        private int row = -1;

        public TextCellEditor() {
            tf = new JTextField();
            tf.getDocument().addDocumentListener(new ClickListener() {
                @Override
                protected void triggered() {
                    if (row >= 0) {
                        model.setValueAt(tf.getText(), row, 1);
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return tf.getText();
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            row = -1;
            return true;
        }

        @Override
        public void cancelCellEditing() {
            row = -1;
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            tf.setText((String) value);
            this.row = row;
            return tf;
        }

    }
}
