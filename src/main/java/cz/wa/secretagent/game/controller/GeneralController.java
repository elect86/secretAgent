package cz.wa.secretagent.game.controller;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.input.Keyboard;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.simulation.GameController;
import cz.wa.secretagent.utils.lwjgl.FullScreenSwitcher;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * Main game controller holding other controllers. 
 * 
 * @author Ondrej Milenovsky
 */
public class GeneralController implements GameController {

    private static final long serialVersionUID = -1763109597070594041L;

    private WorldHolder worldHolder;

    private GameController levelController;
    private GameController islandController;
    private GameController menuController;

    private FullScreenSwitcher fullScreenSwitcher;

    private GameController activeController;

    private int posCheatValue = 32;

    @Override
    public void activate() {
        // empty
    }

    @Override
    public void processInput(double timeS) {
        if (!processGeneralInput(timeS)) {
            return;
        }
        SAMWorld world = worldHolder.getWorld();
        if (worldHolder.getMenuHolder().isMenuActive()) {
            if (activeController != menuController) {
                menuController.activate();
                activeController = menuController;
            }
            menuController.processInput(timeS);
        } else if (world.isRunning()) {
            processCheats(timeS);
            if (world.isIsland()) {
                if (activeController != islandController) {
                    islandController.activate();
                    activeController = islandController;
                }
                islandController.processInput(timeS);
            } else {
                if (activeController != levelController) {
                    levelController.activate();
                    activeController = levelController;
                }
                levelController.processInput(timeS);
            }
        }
    }

    private boolean processGeneralInput(double timeS) {
        // exit
        if (Keyboard.isKeyDown(Keyboard.KEY_F4)
                && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) {
            System.exit(0);
            return false;
        }
        // full screen
        if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)
                && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) {
            worldHolder.getWorld().setRunning(false);
            fullScreenSwitcher.setFullScreen(!fullScreenSwitcher.isFullScreen());
            worldHolder.getWorld().setRunning(true);
            return false;
        }
        return true;
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

    public GameController getLevelController() {
        return levelController;
    }

    @Required
    public void setLevelController(GameController levelController) {
        this.levelController = levelController;
    }

    public GameController getIslandController() {
        return islandController;
    }

    @Required
    public void setIslandController(GameController islandController) {
        this.islandController = islandController;
    }

    public GameController getMenuController() {
        return menuController;
    }

    @Required
    public void setMenuController(GameController menuController) {
        this.menuController = menuController;
    }

    private void processCheats(double timeS) {
        if (posCheatValue == 0) {
            return;
        }
        // move the agent
        HumanAgent agent = worldHolder.getPlayerHolder().getAgent();
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
            agent.setPos(agent.getPos().add(new Vector2D(-posCheatValue, 0)));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
            agent.setPos(agent.getPos().add(new Vector2D(posCheatValue, 0)));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
            agent.setPos(agent.getPos().add(new Vector2D(0, -posCheatValue)));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
            agent.setPos(agent.getPos().add(new Vector2D(0, posCheatValue)));
        }
    }

    public FullScreenSwitcher getFullScreenSwitcher() {
        return fullScreenSwitcher;
    }

    @Required
    public void setFullScreenSwitcher(FullScreenSwitcher fullScreenSwitcher) {
        this.fullScreenSwitcher = fullScreenSwitcher;
    }

}
