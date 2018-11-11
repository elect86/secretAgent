package cz.wa.secretagent.io;

import cz.wa.secretagent.io.map.orig.MapLoader;
import cz.wa.secretagent.io.map.orig.generator.entity.EntityFactory;
import cz.wa.secretagent.simulation.GameRunner;
import cz.wa.secretagent.world.weapon.Weapon;
import cz.wa.secretagent.worldinfo.CampaignInfo;
import cz.wa.secretagent.worldinfo.WorldHolder;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.wautils.math.Vector2I;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.game.GameSettings;
import secretAgent.io.GraphicsFilesLoader;
import secretAgent.io.campaign.CampaignLoader;
import secretAgent.io.campaign.model.ModelFactory;
import secretAgent.view.SAMGraphics;
import secretAgent.view.Settings2D;
import secretAgent.world.ObjectModel;
import secretAgent.world.SamWorld;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Loads maps, saved games, graphics, ...
 * Saves game.
 *  
 * Does not start the game. When anything was loaded, it must be further processed.
 * 
 * @author Ondrej Milenovsky
 */
public class SAMIO implements Serializable {
    private static final long serialVersionUID = -912252983049381017L;

    private static final Logger logger = LoggerFactory.getLogger(SAMIO.class);

    private WorldHolder worldHolder;
    private SAMGraphics graphics;
    private GameRunner gameRunner;
    private Settings2D settings2d;
    private FileSettings fileSettings;
    private GameSettings gameSettings;
    private ModelFactory modelFactory;
    private EntityFactory entityFactory;
    private Map<String, Weapon> allWeapons;

    private transient CampaignInfo campaignInfo;

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

    public SAMGraphics getGraphics() {
        return graphics;
    }

    @Required
    public void setGraphics(SAMGraphics graphics) {
        this.graphics = graphics;
    }

    public Settings2D getSettings2d() {
        return settings2d;
    }

    public GameRunner getGameRunner() {
        return gameRunner;
    }

    @Required
    public void setGameRunner(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    @Required
    public void setSettings2d(Settings2D settings2d) {
        this.settings2d = settings2d;
    }

    public FileSettings getFileSettings() {
        return fileSettings;
    }

    @Required
    public void setFileSettings(FileSettings fileSettings) {
        this.fileSettings = fileSettings;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    @Required
    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    @Required
    public void setModelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    @Required
    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    /**
     * Loads campaign from file.
     * @param file
     * @throws IOException
     */
    public void loadCampaign(File file) throws IOException {
        campaignInfo = new CampaignLoader(file, graphics, getSettings2d(), getFileSettings(), modelFactory)
                .loadCampaign();
        worldHolder.getGraphicsInfo().update(campaignInfo.getGraphicsInfo());
        campaignInfo.clearGraphicsInfo();
        for (Weapon weapon : allWeapons.values()) {
            weapon.linkModel(worldHolder.getGraphicsInfo());
            if (!weapon.hasLinkedModel()) {
                logger.warn("Missing model '" + weapon.getModelName() + "' for weapon: " + weapon.getName());
            }
        }
        // check that every additional model has linked textures
        for (Map.Entry<String, ObjectModel> entry : worldHolder.getGraphicsInfo().getModels().entrySet()) {
            if (!entry.getValue().hasLinkedTextures()) {
                logger.warn("Missing textures for model: " + entry.getKey());
            }
        }
    }

    /**
     * Loads default graphics settings
     * @throws IOException
     */
    public void loadDefaultGraphics() throws IOException {
        File file = new File(fileSettings.dataDir + fileSettings.defaultGraphicsFile);
        GraphicsInfo gri = new GraphicsFilesLoader(file, modelFactory, graphics, fileSettings, settings2d)
                .load();
        worldHolder.setGraphicsInfo(gri);
    }

    /**
     * Loads player's saved progress from file.
     * @param file
     * @throws IOException
     */
    public void loadPlayer(File file) throws IOException {
        // TODO load player
    }

    /**
     * Saves player's progress to file.
     * @param file
     * @throws IOException
     */
    public void savePlayer(File file) throws IOException {
        // TODO save player
    }

    /**
     * Ends the game
     */
    public void clearMap() {
        worldHolder.setWorld(createEmptyWorld());
    }

    private SamWorld createEmptyWorld() {
        SamWorld world = new SamWorld(Vector2I.ZERO, null, -1);
        return world;
    }

    /**
     * Loads single map by level id.
     * @param levelId
     * @throws IOException
     */
    public void loadLevelMap(int levelId) throws IOException {
        List<File> levels = campaignInfo.getMapInfo().getLevelMapFiles();
        Validate.isTrue((levelId >= 1) && (levelId <= levels.size()),
                "Level id must be 1.." + levels.size() + ", but is " + levelId);
        loadLevel(levels.get(levelId - 1), levelId);
    }

    /**
     * Loads island map.
     * @throws IOException
     */
    public void loadIslandMap() throws IOException {
        loadLevel(campaignInfo.getMapInfo().getIslandMapFile(), 0);
    }

    private void loadLevel(File file, int levelId) throws IOException {
        SamWorld world;
        String fileExt = FilenameUtils.getExtension(file.getName());
        if (fileExt.equalsIgnoreCase(fileSettings.mapOrigExt)) {
            world = new MapLoader(file, levelId, worldHolder.getGraphicsInfo(), graphics, entityFactory)
                    .loadMap();
        } else {
            throw new UnsupportedOperationException("TODO load different map type");
        }
        worldHolder.setWorld(world);
    }

    public CampaignInfo getCampaignInfo() {
        return campaignInfo;
    }

    public void dispose() {
        graphics.dispose();
        graphics = null;
        campaignInfo = null;
        worldHolder = null;
        setSettings2d(null);
    }

    public Map<String, Weapon> getAllWeapons() {
        return allWeapons;
    }

    @Required
    public void setAllWeapons(Map<String, Weapon> allWeapons) {
        this.allWeapons = allWeapons;
    }

}
