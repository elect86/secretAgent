package cz.wa.wautils.swing.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class with logger used by listeners.
 *
 * @author Ondrej Milenovsky
 */
public abstract class AbstractLogListener {
    private static final Logger logger = LoggerFactory.getLogger(AbstractLogListener.class);

    public final void trigger() {
        try {
            triggered();
        } catch (Throwable ex) {
            logger.error("", ex);
        }
    }

    protected abstract void triggered();

}
