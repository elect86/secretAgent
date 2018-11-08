package cz.wa.wautils.swing.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for keyPressed
 *
 * @author Ondrej Milenovsky
 */
public abstract class KeyPressedListener extends KeyAdapter {

    private static final Logger logger = LoggerFactory.getLogger(KeyPressedListener.class);

    private void trigger(KeyEvent e) {
        try {
            keyDown(e);
        } catch (Throwable ex) {
            logger.error("", ex);
        }
    }

    protected abstract void keyDown(KeyEvent e);

    @Override
    public void keyPressed(KeyEvent e) {
        trigger(e);
    }

}
