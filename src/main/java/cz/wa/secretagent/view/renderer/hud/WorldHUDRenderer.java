package cz.wa.secretagent.view.renderer.hud;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.game.player.Camera;
import cz.wa.secretagent.view.renderer.PrimitivesDrawer;
import cz.wa.secretagent.view.renderer.Renderer;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.agent.HumanAgent;

/**
 * Renders game world HUD (text, symbols) into the map. 
 * 
 * @author Ondrej Milenovsky
 */
public class WorldHUDRenderer implements Renderer {

    private static final long serialVersionUID = -6980609084106794684L;

    private PrimitivesDrawer primitivesDrawer;
    private double textSize;

    @Override
    public void init() {
        // empty
    }

    @Override
    public void dispose() {
        // empty
    }

    public void drawHUD(SAMWorld world, PlayerHolder player, Camera camera) {
        // TODO draw thread symbols
        drawDisplayedText(player.getAgent(), player.getDisplayedText(), camera);
    }

    private void drawDisplayedText(HumanAgent agent, String text, Camera camera) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        Vector2D pos = camera
                .getScreenPos(agent.getPos().add(new Vector2D(0, -agent.getSizeBounds().getHeight())));
        primitivesDrawer.drawText(text, pos, textSize * camera.getScale(), Color.WHITE);
    }

    public PrimitivesDrawer getPrimitivesDrawer() {
        return primitivesDrawer;
    }

    @Required
    public void setPrimitivesDrawer(PrimitivesDrawer primitivesDrawer) {
        this.primitivesDrawer = primitivesDrawer;
    }

    public double getTextSize() {
        return textSize;
    }

    @Required
    public void setTextSize(double textSize) {
        this.textSize = textSize;
    }

}
