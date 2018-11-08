package cz.wa.secretagent.game.controller;

import org.lwjgl.input.Keyboard;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.menu.MenuKeys;
import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.menu.window.component.selectable.GSelectable;
import cz.wa.secretagent.simulation.GameController;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * Controls all menus. (move cursor, enter, back)
 * 
 * @author Ondrej Milenovsky
 */
public class GameMenuController implements GameController {

    private static final long serialVersionUID = -8439774283488072370L;

    private WorldHolder worldHolder;
    private transient boolean canActivate = false;

    @Override
    public void activate() {
        canActivate = false;
    }

    @Override
    public void processInput(double timeS) {
        MenuKeys menuKeys = worldHolder.getMenuHolder().getKeys();

        if (Keyboard.isKeyDown(menuKeys.kBack)) {
            if (canActivate) {
                GFrame frame = worldHolder.getMenuHolder().getTopFrame();
                frame.callClose();
                canActivate = false;
            }
        } else if (Keyboard.isKeyDown(menuKeys.kUp)) {
            if (canActivate) {
                GFrame frame = worldHolder.getMenuHolder().getTopFrame();
                frame.selectPrevComponent();
                canActivate = false;
            }
        } else if (Keyboard.isKeyDown(menuKeys.kDown)) {
            if (canActivate) {
                GFrame frame = worldHolder.getMenuHolder().getTopFrame();
                frame.selectNextComponent();
                canActivate = false;
            }
        } else if (Keyboard.isKeyDown(menuKeys.kEnter)) {
            if (canActivate) {
                GFrame frame = worldHolder.getMenuHolder().getTopFrame();
                GSelectable selected = frame.getSelectedComponent();
                if (selected != null) {
                    selected.activate();
                }
                canActivate = false;
            }
        } else if (Keyboard.isKeyDown(menuKeys.kLeft)) {
            if (canActivate) {
                GFrame frame = worldHolder.getMenuHolder().getTopFrame();
                GSelectable selected = frame.getSelectedComponent();
                if ((selected != null) && !selected.changeLeft()) {
                    frame.selectPrevComponent();
                }
                canActivate = false;
            }
        } else if (Keyboard.isKeyDown(menuKeys.kRight)) {
            if (canActivate) {
                GFrame frame = worldHolder.getMenuHolder().getTopFrame();
                GSelectable selected = frame.getSelectedComponent();
                if ((selected != null) && !selected.changeRight()) {
                    frame.selectNextComponent();
                }
                canActivate = false;
            }
        } else {
            canActivate = true;
        }
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

}
