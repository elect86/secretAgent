package cz.wa.secretagent.view.renderer.gui;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import secretAgent.game.player.Camera;
import secretAgent.menu.window.GFrame;
import secretAgent.menu.window.component.ComponentType;
import secretAgent.menu.window.component.GComponent;
import secretAgent.view.renderer.gui.ComponentDrawer;

/**
 * Class that renders any component (except frame). 
 *
 * @author Ondrej Milenovsky
 */
public class ComponentRenderer implements Serializable {
    private static final long serialVersionUID = -7689554478280250593L;
    private static final Logger logger = LoggerFactory.getLogger(ComponentRenderer.class);

    private Map<ComponentType, ComponentDrawer<GComponent>> componentDrawers;

    public void renderComponent(GComponent component, GFrame frame, Camera camera) {
        ComponentType type = component.getType();
        if (componentDrawers.containsKey(type)) {
            componentDrawers.get(type).draw(component, frame, camera);
        } else {
            logger.warn("No renderer for component type: " + type);
        }
    }

    public Map<ComponentType, ComponentDrawer<GComponent>> getComponentDrawers() {
        return componentDrawers;
    }

    @Required
    public void setComponentDrawers(Map<ComponentType, ComponentDrawer<GComponent>> componentDrawers) {
        this.componentDrawers = componentDrawers;
    }

}
