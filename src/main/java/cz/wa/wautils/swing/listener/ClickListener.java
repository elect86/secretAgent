package cz.wa.wautils.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Action and item listener that logs every exception.
 *
 * @author Ondrej Milenovsky
 */
public abstract class ClickListener extends AbstractLogListener implements ActionListener, ItemListener,
        DocumentListener {

    @Override
    public final void actionPerformed(ActionEvent e) {
        trigger();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        trigger();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        trigger();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        trigger();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        trigger();
    }

}
