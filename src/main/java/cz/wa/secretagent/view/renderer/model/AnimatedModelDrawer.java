package cz.wa.secretagent.view.renderer.model;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.view.texture.DrawBounds;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.HasDuration;
import cz.wa.secretagent.world.entity.HasModelAngle;
import cz.wa.secretagent.world.entity.HasTime;
import cz.wa.secretagent.worldinfo.WorldHolder;
import secretAgent.view.model.AnimatedModel;
import secretAgent.view.renderer.model.AbstractModelDrawer;

/**
 * Draws animated model. 
 *  
 * @author Ondrej Milenovsky
 */
public class AnimatedModelDrawer extends AbstractModelDrawer<AnimatedModel> {
    private static final long serialVersionUID = 6233405165118049099L;

    private WorldHolder worldHolder;

    @Override
    public void draw(AnimatedModel model, Entity entity, Vector2D pos, double scale) {
        // time from the entity or from level
        long timeMs;
        if (entity instanceof HasTime) {
            timeMs = ((HasTime) entity).getTimeMs();
        } else {
            timeMs = worldHolder.getWorld().getLevelMap().getTimeMs();
        }

        // duration from the entity or from the model
        long durationMs;
        if (entity instanceof HasDuration) {
            durationMs = ((HasDuration) entity).getDurationMs();
        } else {
            durationMs = model.getDurationMs();
        }

        // draw the texture
        List<TextureToDraw> textures = model.getTextures();
        TextureToDraw tex = getFrame(timeMs, durationMs, textures);
        if (entity instanceof HasModelAngle) {
            primitivesDrawer.drawTexture(tex, pos, new DrawBounds(model.getBounds()), scale,
                    ((HasModelAngle) entity).getModelAngle());
        } else {
            primitivesDrawer.drawTexture(tex, pos, new DrawBounds(model.getBounds()), scale);
        }
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

}
