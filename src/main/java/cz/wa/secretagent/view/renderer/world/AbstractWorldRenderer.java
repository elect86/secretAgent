package cz.wa.secretagent.view.renderer.world;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.view.model.GLModel;
import cz.wa.secretagent.view.renderer.Renderer;
import cz.wa.secretagent.view.renderer.model.ModelRenderer;
import cz.wa.secretagent.world.entity.Entity;
import secretAgent.game.player.Camera;
import secretAgent.world.ObjectModel;

/**
 * Abstract world renderer.
 * 
 * @author Ondrej Milenovsky
 */
public abstract class AbstractWorldRenderer implements Renderer {
    private static final long serialVersionUID = 3479516547509351802L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractWorldRenderer.class);

    private ModelRenderer modelRenderer;

    public ModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    @Required
    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    protected void renderModel(Entity entity, Camera camera, ObjectModel model, Vector2D pos) {
        if (model instanceof GLModel) {
            modelRenderer.draw((GLModel) model, entity, pos, camera);
        } else {
            logger.warn("Unknown model class: " + model.getClass().getSimpleName());
        }
    }

}
