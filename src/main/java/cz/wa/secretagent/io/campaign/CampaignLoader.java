package cz.wa.secretagent.io.campaign;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.FileSettings;
import cz.wa.secretagent.io.campaign.model.ModelFactory;
import cz.wa.secretagent.io.graphicsfiles.GraphicsFilesLoader;
import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.Settings2D;
import cz.wa.secretagent.worldinfo.CampaignInfo;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.secretagent.worldinfo.map.MapInfo;

/**
 * Parses campaign definition file, graphics definitions and loads all textures.
 * Then processes the parsed structures and generates CampaignInfo. 
 * 
 * @author Ondrej Milenovsky
 */
public class CampaignLoader {
    private static final Logger logger = LoggerFactory.getLogger(CampaignLoader.class);

    private File file;
    private final SAMGraphics graphics;
    private final Settings2D settings2d;
    private final FileSettings fileSettings;
    private final ModelFactory modelFactory;

    /**
     * @param inputFile file containing campaign definition
     * @param graphics reference to graphics, used to load textures, if null, then textures are not loaded or linked with models
     */
    public CampaignLoader(File inputFile, SAMGraphics graphics, Settings2D settings2d,
            FileSettings fileSettings, ModelFactory modelFactory) {
        this.file = inputFile;
        this.graphics = graphics;
        this.settings2d = settings2d;
        this.fileSettings = fileSettings;
        this.modelFactory = modelFactory;
    }

    /**
     * Parses the campaign file and all subfiles (except maps), loads the textures and generates CampaignInfo.
     * @return parsed and processed CampaignInfo
     * @throws IOException
     * @throws RuntimeException when there is fatal error in file contents
     */
    public CampaignInfo loadCampaign() throws IOException {
        // general info
        logger.info("Loading campaign: " + file.getAbsolutePath());
        CampaignProperties cp = new CampaignPropertiesParser(file, fileSettings).parse();
        validate(cp);

        // tiles
        Map<Integer, File> tilesFiles = new HashMap<Integer, File>(cp.getAddedTileFiles());
        tilesFiles.put(GraphicsInfo.ORIG_LEVEL_TILES_ID, cp.getLevelTilesFile());

        GraphicsInfo gri = new GraphicsFilesLoader(file, modelFactory, graphics, fileSettings, settings2d)
                .load(tilesFiles, cp.getModelsFiles());
        gri.setBgColor(cp.getBgColor());
        return new CampaignInfo(cp.getTitle(), new MapInfo(cp.getIslandMapFile(), cp.getLevelMapFiles()), gri);
    }

    private void validate(CampaignProperties cp) {
        if (StringUtils.isEmpty(cp.getTitle())) {
            logger.warn("Missing campaign title in: " + file.getAbsolutePath());
        }
        Validate.notNull(cp.getLevelTilesFile(), "Missing level tiles file");
        Validate.notNull(cp.getIslandMapFile(), "Missing island map file");
        Validate.notNull(cp.getLevelMapFiles(), "Missing level map files");
    }

}
