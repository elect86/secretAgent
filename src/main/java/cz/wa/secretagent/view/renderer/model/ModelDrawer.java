package cz.wa.secretagent.view.renderer.model;

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.view.model.GLModel;
import cz.wa.secretagent.world.entity.Entity;

/**
 * Draws some particular model on screen. 
 * 
 * @author Ondrej Milenovsky
 */
public interface ModelDrawer<M extends GLModel> extends Serializable {
    /**
     * Draws particular model.
     * @param model model to draw
     * @param entity entity or level corresponding to the model
     * @param pos position on screen
     * @param scale scale of the model
     */
    void draw(M model, Entity entity, Vector2D pos, double scale);
}
