package cz.wa.secretagent.game.simulator;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.simulation.GameSimulator;
import cz.wa.secretagent.worldinfo.WorldHolder;
import secretAgent.game.player.Camera;

/**
 * Keeps the player in center of screen but does not let the camera see outside map.
 * 
 * @author Ondrej Milenovsky
 */
public class SimpleCameraSimulator implements GameSimulator {
    private static final long serialVersionUID = -3960824364903377922L;

    private PlayerHolder playerHolder;
    private WorldHolder worldHolder;

    @Override
    public boolean move(double timeS) {
        Camera camera = playerHolder.getCamera();
        camera.setPos(playerHolder.getAgent().getPos());
        camera.limitInMap(worldHolder.getWorld().getBounds());
        return true;
    }

    public PlayerHolder getPlayerHolder() {
        return playerHolder;
    }

    @Required
    public void setPlayerHolder(PlayerHolder playerHolder) {
        this.playerHolder = playerHolder;
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }
}
