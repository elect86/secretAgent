package cz.wa.secretagent.view.renderer.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.view.Settings2D;
import cz.wa.secretagent.view.model.GLModel;
import cz.wa.secretagent.view.model.HealthBarModel;
import cz.wa.secretagent.view.renderer.DrawPosition;
import cz.wa.secretagent.view.renderer.PrimitivesDrawer;
import cz.wa.secretagent.view.renderer.Renderer;
import cz.wa.secretagent.view.renderer.model.ModelRenderer;
import cz.wa.secretagent.view.texture.DrawBounds;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.world.entity.agent.inventory.AgentInventory;
import cz.wa.secretagent.world.entity.item.ItemType;
import cz.wa.secretagent.world.entity.item.KeyItem;
import cz.wa.secretagent.world.weapon.Weapon;
import cz.wa.secretagent.world.weapon.WeaponOrder;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Rectangle2I;
import cz.wa.wautils.math.Vector2I;
import secretAgent.game.player.Camera;
import secretAgent.world.ObjectModel;

/**
 * Renders health, weapons, keys on the screen 
 * 
 * @author Ondrej Milenovsky
 */
public class ScreenHUDRenderer implements Renderer {
    private static final long serialVersionUID = -7577907590623804209L;

    private static final Logger logger = LoggerFactory.getLogger(ScreenHUDRenderer.class);

    private static final double SIZE = 48;

    private static final String HB_MODEL = "healthBar";
    private static final Vector2D HB_POS = new Vector2D(5.5 / SIZE, 5.5 / SIZE);
    private static final Rectangle2D HB_BOUNDS = new Rectangle2D(-44.0 / SIZE, -5.5 / SIZE, 88.0 / SIZE,
            11.0 / SIZE);

    private static final Vector2D INV_POS = new Vector2D(5.5 / SIZE, 22.0 / SIZE);
    private static final double INV_SCALE = 1 / SIZE;
    private static final double INV_GAP = 16 / SIZE;

    private static final Vector2D WEAP_POS = new Vector2D(142.0 / SIZE, 14 / SIZE);
    private static final double WEAP_GAP = 48.0 / SIZE;
    private static final double WEAP_SCALE = 2.5 / SIZE;
    private static final Color WEAP_MISSING_COLOR = new Color(60, 60, 60, 128);
    private static final Color WEAP_SELECTED_COLOR = new Color(200, 200, 200, 128);
    private static final Rectangle2D WEAP_SELECTED_RECT = new Rectangle2D(-WEAP_GAP / 2, -9.5 / SIZE,
            WEAP_GAP, 38.5 / SIZE);
    private static final Vector2D AMMO_POS = new Vector2D(16 / SIZE, 24 / SIZE);
    private static final Color AMMO_WEAP_COLOR = new Color(190, 190, 190);
    private static final Color AMMO_COLOR = new Color(100, 100, 100);
    private static final double AMMO_SIZE = 16 / SIZE;

    private PrimitivesDrawer primitivesDrawer;
    private ModelRenderer modelRenderer;
    private Settings2D settings2d;
    private WeaponOrder weaponOrder;

    @Override
    public void init() {
        // empty        
    }

    @Override
    public void dispose() {
        // empty        
    }

    public void drawHUD(PlayerHolder player, Rectangle2I bounds, GraphicsInfo graphicsInfo) {
        double size = bounds.getHeight();
        Vector2D addPos = new Vector2D(bounds.getX(), bounds.getY());
        drawHealthBar(player.getAgent(), graphicsInfo, addPos, size);
        drawInventory(player.getAgent(), addPos, size);
        drawWeapons(player.getAgent(), addPos, size);
        // TODO draw power ups
    }

    /**
     * Draw health bar to the upper left corner
     */
    private void drawHealthBar(AgentEntity agent, GraphicsInfo graphicsInfo, Vector2D addPos, double size) {
        HealthBarModel model = (HealthBarModel) graphicsInfo.getModel(HB_MODEL);
        if (model == null) {
            logger.warn("Missing model: " + HB_MODEL);
            return;
        }
        // get quad coords
        DrawBounds drawBounds = new DrawBounds(HB_BOUNDS);
        Vector2D pos = HB_POS.scalarMultiply(size).add(addPos);
        // draw frame
        primitivesDrawer.drawTexture(model.getFrameTexture(), pos, drawBounds, size);

        // modify health quad and tex coords
        TextureToDraw healthTex = model.getHealthTexture();
        double hp = agent.getHealth() / agent.getCapabilities().getMaxHealth();
        if (hp < 1) {
            // tex coords
            Rectangle2D hb = healthTex.getTileBounds();
            healthTex = new TextureToDraw(healthTex.getTexture(),
                    new Rectangle2D(hb.getX(), hb.getY(), hb.getWidth() * hp, hb.getHeight()));
            // quad coords
            double x1 = drawBounds.getX1();
            double x2 = drawBounds.getX2();
            drawBounds = new DrawBounds(x1, drawBounds.getY1(), x1 + hp * (x2 - x1), drawBounds.getY2());
        }
        // draw health
        primitivesDrawer.drawTexture(healthTex, pos, drawBounds, size);
    }

