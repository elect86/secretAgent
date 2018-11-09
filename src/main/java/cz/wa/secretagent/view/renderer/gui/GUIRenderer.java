//package cz.wa.secretagent.view.renderer.gui;
//
//import java.io.Serializable;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.menu.MenuHolder;
//import cz.wa.secretagent.menu.window.GFrame;
//import cz.wa.secretagent.view.Settings2D;
//import cz.wa.secretagent.view.renderer.DrawPosition;
//import cz.wa.secretagent.view.renderer.PrimitivesDrawer;
//import cz.wa.secretagent.view.renderer.Renderer;
//
///**
// * Renders menu windows (options).
// *
// * @author Ondrej Milenovsky
// */
//public class GUIRenderer implements Renderer, Serializable {
//
//    private static final long serialVersionUID = -8156972018160404103L;
//
//    private FrameRenderer frameRenderer;
//    private Settings2D settings2d;
//    private PrimitivesDrawer primitivesDrawer;
//
//    @Override
//    public void init() {
//        // empty
//    }
//
//    @Override
//    public void dispose() {
//        // empty
//    }
//
//    public void drawGUI(MenuHolder menuHolder) {
//        primitivesDrawer.setDrawPosition(DrawPosition.UPPER_LEFT);
//        for (GFrame frame : menuHolder.getFrames()) {
//            // draw the window
//            frameRenderer.drawFrame(frame);
//        }
//        primitivesDrawer.setDrawPosition(DrawPosition.CENTER);
//    }
//
//    public FrameRenderer getFrameRenderer() {
//        return frameRenderer;
//    }
//
//    @Required
//    public void setFrameRenderer(FrameRenderer frameRenderer) {
//        this.frameRenderer = frameRenderer;
//    }
//
//    public Settings2D getSettings2d() {
//        return settings2d;
//    }
//
//    @Required
//    public void setSettings2d(Settings2D settings2d) {
//        this.settings2d = settings2d;
//    }
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
//}
