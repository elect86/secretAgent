//package cz.wa.secretagent.view.renderer.gui;
//
//import java.awt.Color;
//import java.io.Serializable;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.menu.window.GFrame;
//import cz.wa.secretagent.menu.window.component.GComponent;
//import cz.wa.secretagent.view.Settings2D;
//import cz.wa.secretagent.view.renderer.PrimitivesDrawer;
//import cz.wa.secretagent.view.renderer.model.ModelRenderer;
//import cz.wa.secretagent.view.texture.DrawBounds;
//import cz.wa.secretagent.worldinfo.WorldHolder;
//import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
//import cz.wa.secretagent.worldinfo.graphics.TilesInfo;
//import cz.wa.wautils.math.Vector2I;
//import secretAgent.game.player.Camera;
//import secretAgent.view.model.SimpleModel;
//import secretAgent.world.GLModel;
//import secretAgent.world.ModelType;
//import secretAgent.world.ObjectModel;
//
///**
// * Renders frame made of tiles.
// *
// * @author Ondrej Milenovsky
// */
//public class TileFrameRenderer implements FrameRenderer, Serializable {
//
//    private static final long serialVersionUID = -1022332308847341159L;
//
//    private static final String BORDER_UPPER_LEFT = "borderUL";
//    private static final String BORDER_UPPER = "borderU";
//    private static final String BORDER_UPPER_RIGHT = "borderUR";
//    private static final String BORDER_LOWER_RIGHT = "borderDR";
//    private static final String BORDER_LOWER_LEFT = "borderDL";
//    private static final String BORDER_LEFT = "borderL";
//    private static final String BORDER_LOWER = "borderD";
//    private static final String BORDER_RIGHT = "borderR";
//    private static final String MIDDLE = "middle";
//
//    private WorldHolder worldHolder;
//    private ModelRenderer modelRenderer;
//    private PrimitivesDrawer primitivesDrawer;
//    private Settings2D settings2d;
//    private ComponentRenderer componentRenderer;
//
//    @Override
//    public void drawFrame(GFrame frame) {
//        TilesInfo tileSet = worldHolder.getGraphicsInfo().getTileSet(GraphicsInfo.ORIG_GUI_SPRITES_ID);
//        Camera camera = new Camera(new Vector2I(settings2d.screenWidth, settings2d.screenHeight),
//                (double) settings2d.screenHeight / tileSet.getTileSize().getY() * frame.getSizeSH().getY()
//                        / frame.getSizeTiles().getY());
//        drawBorder(frame, tileSet, camera);
//        drawMiddle(frame, tileSet, camera);
//        drawContent(frame, camera);
//    }
//
//    /**
//     * Draw only the border
//     */
//    private void drawBorder(GFrame frame, TilesInfo tileSet, Camera camera) {
//        GraphicsInfo grInfo = worldHolder.getGraphicsInfo();
//        ObjectModel ul = grInfo.getModel(BORDER_UPPER_LEFT);
//        ObjectModel u = grInfo.getModel(BORDER_UPPER);
//        ObjectModel ur = grInfo.getModel(BORDER_UPPER_RIGHT);
//        ObjectModel r = grInfo.getModel(BORDER_RIGHT);
//        ObjectModel dr = grInfo.getModel(BORDER_LOWER_RIGHT);
//        ObjectModel d = grInfo.getModel(BORDER_LOWER);
//        ObjectModel dl = grInfo.getModel(BORDER_LOWER_LEFT);
//        ObjectModel l = grInfo.getModel(BORDER_LEFT);
//
//        TileDrawer td = new TileDrawer(frame, camera);
//
//        // corners
//        primitivesDrawer.setColor(frame.getBorderColor(), false);
//        Vector2I drPos = frame.getSizeTiles().substract(new Vector2I(1, 1));
//        td.drawTile(ul, Vector2I.ZERO);
//        td.drawTile(ur, new Vector2I(drPos.getX(), 0));
//        td.drawTile(dr, drPos);
//        td.drawTile(dl, new Vector2I(0, drPos.getY()));
//
//        // up and down horizontal borders
//        for (int i = 1; i < drPos.getX(); i++) {
//            td.drawTile(u, new Vector2I(i, 0));
//            td.drawTile(d, new Vector2I(i, drPos.getY()));
//        }
//        // left and right vertical borders
//        for (int i = 1; i < drPos.getY(); i++) {
//            td.drawTile(l, new Vector2I(0, i));
//            td.drawTile(r, new Vector2I(drPos.getX(), i));
//        }
//    }
//
//    /**
//     * Draw only the filling tiles.
//     */
//    private void drawMiddle(GFrame frame, TilesInfo tileSet, Camera camera) {
//        ObjectModel m = worldHolder.getGraphicsInfo().getModel(MIDDLE);
//        TileDrawer td = new TileDrawer(frame, camera);
//
//        primitivesDrawer.setColor(frame.getMiddleColor(), false);
//        for (int y = 1; y < frame.getSizeTiles().getY() - 1; y++) {
//            for (int x = 1; x < frame.getSizeTiles().getX() - 1; x++) {
//                td.drawTile(m, new Vector2I(x, y));
//            }
//        }
//    }
//
//    /**
//     * Draw components on the frame. Will change the camera position.
//     */
//    private void drawContent(GFrame frame, Camera camera) {
//        primitivesDrawer.setColor(Color.WHITE, false);
//        camera.setPos(frame.getPosSH().scalarMultiply(settings2d.screenHeight));
//        for (GComponent component : frame.getComponents()) {
//            componentRenderer.renderComponent(component, frame, camera);
//        }
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
//    public ModelRenderer getModelRenderer() {
//        return modelRenderer;
//    }
//
//    @Required
//    public void setModelRenderer(ModelRenderer modelRenderer) {
//        this.modelRenderer = modelRenderer;
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
//    public Settings2D getSettings2d() {
//        return settings2d;
//    }
//
//    @Required
//    public void setSettings2d(Settings2D settings2d) {
//        this.settings2d = settings2d;
//    }
//
//    public ComponentRenderer getComponentRenderer() {
//        return componentRenderer;
//    }
//
//    @Required
//    public void setComponentRenderer(ComponentRenderer componentRenderer) {
//        this.componentRenderer = componentRenderer;
//    }
//
//    private class TileDrawer {
//        private final GFrame frame;
//        private final Camera camera;
//        private final DrawBounds bounds;
//
//        public TileDrawer(GFrame frame, Camera camera) {
//            this.frame = frame;
//            this.camera = camera;
//            double tw = frame.getSizeSH().getX() * settings2d.screenHeight / frame.getSizeTiles().getX();
//            double th = frame.getSizeSH().getY() * settings2d.screenHeight / frame.getSizeTiles().getY();
//            bounds = new DrawBounds(0, 0, tw, th);
//        }
//
//        public void drawTile(ObjectModel model, Vector2I posGrid) {
//            Vector2D posOnFrame = new Vector2D(
//                    posGrid.getX() * frame.getSizeSH().getX() / frame.getSizeTiles().getX(),
//                    posGrid.getY() * frame.getSizeSH().getY() / frame.getSizeTiles().getY());
//            Vector2D pos = frame.getPosSH().add(posOnFrame).scalarMultiply(settings2d.screenHeight);
//            GLModel glModel = (GLModel) model;
//            if (glModel.getType() == ModelType.SIMPLE) {
//                SimpleModel m = (SimpleModel) glModel;
//                primitivesDrawer.drawTexture(m.getTexture(), pos, bounds, 1);
//            } else {
//                modelRenderer.draw(glModel, null, pos, camera);
//            }
//        }
//
//    }
//
//}
