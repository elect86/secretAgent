package cz.wa.wautils.swing.component;

import java.awt.Component;

import javax.swing.JOptionPane;

public class WaDialogs {

    private WaDialogs() {
    }

    /**
     * Opens question dialog.
     * @param frame parent
     * @param title title
     * @param message message
     * @param options list of options, the last one is used if the dialog is closed
     * @return index of chosen option
     */
    public static int showDialog(Component frame, String title, String message, String... options) {
        int ret = JOptionPane.showOptionDialog(frame, message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[options.length - 1]);
        if (ret < 0) {
            ret = options.length - 1;
        }
        return ret;
    }

}
