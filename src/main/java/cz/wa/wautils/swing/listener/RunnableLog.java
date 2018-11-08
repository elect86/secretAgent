package cz.wa.wautils.swing.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable with logger.
 *
 * @author Ondrej Milenovsky
 */
public abstract class RunnableLog implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RunnableLog.class);

    @Override
    public void run() {
        try {
            run2();
        } catch (Throwable e) {
            logger.error("", e);
        }
    }

    public abstract void run2();
}
