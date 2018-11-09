//package cz.wa.secretagent.view.renderer.model;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.view.texture.DrawBounds;
//import cz.wa.secretagent.view.texture.TextureToDraw;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.laser.LaserEntity;
//import cz.wa.secretagent.world.entity.laser.LaserType;
//import cz.wa.secretagent.world.entity.laser.LineLaser;
//import cz.wa.secretagent.worldinfo.WorldHolder;
//import cz.wa.wautils.math.Rectangle2D;
//import cz.wa.wautils.math.VectorUtils;
//import secretAgent.view.model.LaserModel;
//import secretAgent.view.renderer.model.AbstractModelDrawer;
//
///**
// * Draws simple not animated model.
// *
// * @author Ondrej Milenovsky
// */
//public class LaserModelDrawer extends AbstractModelDrawer<LaserModel> {
//    private static final long serialVersionUID = -7866747539212406576L;
//
//    private static final Logger logger = LoggerFactory.getLogger(LaserModelDrawer.class);
//
//    private WorldHolder worldHolder;
//
//    @Override
//    public void draw(LaserModel model, Entity entity, Vector2D pos, double scale) {
//        if (!(entity instanceof LaserEntity)) {
//            logger.warn("Entity must be laser, but is: " + entity.getClass().getSimpleName());
//            return;
//        }
//        LaserEntity laser = (LaserEntity) entity;
//
//        List<TextureToDraw> textures = model.getTextures();
//        if (textures == null) {
//            logger.warn("Laser model has not linked textures: " + laser.getSecondType());
//            return;
//        }
//        long timeMs = worldHolder.getWorld().getLevelMap().getTimeMs();
//        TextureToDraw tex = getFrame(timeMs, model.getDurationMs(), textures);
//        if (laser.getSecondType() == LaserType.LINE) {
//            LineLaser line = (LineLaser) laser;
//            double length = line.getPos().distance(line.getPos2());
//            double angle = VectorUtils.getAngle(line.getPos(), line.getPos2());
//            double width = line.getWidth();
//            tex = getTextureToDraw(tex, length / width);
//            DrawBounds bounds = new DrawBounds(0, -width / 2.0, length, width / 2.0);
//            // draw laser
//            primitivesDrawer.setTexColor(model.getColor());
//            primitivesDrawer.drawTexture(tex, pos, bounds, scale, angle);
//        } else {
//            // modify texture bounds
//            double length = laser.getSizeBounds().getWidth() / laser.getSizeBounds().getHeight();
//            tex = getTextureToDraw(tex, length);
//            // draw laser
//            primitivesDrawer.setTexColor(model.getColor());
//            primitivesDrawer.drawTexture(tex, pos, new DrawBounds(laser.getSizeBounds()), scale);
//        }
//    }
//
//    private TextureToDraw getTextureToDraw(TextureToDraw tex, double length) {
//        Rectangle2D tb = tex.getTileBounds();
//        tb = new Rectangle2D(tb.getX(), tb.getY(), length * tb.getHeight(), tb.getHeight());
//        tex = new TextureToDraw(tex.getTexture(), tb);
//        return tex;
//    }
//
//    public WorldHolder getWorldHolder() {
//        return worldHolder;
//    }
//
//    @Required
//    public void setWorldHolder(WorldHolder worldHolder) {
//        this.worldHolder = worldHolder;
//    }
//
//}
