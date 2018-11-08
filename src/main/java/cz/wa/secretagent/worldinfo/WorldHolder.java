package cz.wa.secretagent.worldinfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.menu.MenuHolder;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;

/**
 * Holds world, player stats, graphics, current displayed GUI, ... 
 * 
 * @author Ondrej Milenovsky
 */
public class WorldHolder implements Serializable {

    private static final long serialVersionUID = 5645827950378580677L;

    private MenuHolder menuHolder;
    private PlayerHolder playerHolder;

    private transient SAMWorld world;
    private transient GraphicsInfo graphicsInfo;

    public WorldHolder() {
        init();
    }

    private void init() {
        graphicsInfo = new GraphicsInfo();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    public SAMWorld getWorld() {
        return world;
    }

    public void setWorld(SAMWorld world) {
        this.world = world;
    }

    public MenuHolder getMenuHolder() {
        return menuHolder;
    }

    @Required
    public void setMenuHolder(MenuHolder menuHolder) {
        this.menuHolder = menuHolder;
    }

    public PlayerHolder getPlayerHolder() {
        return playerHolder;
    }

    @Required
    public void setPlayerHolder(PlayerHolder playerHolder) {
        this.playerHolder = playerHolder;
    }

    public GraphicsInfo getGraphicsInfo() {
        return graphicsInfo;
    }

    public void setGraphicsInfo(GraphicsInfo graphicsInfo) {
        this.graphicsInfo = graphicsInfo;
    }

}
