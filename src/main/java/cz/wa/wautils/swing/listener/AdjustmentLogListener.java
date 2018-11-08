package cz.wa.wautils.swing.listener;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public abstract class AdjustmentLogListener extends AbstractLogListener implements AdjustmentListener {
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        trigger();
    }

}
