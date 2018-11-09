package cz.wa.secretagent.view.renderer.gui.component;

import java.awt.Color;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.menu.window.component.selectable.GSelectable;
import cz.wa.secretagent.view.renderer.gui.ComponentRenderer;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.game.player.Camera;

/**
 * Draws the display component and selected frame around it. 
 *
 * @author Ondrej Milenovsky
 */
public class SelectedComponentDrawer extends AbstractComponentDrawer<GSelectable> {

    private static final long serialVersionUID = -650455454002484030L;

    private Color selectedColor;
    private Color notSelectedColor;
    private double borderWidthSH;
    private ComponentRenderer componentRenderer;

    @Override
    public void draw(GSelectable component, GFrame frame, Camera camera) {
        // the frame
        Color color = (component.isSelected() ? selectedColor : notSelectedColor);
        int screenHeight = primitivesDrawer.getSettings().screenHeight;
        Rectangle2D rect = new Rectangle2D(component.getPosSH().getX(), component.getPosSH().getY(),
                component.getSizeSH().getX(), component.getSizeSH().getY());
        rect = rect.scalarMultiply(screenHeight).move(camera.getPos());
        primitivesDrawer.drawRect(rect, borderWidthSH * screenHeight, color);
        // the sub component
        componentRenderer.renderComponent(component.getDisplayComponent(), frame, camera);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    @Required
    public void setSelectedColor(Color selectedColor) {
        Validate.notNull(selectedColor, "selectedColor is null");
        this.selectedColor = selectedColor;
    }

    public Color getNotSelectedColor() {
        return notSelectedColor;
    }

    @Required
    public void setNotSelectedColor(Color notSelectedColor) {
        Validate.notNull(notSelectedColor, "notSelectedColor is null");
        this.notSelectedColor = notSelectedColor;
    }

    public double getBorderWidthSH() {
        return borderWidthSH;
    }

    @Required
    public void setBorderWidthSH(double borderWidthSH) {
        this.borderWidthSH = borderWidthSH;
    }

    public ComponentRenderer getComponentRenderer() {
        return componentRenderer;
    }

    @Required
    public void setComponentRenderer(ComponentRenderer componentRenderer) {
        this.componentRenderer = componentRenderer;
    }

}
