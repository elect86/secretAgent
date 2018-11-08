package cz.wa.secretagent.menu.window.component;

import java.awt.Color;

import org.apache.commons.lang.Validate;

/**
 * Text on screen. 
 * 
 * @author Ondrej Milenovsky
 */
public class GLabel extends GComponent {
    private String text;
    private Color color;

    public GLabel() {
        color = Color.WHITE;
    }

    public GLabel(String text) {
        this();
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        Validate.notNull(text, "text is null");
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        Validate.notNull(color, "color is null");
        this.color = color;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.LABEL;
    }
}
