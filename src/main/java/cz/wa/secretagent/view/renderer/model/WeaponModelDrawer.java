//package cz.wa.secretagent.view.renderer.model;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.view.texture.DrawBounds;
//import cz.wa.secretagent.view.texture.TextureToDraw;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.worldinfo.WorldHolder;
//import secretAgent.view.model.AnimatedModel;
//import secretAgent.view.model.WeaponModel;
//import secretAgent.view.renderer.model.AbstractModelDrawer;
//
///**
// * Draws weapon model, used when drawing on screen only!
// * Weapon in world is drawn with the agent.
// *
// * @author Ondrej Milenovsky
// */
//public class WeaponModelDrawer extends AbstractModelDrawer<WeaponModel> {
//
//    private static final long serialVersionUID = 4014057635662109586L;
//
//    private WorldHolder worldHolder;
//
//    @Override
//    public void draw(WeaponModel model, Entity entity, Vector2D pos, double scale) {
//        AnimatedModel model2 = model.getModel(false);
//        List<TextureToDraw> textures = model2.getTextures();
//        TextureToDraw tex = getFrame(getWorldHolder().getWorld().getLevelMap().getTimeMs(),
//                model2.getDurationMs(), textures);
//        primitivesDrawer.drawTexture(tex, pos, new DrawBounds(model.getBounds()), scale);
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
