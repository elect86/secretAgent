package cz.wa.secretagent.menu.window;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.menu.window.component.ComponentType;
import cz.wa.secretagent.menu.window.component.GComponent;
import cz.wa.secretagent.menu.window.component.selectable.GButtonListener;
import cz.wa.secretagent.menu.window.component.selectable.GSelectable;
import cz.wa.wautils.math.Vector2I;

/**
 * Window frame for openGL. 
 * 
 * @author Ondrej Milenovsky
 */
public class GFrame extends GComponent {

    /** Size in tiles */
    private Vector2I sizeTiles;
    /** Color of border tiles */
    private Color borderColor;
    /** Color of middle tiles */
    private Color middleColor;
    /** Child components */
    private Set<GComponent> components;
    /** Transparency alpha */
    private double alpha;
    /** listener called when pressed escape */
    private GButtonListener closeListener;

    /** list of selectable components */
    private List<GSelectable> selectables;
    /** index of selected component */
    private int selectedIndex;

    public GFrame() {
        components = new LinkedHashSet<GComponent>();
        middleColor = Color.WHITE;
        setBorderColor(Color.WHITE);
        alpha = 1;
        selectedIndex = -1;
        selectables = new ArrayList<GSelectable>();
    }

    /**
     * Adds the component to the frame. If the frame already contains the component, then nothing happens.
     * @param c
     */
    public void add(GComponent c) {
        Validate.notNull(c, "the component is null");
        Validate.isTrue(c.getType() != ComponentType.FRAME, "the coponent must not be frame");
        if (components.add(c)) {
            if (c.getType() == ComponentType.SELECTABLE) {
                selectables.add((GSelectable) c);
                if (selectedIndex < 0) {
                    setSelectedIndex(0);
                }
            }
        }
    }

    /**
     * Removes the component from the frame. If the frame already contains the component, then nothing happens.
     * @param c
     */
    public void remove(GComponent c) {
        if (components.remove(c)) {
            if (c.getType() == ComponentType.SELECTABLE) {
                selectables.remove(c);
                if (selectedIndex >= selectables.size()) {
                    selectedIndex = selectables.size() - 1;
                }
            }
        }
    }

    /**
     * @return unmodifiable set of components
     */
    public Set<GComponent> getComponents() {
        return Collections.unmodifiableSet(components);
    }

    public Vector2I getSizeTiles() {
        return sizeTiles;
    }

    public void setSizeTiles(Vector2I sizeTiles) {
        Validate.notNull(sizeTiles, "sizeTiles is null");
        Validate.isTrue((sizeTiles.getX() > 0) && (sizeTiles.getY() > 0),
                "sizeTiles must be > 0 in both dimensions, but is " + sizeTiles);
        this.sizeTiles = sizeTiles;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        Validate.notNull(borderColor, "borderColor is null");
        this.borderColor = borderColor;
    }

    public Color getMiddleColor() {
        return middleColor;
    }

    public void setMiddleColor(Color color) {
        Validate.notNull(color, "color is null");
        this.middleColor = color;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.FRAME;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        Validate.isTrue((alpha > 0) && (alpha <= 1), "alpha must be > 0 and <= 1");
        this.alpha = alpha;
    }

    public GButtonListener getCloseListener() {
        return closeListener;
    }

    public void setCloseListener(GButtonListener closeListener) {
        this.closeListener = closeListener;
    }

    /**
     * Just calls the close listener
     */
    public void callClose() {
        if (closeListener != null) {
            closeListener.actionPerformed();
        }
    }

    /**
     * @return index of selected component or -1
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @param selectedIndex new index of component to select, -1 means none selected
     */
    public void setSelectedIndex(int selectedIndex) {
        Validate.isTrue(selectedIndex < selectables.size(),
                "selectedIndex must be < " + selectables.size() + ", but is " + selectedIndex);
        if (this.selectedIndex >= 0) {
            selectables.get(this.selectedIndex).setSelected(false);
        }
        if (selectedIndex >= 0) {
            selectables.get(selectedIndex).setSelected(true);
        } else {
            selectedIndex = -1;
        }
        this.selectedIndex = selectedIndex;
    }

    /**
     * @return selected component or null
     */
    public GSelectable getSelectedComponent() {
        if (selectedIndex < 0) {
            return null;
        } else {
            return selectables.get(selectedIndex);
        }
    }

    /**
     * @return number of selectable components
     */
    public int getSelectablesCount() {
        return selectables.size();
    }

    /**
     * Select next component. Does nothing, if there are no selectable components.
     */
    public void selectNextComponent() {
        if (selectables.size() > 0) {
            int i = selectedIndex;
            i = (selectedIndex + 1) % selectables.size();
            setSelectedIndex(i);
        }
    }

    /**
     * Select prev component. Does nothing, if there are no selectable components.
     */
    public void selectPrevComponent() {
        if (selectables.size() > 0) {
            int i = selectedIndex;
            i--;
            if (i < 0) {
                i = selectables.size() - 1;
            }
            setSelectedIndex(i);
        }
    }
}
