package cz.wa.secretagent.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.game.projectile.ProjectileFactory;
import cz.wa.secretagent.game.starter.MapStarter;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.LockedEntity;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity;
import cz.wa.secretagent.world.entity.item.ItemEntity;
import cz.wa.secretagent.world.entity.item.ItemType;
import cz.wa.secretagent.world.entity.item.KeyItem;
import cz.wa.secretagent.world.entity.projectile.DynamiteProjectile;
import cz.wa.secretagent.world.entity.usable.BuildingUsable;
import cz.wa.secretagent.world.entity.usable.DoorUsable;
import cz.wa.secretagent.world.entity.usable.TeleportUsable;
import cz.wa.secretagent.world.entity.usable.UsableEntity;
import cz.wa.secretagent.world.entity.usable.UsableType;
import cz.wa.secretagent.world.map.LevelMap;

/**
 * Action performing activating items. Only humans can do that.
 * 
 * @author Ondrej Milenovsky
 */
public class AgentActivateAction extends AgentAction<HumanAgent> {
    private static final Logger logger = LoggerFactory.getLogger(AgentActivateAction.class);

    private static final String DYNAMITE_LOCK = "dynamite";

    private MapStarter mapStarter;
    private ProjectileFactory projectileFactory;
    private boolean activating = true;

    AgentActivateAction() {
    }

    @Override
    protected void init() {
        mapStarter = getActionFactory().getMapStarter();
        projectileFactory = getActionFactory().getProjectileFactory();
    }

    /**
     * Start using the usable
     * @param entity entity to use
     */
    public void useEntity(Entity entity) {
        if (activating) {
            return;
        }
        activating = true;

        if (entity.getEntityType() == EntityType.USABLE) {
            useUsable((UsableEntity) entity);
        } else if (entity.getEntityType() == EntityType.SWITCH) {
            useSwitch((SwitchEntity) entity);
        }
    }

    private void useSwitch(SwitchEntity entity) {
        if (tryUnlock(entity)) {
            entity.activate(getAgent(), getWorld());
        }
    }

    private void useUsable(UsableEntity usable) {
        if (usable.getSecondType() == UsableType.DOOR) {
            DoorUsable door = (DoorUsable) usable;
            if (tryUnlock(door)) {
                // completely remove the door
                getWorld().getEntityMap().removeEntity(door);
                LevelMap levelMap = getWorld().getLevelMap();
                levelMap.updateType(levelMap.getNearestTilePos(door.getPos()));
            }
        } else if (usable.getSecondType() == UsableType.TELEPORT) {
            TeleportUsable teleport = (TeleportUsable) usable;
            if (teleport.getDestination() != null) {
                getAgent().setPos(teleport.getDestination());
            }
        } else if (usable.getSecondType() == UsableType.EXIT) {
            mapStarter.exitLevelMap();
        } else if (usable.getSecondType() == UsableType.EXIT_DOOR) {
            KeyItem dynamite = findDynamiteItem();
            if (dynamite != null) {
                usable.setActive(false);
                useDynamite(dynamite);
            }
        } else if (usable.getSecondType() == UsableType.BUILDING) {
            BuildingUsable building = (BuildingUsable) usable;
            int levelId = building.getLevelId();
            if (levelId > 0) {
                mapStarter.startLevelMap(levelId);
            } else {
                logger.warn("Not linked level for building at " + building.getPos());
            }
        }
    }

    /**
     * Tries to find dynamite in inventory
     * @return dynamite item or null
     */
    private KeyItem findDynamiteItem() {
        for (Entity entity : getAgent().getInventory().getItems(ItemType.KEY)) {
            KeyItem key = (KeyItem) entity;
            if (key.getLockType().equals(DYNAMITE_LOCK)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Use the dynamite (remove from inventory and place)
     */
    private void useDynamite(KeyItem key) {
        getAgent().getInventory().removeItem(key);
        DynamiteProjectile dynamite = projectileFactory.createDynamite(getAgent().getPos());
        getWorld().getEntityMap().addEntity(dynamite);
    }

    /**
     * Stop using, must be called after last use to reload
     */
    public void stopUsing() {
        activating = false;
    }

    /**
     * Tries to unlock the door or switch. If can unlock, removes the key but does not activate the usable.
     * @return true if success
     */
    private boolean tryUnlock(LockedEntity usable) {
        if (usable.isLocked()) {
            KeyItem key = findKey(usable);
            if (key != null) {
                usable.unlock();
                getAgent().getInventory().removeItem(key);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Find key for the usable, may be null
     */
    private KeyItem findKey(LockedEntity usable) {
        for (ItemEntity item : getAgent().getInventory().getItems(ItemType.KEY)) {
            KeyItem key = (KeyItem) item;
            if (key.getLockType().equals(usable.getLockType())) {
                return key;
            }
        }
        return null;
    }

}
