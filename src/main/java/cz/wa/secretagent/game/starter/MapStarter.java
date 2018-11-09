package cz.wa.secretagent.game.starter;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.game.action.ActionFactory;
import cz.wa.secretagent.game.controller.menucreator.MainMenuCreator;
import cz.wa.secretagent.game.player.Camera;
import cz.wa.secretagent.game.player.PlayerStats;
import cz.wa.secretagent.game.projectile.ProjectileFactory;
import cz.wa.secretagent.game.sensor.SensorFactory;
import cz.wa.secretagent.io.SAMIO;
import cz.wa.secretagent.menu.MenuHolder;
import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.world.entity.usable.BuildingUsable;
import cz.wa.secretagent.world.entity.usable.UsableType;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.StoredTile;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.secretagent.world.weapon.Weapon;
import cz.wa.secretagent.world.weapon.WeaponOrder;
import cz.wa.secretagent.worldinfo.WorldHolder;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.wautils.math.Vector2I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.world.SamWorld;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Starts single map. 
 * 
 * @author Ondrej Milenovsky
 */
public class MapStarter implements Serializable {
    private static final long serialVersionUID = 8727551963670481421L;

    private static final Logger logger = LoggerFactory.getLogger(MapStarter.class);

    private SAMIO io;
    private PlayerHolder playerHolder;
    private CameraInfo cameraInfo;
    private ProjectileFactory projectileFactory;
    private MainMenuCreator mainMenuCreator;
    private WeaponOrder weaponOrder;

    public boolean startMainMenu() {
        io.clearMap();
        MenuHolder menuHolder = io.getWorldHolder().getMenuHolder();
        menuHolder.removeAllFrames();

        GFrame mainMenu = mainMenuCreator.getMainMenu();
        mainMenu.setSelectedIndex(0);
        menuHolder.addFrame(mainMenu);
        return true;
    }

    public boolean startIslandMap() {
        File file = io.getCampaignInfo().getMapInfo().getIslandMapFile();
        try {
            // load map
            io.loadIslandMap();
            completeBuildings();
            // find player
            HumanAgent agent = findFirstPlayerAgent();
            if (agent == null) {
                logger.warn("No start position in the map " + file.getAbsolutePath());
                return false;
            }
            // update position
            PlayerStats stats = playerHolder.getPlayerStats();
            if (stats.getIslandPos() == null) {
                stats.setIslandPos(agent.getPos());
            } else {
                agent.setPos(stats.getIslandPos());
            }
            // create and set game classes
            WorldHolder worldHolder = io.getWorldHolder();
            SamWorld world = worldHolder.getWorld();
            Camera camera = cameraInfo.createCamera(agent.getPos());

            SensorFactory sensorFactory = new SensorFactory(agent, world);
            ActionFactory actionFactory = new ActionFactory(agent, world, sensorFactory, this, null);

            playerHolder.getIslandController().init(actionFactory);
            playerHolder.getLevelController().init(null, null);
            playerHolder.setAgent(agent);
            playerHolder.setCamera(camera);

            world.setRunning(true);
            return true;
        } catch (IOException e) {
            logger.error("Cannot start island map " + file.getAbsolutePath(), e);
            return false;
        }
    }

    /**
     * Replace completed buildings by ghosts. Also removes fence when only final building remains.
     */
    private void completeBuildings() {
        EntityMap entityMap = io.getWorldHolder().getWorld().getEntityMap();
        Set<Integer> finishedLevels = playerHolder.getPlayerStats().getFinishedLevels();
        LevelMap levelMap = io.getWorldHolder().getWorld().getLevelMap();
        GraphicsInfo graphicsInfo = io.getWorldHolder().getGraphicsInfo();
        int levelCount = 0;
        boolean finishCounted = false;

        // check all buildings
        for (Entity entity : new ArrayList<Entity>(entityMap.getEntities(EntityType.USABLE))) {
            if (entity.getSecondType() == UsableType.BUILDING) {
                BuildingUsable building = (BuildingUsable) entity;
                if (finishedLevels.contains(building.getLevelId())) {
                    // the entity is finished building, remove it
                    entityMap.removeEntity(building);
                    // create ghost tile
                    ObjectModel model = graphicsInfo.getModel(building.getFinishedModel());
                    Vector2I pos = levelMap.getNearestTilePos(building.getPos());
                    StoredTile tile = new StoredTile(pos, new Tile(TileType.GHOST, model));
                    // add the tile
                    levelMap.addTile(tile);
                    // update type to remove the wall
                    levelMap.updateType(pos);
                }
                // update count
                if (building.isFinalBuilding()) {
                    if (!finishCounted) {
                        finishCounted = true;
                        levelCount++;
                    }
                } else {
                    levelCount++;
                }
            }
        }

        // remove fences
        if (finishedLevels.size() >= levelCount - 1) {
            for (Entity entity : new ArrayList<Entity>(entityMap.getEntities(EntityType.USABLE))) {
                if (entity.getSecondType() == UsableType.FENCE) {
                    entityMap.removeEntity(entity);
                }
            }
        }
    }

