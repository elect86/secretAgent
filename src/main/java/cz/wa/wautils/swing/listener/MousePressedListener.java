package cz.wa.wautils.swing.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mouse pressed listener.
 *
 * @author Ondrej Milenovsky
 */
public abstract class MousePressedListener extends MouseAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MousePressedListener.class);

    private void trigger(MouseEvent e) {
        try {
            mouseDown(e);
        } catch (Throwable ex) {
            logger.error("", ex);
        }
    }

    public abstract void mouseDown(MouseEvent e);

    @Override
    public void mousePressed(MouseEvent e) {
        trigger(e);
    }

}
