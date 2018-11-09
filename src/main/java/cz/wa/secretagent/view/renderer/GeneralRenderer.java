package cz.wa.secretagent.view.renderer;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.menu.MenuHolder;
import cz.wa.secretagent.view.Settings2D;
import cz.wa.secretagent.view.renderer.hud.WorldHUDRenderer;
import cz.wa.secretagent.worldinfo.WorldHolder;
import cz.wa.wautils.math.Rectangle2I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.game.player.Camera;
import secretAgent.view.renderer.DrawPosition;
import secretAgent.view.renderer.EntitiesRenderer;
import secretAgent.view.renderer.LevelRenderer;
import secretAgent.view.renderer.Renderer;
import secretAgent.view.renderer.gui.GuiRenderer;
import secretAgent.view.renderer.hud.ScreenHudRenderer;
import secretAgent.world.SamWorld;

import java.io.Serializable;

/**
 * Renders the screen.
 * 
 * @author Ondrej Milenovsky
 */
public class GeneralRenderer implements Renderer, Serializable {
    private static final long serialVersionUID = -1940709352630583876L;

    private static final Logger logger = LoggerFactory.getLogger(GeneralRenderer.class);

    private Settings2D settings;

    private PrimitivesDrawer primitivesDrawer;
    private LevelRenderer levelRenderer;
    private EntitiesRenderer entitiesRenderer;
    private WorldHUDRenderer worldHudRenderer;
    private ScreenHudRenderer screenHudRenderer;
    private GuiRenderer guiRenderer;

    @Override
    public void init() {
        primitivesDrawer.init();
        levelRenderer.init();
        entitiesRenderer.init();
        getWorldHudRenderer().init();
        guiRenderer.init();
    }

    @Override
    public void dispose() {
        levelRenderer.dispose();
        entitiesRenderer.dispose();
        getWorldHudRenderer().dispose();
        guiRenderer.dispose();
        primitivesDrawer.dispose();
    }

    public void render(WorldHolder world) {
        MenuHolder menuHolder = world.getMenuHolder();
        try {
            if ((world.getWorld() != null) && !world.getWorld().isEmpty() && !menuHolder.isSolid()) {
                // draw scene
                initScene(world.getPlayerHolder().getCamera());
                drawSceneByCamera(world);

                // draw HUD
                drawHUDByCamera(world);
                initHud();
                drawHUDOnScreen(world);
            }
        } catch (Throwable e) {
            logger.error("Cannot draw scene", e);
        }

        try {
            if (menuHolder.isMenuActive()) {
                // draw GUI
                guiRenderer.drawGUI(menuHolder);
            }
        } catch (Throwable e) {
            logger.error("Cannot draw GUI", e);
        }
    }

    private void drawHUDByCamera(WorldHolder world) {
        PlayerHolder playerHolder = world.getPlayerHolder();
        worldHudRenderer.drawHUD(world.getWorld(), playerHolder, playerHolder.getCamera());
    }

    private void drawHUDOnScreen(WorldHolder world) {
        PlayerHolder playerHolder = world.getPlayerHolder();
        Rectangle2I bounds = playerHolder.getCamera().getHudBounds();
        primitivesDrawer.fillRect(bounds, world.getGraphicsInfo().getBgColor());
        screenHudRenderer.drawHUD(playerHolder, bounds, world.getGraphicsInfo());
    }

    private void drawSceneByCamera(WorldHolder world) {
        SamWorld map = world.getWorld();
        Camera camera = world.getPlayerHolder().getCamera();
        levelRenderer.drawBackground(map.getLevelMap(), camera);
        entitiesRenderer.drawEntities(map.getEntityMap(), camera);
        levelRenderer.drawForeground(map.getLevelMap(), camera);
        entitiesRenderer.drawOverEntities(map.getEntityMap(), camera);
    }

    private void initScene(Camera camera) {
        primitivesDrawer.setDrawPosition(DrawPosition.CENTER);
    }

    private void initHud() {
        primitivesDrawer.setDrawPosition(DrawPosition.UPPER_LEFT);
    }

    public Settings2D getSettings() {
        return settings;
    }

    @Required
    public void setSettings(Settings2D settings) {
        this.settings = settings;
    }

    public LevelRenderer getLevelRenderer() {
        return levelRenderer;
    }

    @Required
    public void setLevelRenderer(LevelRenderer levelRenderer) {
        this.levelRenderer = levelRenderer;
    }

    public EntitiesRenderer getEntitiesRenderer() {
        return entitiesRenderer;
    }

    @Required
    public void setEntitiesRenderer(EntitiesRenderer entitiesRenderer) {
        this.entitiesRenderer = entitiesRenderer;
    }

    public GuiRenderer getGuiRenderer() {
        return guiRenderer;
    }

    @Required
    public void setGuiRenderer(GuiRenderer guiRenderer) {
        this.guiRenderer = guiRenderer;
    }

    public PrimitivesDrawer getPrimitivesDrawer() {
        return primitivesDrawer;
    }

    @Required
    public void setPrimitivesDrawer(PrimitivesDrawer primitivesDrawer) {
        this.primitivesDrawer = primitivesDrawer;
    }

    public WorldHUDRenderer getWorldHudRenderer() {
        return worldHudRenderer;
    }

    @Required
    public void setWorldHudRenderer(WorldHUDRenderer worldHudRenderer) {
        this.worldHudRenderer = worldHudRenderer;
    }

    public ScreenHudRenderer getScreenHudRenderer() {
        return screenHudRenderer;
    }

    @Required
    public void setScreenHudRenderer(ScreenHudRenderer screenHudRenderer) {
        this.screenHudRenderer = screenHudRenderer;
    }

}