    /**
     * Draw all keys under the health bar
     */
    private void drawInventory(HumanAgent agent, Vector2D addPos, double size) {
        // get all items and group them by lock
        Map<String, List<KeyItem>> items = new LinkedHashMap<String, List<KeyItem>>();
        for (Entity entity : agent.getInventory().getItems(ItemType.KEY)) {
            KeyItem key = (KeyItem) entity;
            String lock = key.getLockType();
            if (!items.containsKey(lock)) {
                items.put(lock, new ArrayList<KeyItem>(4));
            }
            items.get(lock).add(key);
        }

        // draw the items
        Camera camera = new Camera(new Vector2I(settings2d.screenWidth, settings2d.screenHeight),
                INV_SCALE * size);
        double lastX = INV_POS.getX() * size;
        double posY = INV_POS.getY() * size;
        for (List<KeyItem> list : items.values()) {
            for (KeyItem key : list) {
                ObjectModel model = key.getModel();
                if (model instanceof GLModel) {
                    modelRenderer.draw(((GLModel) model), key, new Vector2D(lastX, posY).add(addPos), camera);
                } else {
                    logger.warn("Unknown KeyItem model: " + model.getClass().getSimpleName());
                }
                lastX += INV_GAP * size;
            }
        }
    }

    /**
     * Draw weapons with ammo and selected frame
     */
    private void drawWeapons(HumanAgent agent, Vector2D addPos, double size) {
        Camera camera = new Camera(new Vector2I(settings2d.screenWidth, settings2d.screenHeight),
                WEAP_SCALE * size);
        AgentInventory inventory = agent.getInventory();
        Set<Weapon> weapons = inventory.getWeapons();
        double lastX = WEAP_POS.getX() * size;
        double weapPosY = WEAP_POS.getY() * size;
        double ammoPosY = AMMO_POS.getY() * size;
        for (Weapon weapon : weaponOrder) {
            // selected weapon
            if (agent.getWeapon() == weapon) {
                Rectangle2D rect = WEAP_SELECTED_RECT.multiply(size)
                        .move(new Vector2D(lastX, weapPosY).add(addPos));
                primitivesDrawer.fillRect(new Rectangle2I(rect), WEAP_SELECTED_COLOR);
            }
            // draw weapon
            ObjectModel model = weapon.getModel();
            boolean hasWeapon = weapons.contains(weapon);
            if (model instanceof GLModel) {
                Vector2D pos = new Vector2D(lastX, weapPosY).add(addPos);
                if (!hasWeapon) {
                    primitivesDrawer.setTexColor(WEAP_MISSING_COLOR);
                }
                primitivesDrawer.setDrawPosition(DrawPosition.CENTER);
                modelRenderer.draw((GLModel) model, agent, pos, camera);
                primitivesDrawer.setDrawPosition(DrawPosition.UPPER_LEFT);
            } else {
                logger.warn("Weapon '" + weapon.getName() + "' has unknown model: "
                        + model.getClass().getSimpleName());
            }
            // draw ammo
            Vector2D pos = new Vector2D(lastX + AMMO_POS.getX() * size, ammoPosY).add(addPos);
            Color color;
            if (hasWeapon) {
                color = AMMO_WEAP_COLOR;
            } else {
                color = AMMO_COLOR;
            }
            primitivesDrawer.setDrawPosition(DrawPosition.UPPER_RIGHT);
            primitivesDrawer.drawText(inventory.getAmmo(weapon) + "", pos, AMMO_SIZE * size, color);
            primitivesDrawer.setDrawPosition(DrawPosition.CENTER);

            lastX += WEAP_GAP * size;
        }
    }

    protected static int getFrameNum(long currTimeMs, long durationMs, List<?> textures) {
        currTimeMs = currTimeMs % durationMs;
        return (int) (currTimeMs / (double) durationMs * textures.size());
    }

    public PrimitivesDrawer getPrimitivesDrawer() {
        return primitivesDrawer;
    }

    @Required
    public void setPrimitivesDrawer(PrimitivesDrawer primitivesDrawer) {
        this.primitivesDrawer = primitivesDrawer;
    }

    public ModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    @Required
    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public Settings2D getSettings2d() {
        return settings2d;
    }

    @Required
    public void setSettings2d(Settings2D settings2d) {
        this.settings2d = settings2d;
    }

    public WeaponOrder getWeaponOrder() {
        return weaponOrder;
    }

    @Required
    public void setWeaponOrder(WeaponOrder weaponOrder) {
        this.weaponOrder = weaponOrder;
    }

}
