package cz.wa.wautils.swing.component;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Window with message.
 *
 * @author Ondrej Milenovsky
 */
public class MessageWindow extends JFrame {
    private static final long serialVersionUID = -3042847492198135009L;

    private JTextArea area;

    public MessageWindow(String title, String message) {
        super(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(300, 200, 600, 500);
        initComponents(message);
        setVisible(true);
    }

    private void initComponents(String message) {
        setLayout(new BorderLayout());

        area = new JTextArea(message);
        area.setEditable(false);

        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}
