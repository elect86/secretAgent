package cz.wa.wautils.swing.listener;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Popup menu will become visible listener. 
 *
 * @author Ondrej Milenovsky
 */
public abstract class PopupVisibleListener extends AbstractLogListener implements PopupMenuListener {
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        trigger();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }
}
