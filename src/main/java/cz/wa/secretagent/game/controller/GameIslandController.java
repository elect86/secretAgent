package cz.wa.secretagent.game.controller;

import org.lwjgl.input.Keyboard;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.PlayerKeys;
import cz.wa.secretagent.game.action.ActionFactory;
import cz.wa.secretagent.game.action.AgentIslandAction;
import cz.wa.secretagent.game.controller.menucreator.GameMenuCreator;
import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.world.entity.direction.EntityXDirection;
import cz.wa.secretagent.world.entity.direction.EntityYDirection;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * Controller that works when the player is moving on island map. (moving, entering)
 * 
 * @author Ondrej Milenovsky
 */
public class GameIslandController extends AbstractPlayerController {

    private static final long serialVersionUID = 943816424295202253L;

    private WorldHolder worldHolder;
    private GameMenuCreator gameMenuCreator;

    private transient AgentIslandAction action;

    private transient GFrame menuFrame;
    private transient boolean canActivate = false;
    private transient boolean canMenu = false;

    public void init(ActionFactory actionFactory) {
        if (actionFactory == null) {
            action = null;
        } else {
            this.action = actionFactory.getAction(AgentIslandAction.class);
            if (menuFrame == null) {
                menuFrame = gameMenuCreator.getIslandMainMenu();
            }
        }
    }

    @Override
    public void activate() {
        canActivate = false;
        canMenu = false;
    }

    @Override
    public void processInput(double timeS) {
        if (action == null) {
            return;
        }

        PlayerKeys keys = playerHolder.getKeys();

        if (Keyboard.isKeyDown(keys.kLeft)) {
            action.moveX(EntityXDirection.LEFT);
        } else if (Keyboard.isKeyDown(keys.kRight)) {
            action.moveX(EntityXDirection.RIGHT);
        } else {
            action.moveX(EntityXDirection.NONE);
        }

        if (Keyboard.isKeyDown(keys.kUp)) {
            action.moveY(EntityYDirection.UP);
        } else if (Keyboard.isKeyDown(keys.kDown)) {
            action.moveY(EntityYDirection.DOWN);
        } else {
            action.moveY(EntityYDirection.NONE);
        }

        if (Keyboard.isKeyDown(keys.kActivate)) {
            if (canActivate) {
                action.activate(true);
                canActivate = false;
            }
        } else {
            action.activate(false);
            canActivate = true;
        }

        if (Keyboard.isKeyDown(keys.kMenu)) {
            if (canMenu) {
                worldHolder.getWorld().setRunning(false);
                menuFrame.setSelectedIndex(0);
                worldHolder.getMenuHolder().addFrame(menuFrame);
                canMenu = false;
            }
        } else {
            canMenu = true;
        }
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

    public GameMenuCreator getGameMenuCreator() {
        return gameMenuCreator;
    }

    @Required
    public void setGameMenuCreator(GameMenuCreator gameMenuCreator) {
        this.gameMenuCreator = gameMenuCreator;
    }

}
