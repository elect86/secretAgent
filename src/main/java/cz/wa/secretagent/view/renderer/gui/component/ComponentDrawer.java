package cz.wa.secretagent.view.renderer.gui.component;

import java.io.Serializable;

import cz.wa.secretagent.game.player.Camera;
import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.menu.window.component.GComponent;

/**
 * Draws single GComponent. 
 * 
 * @author Ondrej Milenovsky
 */
public interface ComponentDrawer<C extends GComponent> extends Serializable {

    /**
     * Draws the component.
     * @param component component to draw
     * @param frame parent frame
     * @param camera camera
     */
    void draw(C component, GFrame frame, Camera camera);

}