    public boolean startLevelMap(int id) {
        List<File> levelMapFiles = io.getCampaignInfo().getMapInfo().getLevelMapFiles();
        if (id - 1 >= levelMapFiles.size()) {
            logger.warn("Missing level " + id + " in campaign definition");
            return false;
        }

        File file = levelMapFiles.get(id - 1);
        // store position on map
        playerHolder.getPlayerStats().setIslandPos(playerHolder.getAgent().getPos());
        try {
            // load map
            io.loadLevelMap(id);
            // find player
            HumanAgent agent = findFirstPlayerAgent();
            if (agent == null) {
                logger.warn("No start position in the map " + file.getAbsolutePath());
                return false;
            }
            // create and set game classes
            Camera camera = cameraInfo.createCamera(agent.getPos());
            SamWorld world = io.getWorldHolder().getWorld();

            SensorFactory sensorFactory = new SensorFactory(agent, world);
            ActionFactory actionFactory = new ActionFactory(agent, world, sensorFactory, this,
                    getProjectileFactory());

            playerHolder.getIslandController().init(null);
            playerHolder.getLevelController().init(actionFactory, sensorFactory);
            playerHolder.setAgent(agent);
            playerHolder.setCamera(camera);
            // select first weapon
            Set<Weapon> agentsWeapons = agent.getInventory().getWeapons();
            for (Weapon weapon : weaponOrder) {
                if (agentsWeapons.contains(weapon)) {
                    agent.setWeapon(weapon);
                    break;
                }
            }

            WorldHolder worldHolder = io.getWorldHolder();
            worldHolder.getWorld().setRunning(true);
            return true;
        } catch (IOException e) {
            logger.error("Cannot start island map " + file.getAbsolutePath(), e);
            return false;
        }
    }

    public SAMIO getIo() {
        return io;
    }

    @Required
    public void setIo(SAMIO io) {
        this.io = io;
    }

    public PlayerHolder getPlayerHolder() {
        return playerHolder;
    }

    @Required
    public void setPlayerHolder(PlayerHolder playerHolder) {
        this.playerHolder = playerHolder;
    }

    public CameraInfo getCameraInfo() {
        return cameraInfo;
    }

    @Required
    public void setCameraInfo(CameraInfo cameraInfo) {
        this.cameraInfo = cameraInfo;
    }

    private HumanAgent findFirstPlayerAgent() {
        EntityMap entityMap = io.getWorldHolder().getWorld().getEntityMap();
        Set<Entity> agents = entityMap.getEntities(EntityType.AGENT);
        for (Entity entity : agents) {
            HumanAgent agent = (HumanAgent) entity;
            if (agent.getTeam().equals(playerHolder.getTeam())) {
                return agent;
            }
        }
        return null;
    }

    /**
     * Exit level map and display summary 
     */
    public void exitLevelMap() {
        // TODO summary screen
        playerHolder.getPlayerStats().getFinishedLevels().add(io.getWorldHolder().getWorld().getLevelId());
        startIslandMap();
    }

    public ProjectileFactory getProjectileFactory() {
        return projectileFactory;
    }

    @Required
    public void setProjectileFactory(ProjectileFactory projectileFactory) {
        this.projectileFactory = projectileFactory;
    }

    public MainMenuCreator getMainMenuCreator() {
        return mainMenuCreator;
    }

    @Required
    public void setMainMenuCreator(MainMenuCreator mainMenuCreator) {
        this.mainMenuCreator = mainMenuCreator;
    }

    public WeaponOrder getWeaponOrder() {
        return weaponOrder;
    }

    @Required
    public void setWeaponOrder(WeaponOrder weaponOrder) {
        this.weaponOrder = weaponOrder;
    }

}
