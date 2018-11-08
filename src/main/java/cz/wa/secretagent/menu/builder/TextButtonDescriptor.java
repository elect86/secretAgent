package cz.wa.secretagent.menu.builder;

import cz.wa.secretagent.menu.window.component.selectable.GButtonListener;

/**
 * Describes single button with text. 
 * 
 * @author Ondrej Milenovsky
 */
public class TextButtonDescriptor {
    private final String text;
    private final GButtonListener listener;

    public TextButtonDescriptor(String text, GButtonListener listener) {
        this.text = text;
        this.listener = listener;
    }

    public String getText() {
        return text;
    }

    public GButtonListener getListener() {
        return listener;
    }
}
