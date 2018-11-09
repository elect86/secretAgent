//package cz.wa.secretagent.view.renderer.model;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.view.texture.DrawBounds;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.HasModelAngle;
//import secretAgent.view.model.SimpleModel;
//import secretAgent.view.renderer.model.AbstractModelDrawer;
//
///**
// * Draws simple not animated model.
// *
// * @author Ondrej Milenovsky
// */
//public class SimpleModelDrawer extends AbstractModelDrawer<SimpleModel> {
//
//    private static final long serialVersionUID = -21991880645460102L;
//
//    @Override
//    public void draw(SimpleModel model, Entity entity, Vector2D pos, double scale) {
//        if (entity instanceof HasModelAngle) {
//            primitivesDrawer.drawTexture(model.getTexture(), pos, new DrawBounds(model.getBounds()), scale,
//                    ((HasModelAngle) entity).getModelAngle());
//        } else {
//            primitivesDrawer.drawTexture(model.getTexture(), pos, new DrawBounds(model.getBounds()), scale);
//        }
//    }
//
//}
