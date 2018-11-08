package cz.wa.wautils.log4j;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Window with messages that will be shown only on new messages.
 * Can be closed and new messages will open it again, at start is not visible by default.
 * Components are initialized on first message or when visible is set to true.
 * Getters and setters are not thread-safe.
 * 
 * Colors are set from string using Color.decode(): "#00ff00", "0x00FF00"
 * Colors are returned as 6-digit upper case hex string starting #: "#00FF00"
 * 
 * Version: 1
 *
 * @author Ondrej Milenovsky
 */
public class WindowAppender extends AppenderSkeleton {

    private JFrame frame;
    private JTextArea text;
    private JMenuBar menu;
    private JScrollPane scroll;
    private JCheckBoxMenuItem autoBringToFrontCh;
    private JCheckBoxMenuItem autoScrollCh;
    private JCheckBoxMenuItem disableCh;
    private JCheckBoxMenuItem clearOnCloseCh;

    // these fields are only for initialization, they are not updated by user events
    private String title = "Logger messages";
    private Rectangle bounds = getDefaultBounds();
    private boolean autoScroll = true;
    private boolean autoBringToFront = true;
    private boolean disabled = false;
    private boolean clearOnClose = false;
    private Color foreground = Color.BLACK;
    private Color background = Color.WHITE;

    public WindowAppender() {
    }

    private static Rectangle getDefaultBounds() {
        return getCenteredBounds(600, 600);
    }

