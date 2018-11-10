package cz.wa.secretagent.io.graphicsfiles;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.FileSettings;
import cz.wa.secretagent.io.campaign.model.ModelFactory;
import cz.wa.secretagent.io.tiles.GLTileSetLoader;
import cz.wa.secretagent.io.tiles.ModelsLoader;
import cz.wa.secretagent.io.tiles.TilesPropertiesLoader;
import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.Settings2D;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.secretagent.worldinfo.graphics.TilesInfo;
import cz.wa.wautils.math.Vector2I;
import secretAgent.view.renderer.GLGraphics;
import secretAgent.view.renderer.GLTileSet;
import secretAgent.world.ObjectModel;

/**
 * Parses graphics files definition and creates graphics info. 
 * 
 * @author Ondrej Milenovsky
 */
public class GraphicsFilesLoader {
    private static final Logger logger = LoggerFactory.getLogger(GraphicsFilesLoader.class);

    private final File file;
    private final ModelFactory modelFactory;
    private final SAMGraphics graphics;
    private final FileSettings fileSettings;
    private final Settings2D settings2d;

    public GraphicsFilesLoader(File file, ModelFactory modelFactory, SAMGraphics graphics,
            FileSettings fileSettings, Settings2D settings2d) {
        this.file = file;
        this.modelFactory = modelFactory;
        this.graphics = graphics;
        this.fileSettings = fileSettings;
        this.settings2d = settings2d;
    }

    public GraphicsInfo load() throws IOException {
        logger.info("Loading default graphics: " + file.getAbsolutePath());
        // parse the properties file
        GraphicsFiles gf = new GraphicsFilesParser(file, fileSettings).parse();
        // load all other files
        GraphicsInfo gri = load(gf.getTilesFiles(), gf.getModelsFiles());
        gri.setBgColor(gf.getBgColor());
        return gri;
    }

    public GraphicsInfo load(Map<Integer, File> tilesFiles, List<File> modelsFiles) {
        // load all the files
        GraphicsInfo gri = loadGraphicsInfo(tilesFiles, modelsFiles, modelFactory, fileSettings.tilesDefExt);
        // load and link textures
        if (graphics != null) {
            loadTextures(gri, tilesFiles);
            gri.linkTexturesToModels(graphics);
        }
        return gri;
    }

    /**
     * Loads and uses all new textures.
     */
    private void loadTextures(GraphicsInfo gri, Map<Integer, File> tileFiles) {
        for (Map.Entry<Integer, File> entry : tileFiles.entrySet()) {
            Vector2I tileSize = gri.getTileSets().get(entry.getKey()).getTileSize();
            File texFile = entry.getValue();
            try {
                GLTileSet tileSet = new GLTileSetLoader(texFile).load(tileSize, settings2d.texFilter);
                if (graphics instanceof GLGraphics) {
                    GLGraphics gr = (GLGraphics) graphics;
                    gr.setTileSet(entry.getKey(), tileSet);
                } else {
                    throw new UnsupportedOperationException("Unknown graphics, refactoring needed");
                }
            } catch (IOException e) {
                logger.error("Cannot load textures: " + entry.getValue().getAbsolutePath(), e);
            }
        }
    }

    /**
     * Parses info for all tile sets and generates the GraphicsInfo. The input files are images.
     */
    private GraphicsInfo loadGraphicsInfo(Map<Integer, File> tileFiles, List<File> modelFiles,
            ModelFactory modelFactory, String tilesDefExt) {
        // tile sets
        Map<Integer, TilesInfo> tileSets = new HashMap<Integer, TilesInfo>(tileFiles.size());
        for (Map.Entry<Integer, File> entry : tileFiles.entrySet()) {
            try {
                TilesInfo setInfo = loadTileSetInfo(entry.getKey(), entry.getValue(), modelFactory,
                        tilesDefExt);
                tileSets.put(entry.getKey(), setInfo);
            } catch (IOException e) {
                logger.error("Cannot load tiles info: " + entry.getValue().getAbsolutePath(), e);
            }
        }
        // models
        Map<String, ObjectModel> models = loadModels(modelFiles, modelFactory);
        return new GraphicsInfo(tileSets, models);
    }

    /**
     * Loads all models from the files.
     */
    private Map<String, ObjectModel> loadModels(List<File> modelFiles, ModelFactory modelFactory) {
        Map<String, ObjectModel> ret = new HashMap<String, ObjectModel>();
        for (File file : modelFiles) {
            try {
                Map<String, ObjectModel> models = new ModelsLoader(file, modelFactory).loadModels();
                ret.putAll(models);
            } catch (IOException e) {
                logger.error("Cannot load models: " + file.getAbsolutePath(), e);
            }
        }
        return ret;
    }

    private TilesInfo loadTileSetInfo(int tileSetId, File tilesFile, ModelFactory modelFactory,
            String tilesDefExt) throws IOException {
        Validate.notNull(tilesFile, "No tiles file for id " + tileSetId);
        String infoFileName = FilenameUtils.removeExtension(tilesFile.getPath()) + "." + tilesDefExt;
        File infoFile = new File(infoFileName);

        return new TilesPropertiesLoader(infoFile, tileSetId, modelFactory).loadInfo();
    }

}
