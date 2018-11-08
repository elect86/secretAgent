package cz.wa.wautils.swing.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Key pressed listener reacting on key enter.
 *
 * @author Ondrej Milenovsky
 */
public abstract class EnterPressedListener extends AbstractLogListener implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            trigger();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
