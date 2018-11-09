//package cz.wa.secretagent.view.renderer.model;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.view.renderer.PrimitivesDrawer;
//import cz.wa.secretagent.view.texture.TextureToDraw;
//import secretAgent.world.GLModel;
//
///**
// *
// *
// * @author Ondrej Milenovsky
// */
//public abstract class AbstractModelDrawer<M extends GLModel> implements ModelDrawer<M> {
//
//    private static final long serialVersionUID = -5570243308467570348L;
//
//    protected PrimitivesDrawer primitivesDrawer;
//
//    public PrimitivesDrawer getPrimitivesDrawer() {
//        return primitivesDrawer;
//    }
//
//    @Required
//    public void setPrimitivesDrawer(PrimitivesDrawer primitivesDrawer) {
//        this.primitivesDrawer = primitivesDrawer;
//    }
//
//    protected static TextureToDraw getFrame(long currTimeMs, long durationMs, List<TextureToDraw> textures) {
//        currTimeMs = currTimeMs % durationMs;
//        int num = (int) (currTimeMs / (double) durationMs * textures.size());
//        return textures.get(num);
//    }
//
//}
