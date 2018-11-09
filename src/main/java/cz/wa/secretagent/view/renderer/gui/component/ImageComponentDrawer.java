package cz.wa.secretagent.view.renderer.gui.component;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.menu.window.component.GImage;
import cz.wa.secretagent.view.model.GLModel;
import cz.wa.secretagent.view.renderer.model.ModelRenderer;
import secretAgent.game.player.Camera;

/**
 * Draws the model of the image. 
 *
 * @author Ondrej Milenovsky
 */
public class ImageComponentDrawer extends AbstractComponentDrawer<GImage> {

    private static final long serialVersionUID = 5453934946621231128L;

    private ModelRenderer modelRenderer;

    @Override
    public void draw(GImage component, GFrame frame, Camera camera) {
        modelRenderer.draw((GLModel) component.getModel(), null, component.getPosSH(), camera);
    }

    public ModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    @Required
    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

}
