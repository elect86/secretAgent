package cz.wa.wautils.swing.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window closing listener.
 *
 * @author Ondrej Milenovsky
 */
public abstract class WindowClosingListener extends AbstractLogListener implements WindowListener {

    @Override
    public void windowClosing(WindowEvent e) {
        trigger();
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
