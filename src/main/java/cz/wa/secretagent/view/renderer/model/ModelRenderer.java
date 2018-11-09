package cz.wa.secretagent.view.renderer.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.view.model.GLModel;
import cz.wa.secretagent.view.model.ModelType;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.game.player.Camera;

/**
 * Renders any model using single drawers, decides which drawer will be used.
 * 
 * @author Ondrej Milenovsky
 */
public class ModelRenderer implements Serializable {
    private static final long serialVersionUID = -3548741344017331536L;

    private static final Logger logger = LoggerFactory.getLogger(ModelRenderer.class);

    private Map<ModelType, ModelDrawer<GLModel>> drawers;

    public Map<ModelType, ModelDrawer<GLModel>> getDrawers() {
        return drawers;
    }

    @Required
    public void setDrawers(Map<ModelType, ModelDrawer<GLModel>> drawers) {
        this.drawers = drawers;
    }

    /**
     * Draws the model
     * @param model model to draw
     * @param entity entity for the model, can be null
     * @param pos position of the model in world
     * @param camera camera
     */
    public void draw(GLModel model, Entity entity, Vector2D pos, Camera camera) {
        // check if model in viewport
        Rectangle2D modelBounds = model.getMaxBounds().move(pos);
        Rectangle2D worldBounds = camera.getWorldBounds();
        if ((worldBounds != null) && !modelBounds.intersects(worldBounds)) {
            return;
        }

        // draw the model
        ModelType type = model.getType();
        if (type != ModelType.EMPTY) {
            if (drawers.containsKey(type)) {
                drawers.get(type).draw(model, entity, camera.getScreenPos(pos), camera.getScale());
            } else {
                logger.warn("Unknown model type: " + type);
            }
        }
    }

}