    private static Rectangle getCenteredBounds(int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Rectangle((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
    }

    private void init() {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setBounds(bounds);

        menu = new JMenuBar();

        JMenu actions = new JMenu("Actions");

        JMenuItem clearBt = new JMenuItem("Clear");
        clearBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        actions.add(clearBt);

        JMenu settings = new JMenu("Settings");

        autoBringToFrontCh = new JCheckBoxMenuItem("Bring to front for every log");
        autoBringToFrontCh.setSelected(autoBringToFront);
        settings.add(autoBringToFrontCh);

        autoScrollCh = new JCheckBoxMenuItem("Auto scroll down");
        autoScrollCh.setSelected(autoScroll);
        settings.add(autoScrollCh);

        clearOnCloseCh = new JCheckBoxMenuItem("Clear on close");
        clearOnCloseCh.setSelected(clearOnClose);
        settings.add(clearOnCloseCh);

        disableCh = new JCheckBoxMenuItem("Disabled");
        disableCh.setSelected(disabled);
        settings.add(disableCh);

        menu.add(settings);
        menu.add(actions);

        text = new JTextArea();
        text.setEditable(false);
        text.setBackground(background);
        text.setForeground(foreground);
        text.setTabSize(8);

        frame.setJMenuBar(menu);
        scroll = new JScrollPane(text);
        frame.add(scroll, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isClearOnClose()) {
                    clear();
                }
            }
        });
    }

    private boolean isInitialized() {
        return frame != null;
    }

    public synchronized void clear() {
        if (isInitialized()) {
            text.setText("");
        }
    }

    public void setVisible(boolean b) {
        if (b && !isInitialized()) {
            init();
        }
        if (isInitialized()) {
            frame.setVisible(b);
        }
    }

    public boolean isVisible() {
        if (!isInitialized()) {
            return false;
        }
        return frame.isVisible();
    }

    /**
     * Brings window to front.
     * Not synchronized but in this case it is thread-safe.
     */
    public void toFront() {
        if (isInitialized()) {
            frame.toFront();
        }
    }

    /**
     * Sends window to back.
     * Not synchronized but in this case it is thread-safe.
     */
    public void toBack() {
        if (isInitialized()) {
            frame.toBack();
        }
    }

    /**
     * Usage:
     * integers separated by comma, spaces are removed
     * 
     * 2 numbers: width, height, window is centered on the screen
     * 
     * 4 numbers: x, y, width, height  
     * 
     * @param str input string, throws exception if wrong format
     * @throws IllegalArgumentException if there is different number of numbers
     * @throws NumberFormatException if there is something else than integers, commas and white spaces
     */
    public void setBoundsDecode(String str) {
        String[] s = str.replace(" ", "").split(",");
        if (s.length == 2) {
            setBounds(getCenteredBounds(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
        } else if (s.length == 4) {
            setBounds(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]),
                    Integer.parseInt(s[3]));
        } else {
            throw new IllegalArgumentException(
                    "Input string must contain 2 or 4 numbers separated by comma, but contins " + s.length
                            + " numbers");
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setBounds(new Rectangle(x, y, width, height));
    }

    public void setBounds(Rectangle r) {
        bounds = r;
        if (isInitialized()) {
            frame.setBounds(bounds);
        }
    }

    public Rectangle getBounds() {
        if (!isInitialized()) {
            return bounds;
        }
        return frame.getBounds();
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        if (isInitialized()) {
            disableCh.setSelected(disabled);
        }
    }

    public boolean isDisabled() {
        if (!isInitialized()) {
            return disabled;
        }
        return disableCh.isSelected();
    }

    public void setAutoScroll(boolean b) {
        autoScroll = b;
        if (isInitialized()) {
            autoScrollCh.setSelected(b);
        }
    }

    public boolean isAutoScroll() {
        if (!isInitialized()) {
            return autoScroll;
        }
        return autoScrollCh.isSelected();
    }

    public void setAutoBringToFront(boolean b) {
        autoBringToFront = b;
        if (isInitialized()) {
            autoBringToFrontCh.setSelected(b);
        }
    }

    public boolean isAutoBringToFront() {
        if (!isInitialized()) {
            return autoBringToFront;
        }
        return autoBringToFrontCh.isSelected();
    }

    public void setClearOnClose(boolean b) {
        clearOnClose = b;
        if (isInitialized()) {
            clearOnCloseCh.setSelected(b);
        }
    }

    public boolean isClearOnClose() {
        if (!isInitialized()) {
            return clearOnClose;
        }
        return clearOnCloseCh.isSelected();
    }

    public void setTitle(String title) {
        this.title = title;
        if (isInitialized()) {
            frame.setTitle(title);
        }
    }

    public String getTitle() {
        if (!isInitialized()) {
            return title;
        }
        return frame.getTitle();
    }

    public void setForeground(String color) {
        this.foreground = Color.decode(color);
        if (isInitialized()) {
            text.setForeground(foreground);
        }
    }

    public String getForeground() {
        if (!isInitialized()) {
            return toHexString(foreground);
        }
        return toHexString(text.getForeground());
    }

    public void setBackground(String color) {
        this.background = Color.decode(color);
        if (isInitialized()) {
            text.setBackground(Color.decode(color));
        }
    }

    public String getBackground() {
        if (!isInitialized()) {
            return toHexString(background);
        }
        return toHexString(text.getBackground());
    }

    @Override
    public synchronized void close() {
        setDisabled(true);
        if (isInitialized()) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    protected void append(LoggingEvent event) {
        if (isDisabled()) {
            return;
        }
        if (!isInitialized()) {
            init();
        }
        final JScrollBar vert = scroll.getVerticalScrollBar();
        final int lastPos = vert.getValue();
        appendEvent(event);

        if (!isAutoScroll()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vert.setValue(lastPos);
                }
            });
        }

        showWindow();
    }

    private void showWindow() {
        if (!isVisible()) {
            setVisible(true);
        }

        if (isAutoBringToFront()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.toFront();
                }
            });
        }
    }

    private void appendEvent(LoggingEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(layout.format(event));
        if (layout.ignoresThrowable()) {
            String[] ex = event.getThrowableStrRep();
            if (ex != null) {
                for (String s : ex) {
                    sb.append(s).append(Layout.LINE_SEP);
                }
            }
        }
        text.append(sb.toString());
    }

    private static String toHexString(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
    }

}
