package cz.wa.secretagent.game.controller;

import cz.wa.secretagent.game.PlayerKeys;
import cz.wa.secretagent.game.action.ActionFactory;
import cz.wa.secretagent.game.action.AgentLevelAction;
import cz.wa.secretagent.game.controller.menucreator.GameMenuCreator;
import cz.wa.secretagent.game.sensor.AgentWeaponSensor;
import cz.wa.secretagent.game.sensor.SensorFactory;
import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.world.weapon.Weapon;
import cz.wa.secretagent.world.weapon.WeaponOrder;
import cz.wa.secretagent.worldinfo.WorldHolder;
import org.apache.commons.lang.Validate;
import org.lwjgl.input.Keyboard;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.world.entity.EntityXDirection;
import secretAgent.world.entity.EntityYDirection;

import java.util.Set;

/**
 * Controller that works if the player is in level map. (moving, jumping, shooting) 
 * 
 * @author Ondrej Milenovsky
 */
public class GameLevelController extends AbstractPlayerController {

    private static final long serialVersionUID = -8521706793940602063L;

    private WeaponOrder weaponOrder;
    private WorldHolder worldHolder;
    private GameMenuCreator gameMenuCreator;

    private transient AgentLevelAction action;
    private transient AgentWeaponSensor weaponSensor;

    private transient boolean canFire = false;
    private transient boolean canSwitch = false;
    private transient boolean canMenu = false;

    private transient GFrame menuFrame;

    public void init(ActionFactory actionFactory, SensorFactory sensorFactory) {
        Validate.isTrue((actionFactory == null) == (sensorFactory == null),
                "actionFactory and sensorFactory must be both set or null");
        if (actionFactory == null) {
            action = null;
            weaponSensor = null;
        } else {
            this.action = actionFactory.getAction(AgentLevelAction.class);
            this.weaponSensor = sensorFactory.getSensor(AgentWeaponSensor.class);
        }
        if (menuFrame == null) {
            menuFrame = gameMenuCreator.getLevelMainMenu();
        }
    }

    @Override
    public void activate() {
        canFire = false;
        canSwitch = false;
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
            action.aimY(EntityYDirection.UP);
        } else if (Keyboard.isKeyDown(keys.kDown)) {
            action.aimY(EntityYDirection.DOWN);
        } else {
            action.aimY(EntityYDirection.NONE);
        }

        if (Keyboard.isKeyDown(keys.kJump)) {
            action.jump(true);
        } else {
            action.jump(false);
        }

        // activate
        if (Keyboard.isKeyDown(keys.kActivate)) {
            action.activate(true);
        } else {
            action.activate(false);
        }

        if (action == null) {
            // exited level
            return;
        }

        // fire
        if (Keyboard.isKeyDown(keys.kFire)) {
            if (canFire) {
                action.fire(true);
                return;
            }
        } else {
            action.fire(false);
            canFire = true;
        }

        // weapons
        if (Keyboard.isKeyDown(keys.kNoWeap)) {
            if (canSwitch) {
                action.switchWeapon(null);
                canSwitch = false;
                canFire = false;
            }
        } else if (Keyboard.isKeyDown(keys.kWeap1)) {
            switchWeapon(1);
        } else if (Keyboard.isKeyDown(keys.kWeap2)) {
            switchWeapon(2);
        } else if (Keyboard.isKeyDown(keys.kWeap3)) {
            switchWeapon(3);
        } else if (Keyboard.isKeyDown(keys.kWeap4)) {
            switchWeapon(4);
        } else if (Keyboard.isKeyDown(keys.kWeap5)) {
            switchWeapon(5);
        } else if (Keyboard.isKeyDown(keys.kWeap6)) {
            switchWeapon(6);
        } else if (Keyboard.isKeyDown(keys.kWeap7)) {
            switchWeapon(7);
        } else if (Keyboard.isKeyDown(keys.kWeap8)) {
            switchWeapon(8);
        } else if (Keyboard.isKeyDown(keys.kWeap9)) {
            switchWeapon(9);
        } else if (Keyboard.isKeyDown(keys.kWeap10)) {
            switchWeapon(10);
        } else if (Keyboard.isKeyDown(keys.kWeapLeft)) {
            switchNextWapon(-1);
        } else if (Keyboard.isKeyDown(keys.kWeapRight)) {
            switchNextWapon(1);
        } else if (Keyboard.isKeyDown(keys.kDropWeap)) {
            action.dropWeapon();
        } else {
            canSwitch = true;
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

    private void switchNextWapon(int dir) {
        if (!canSwitch) {
            return;
        }
        canSwitch = false;
        canFire = false;
        Weapon w = weaponSensor.getActiveWeapon();
        int ind = -dir;
        if (w != null) {
            ind = weaponOrder.indexOf(w);
        }
        Set<Weapon> weapons = weaponSensor.getWeapons();
        int wCount = weaponOrder.size();
        for (int i = 0; i < wCount; i++) {
            ind += dir;
            if (ind < 0) {
                ind = wCount - 1;
            } else if (ind >= wCount) {
                ind = 0;
            }
            w = weaponOrder.get(ind);
            if (weapons.contains(w)) {
                action.switchWeapon(w);
                return;
            }
        }
    }

    private void switchWeapon(int i) {
        if (canSwitch) {
            if (i - 1 < weaponOrder.size()) {
                Weapon w = weaponOrder.get(i - 1);
                action.switchWeapon(w);
            }
            canSwitch = false;
            canFire = false;
        }
    }

    public WeaponOrder getWeaponOrder() {
        return weaponOrder;
    }

    @Required
    public void setWeaponOrder(WeaponOrder weaponOrder) {
        this.weaponOrder = weaponOrder;
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
