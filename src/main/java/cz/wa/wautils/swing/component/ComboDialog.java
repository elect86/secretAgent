package cz.wa.wautils.swing.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import org.apache.commons.lang.Validate;

import cz.wa.wautils.swing.listener.ClickListener;

/**
 * Dialog with single combo box.
 *
 * @author Ondrej Milenovsky
 */
public class ComboDialog extends JDialog {

    private static final long serialVersionUID = 5697735541145734967L;

    private JLabel label;
    private JComboBox<Object> combo;
    private JButton okBt;
    private JButton cancelBt;

    private Object result;

    public ComboDialog(Window parent) {
        super(parent);
        setModal(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel p = new JPanel();
        //p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        add(p, BorderLayout.CENTER);

        label = new JLabel();
        p.add(label);

        combo = new JComboBox<Object>();
        combo.setEditable(false);
        combo.setBackground(new ColorUIResource(Color.WHITE));
        p.add(combo);

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p.add(p1);

        okBt = new JButton("Ok");
        okBt.addActionListener(new ClickListener() {
            @Override
            protected void triggered() {
                result = combo.getSelectedItem();
                setVisible(false);
            }
        });
        p1.add(okBt);

        cancelBt = new JButton("Cancel");
        cancelBt.addActionListener(new ClickListener() {
            @Override
            protected void triggered() {
                result = null;
                setVisible(false);
            }
        });
        p1.add(cancelBt);
    }

    /**
     * Shows dialog with message and combo box made of items.
     * @param title dialog title
     * @param message message
     * @param items list of items
     * @return one of the items or null
     */
    @SuppressWarnings("unchecked")
    public <C> C showDialog(String title, String message, List<C> items) {
        Validate.notEmpty(items, "items are empty");
        result = null;
        setTitle(title);
        label.setText(message);
        combo.setModel(new DefaultComboBoxModel<Object>(items.toArray(new Object[0])));
        combo.setSelectedIndex(0);

        setBounds(400, 400, 200, 120);
        setVisible(true);
        return (C) result;
    }
}
