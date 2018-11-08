package cz.wa.secretagent.menu.window.component;

import cz.wa.secretagent.world.ObjectModel;

/**
 * Image with some model (can only handle models that don't need entity). 
 * 
 * @author Ondrej Milenovsky
 */
public class GImage extends GComponent {
    private final ObjectModel model;

    public GImage(ObjectModel model) {
        this.model = model;
    }

    public ObjectModel getModel() {
        return model;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.IMAGE;
    }
}
