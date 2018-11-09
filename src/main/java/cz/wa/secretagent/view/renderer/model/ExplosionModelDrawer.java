package cz.wa.secretagent.view.renderer.model;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.view.texture.DrawBounds;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.explosion.Explosion;
import cz.wa.secretagent.worldinfo.WorldHolder;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.view.model.ExplosionModel;
import secretAgent.view.renderer.model.AbstractModelDrawer;

/**
 * Draws explosion model. 
 *  
 * @author Ondrej Milenovsky
 */
public class ExplosionModelDrawer extends AbstractModelDrawer<ExplosionModel> {
    private static final long serialVersionUID = -7708654087126840532L;

    private static final Logger logger = LoggerFactory.getLogger(ExplosionModelDrawer.class);

    private WorldHolder worldHolder;

    @Override
    public void draw(ExplosionModel model, Entity entity, Vector2D pos, double scale) {
        if (!(entity instanceof Explosion)) {
            logger.warn("Entity must be Explosion, but is: " + entity.getClass().getSimpleName());
            return;
        }
        Explosion explosion = (Explosion) entity;
        long timeMs = Math.round(explosion.getTimeS() * 1000);
        long durationMs = Math.round(explosion.getDurationS() * 1000);

        // draw the texture
        List<TextureToDraw> textures = model.getTextures();
        TextureToDraw tex = getFrame(timeMs, durationMs, textures);
        double radius = explosion.getRadius();
        double height = tex.getTileBounds().getHeight() / tex.getTileBounds().getWidth();
        Rectangle2D bounds = new Rectangle2D(-radius, -radius * height * 2.0 + radius, radius * 2.0,
                radius * height * 2.0);
        primitivesDrawer.drawTexture(tex, pos, new DrawBounds(bounds), scale);
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

}
