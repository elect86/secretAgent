package cz.wa.secretagent.menu.window.component.selectable;

import cz.wa.secretagent.menu.window.component.ComponentType;
import cz.wa.secretagent.menu.window.component.GComponent;

/**
 * Some selectable component. 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class GSelectable extends GComponent {
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SELECTABLE;
    }

    public abstract GComponent getDisplayComponent();

    /**
     * Enter pressed on the component.
     * @return true if the component does anything on this action
     */
    public abstract boolean activate();

    /**
     * Left arrow pressed on the component.
     * @return true if the component does anything on this action
     */
    public abstract boolean changeLeft();

    /**
     * Right arrow pressed on the component.
     * @return true if the component does anything on this action
     */
    public abstract boolean changeRight();

}
