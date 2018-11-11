//package cz.wa.secretagent.io.campaign;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.apache.commons.math3.util.FastMath;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.secretagent.io.FileSettings;
//import cz.wa.secretagent.io.graphicsfiles.GraphicsFilesParser;
//
///**
// * Parses campaign info from file.
// *
// * @author Ondrej Milenovsky
// */
//public class CampaignPropertiesParser {
//    private static final Logger logger = LoggerFactory.getLogger(CampaignPropertiesParser.class);
//
//    private static final String TITLE_KEY = "title";
//    private static final String DESCRIPTION_KEY = "description";
//    private static final String BG_COLOR_KEY = "bgColor";
//    private static final String LEVEL_TILES_KEY = "levelTilesFile";
//    private static final String ADDED_TILES_KEY = "addedTileFiles";
//    private static final String MODELS_KEY = "modelsFiles";
//    private static final String ISLAND_MAP_KEY = "islandMapFile";
//    private static final String LEVEL_MAPS_KEY = "levelMapFiles";
//
//    private final File file;
//    private final FileSettings fileSettings;
//
//    public CampaignPropertiesParser(File file, FileSettings fileSettings) {
//        this.file = file;
//        this.fileSettings = fileSettings;
//    }
//
//    public CampaignProperties parse() throws IOException {
//        // read from file
//        Properties p = new Properties();
//        p.load(new FileReader(file));
//
//        // parse
//        String title = p.getProperty(TITLE_KEY).trim();
//        String description = p.getProperty(DESCRIPTION_KEY).trim();
//        Color bgColor = GraphicsFilesParser.getColor(p, BG_COLOR_KEY);
//        File levelTilesFile = getFile(p, LEVEL_TILES_KEY);
//        Map<Integer, File> addedTileFiles = GraphicsFilesParser.getFileMap(p, ADDED_TILES_KEY,
//                fileSettings.dataDir);
//        List<File> modelsFiles = GraphicsFilesParser.getFileList(p, MODELS_KEY, fileSettings.dataDir);
//        File islandMapFile = getFile(p, ISLAND_MAP_KEY);
//        Map<Integer, File> levelMapFiles = GraphicsFilesParser.getFileMap(p, LEVEL_MAPS_KEY,
//                fileSettings.dataDir);
//
//        List<File> levelListFiles = convertToList(levelMapFiles);
//
//        // return
//        return new CampaignProperties(title, description, bgColor, levelTilesFile, addedTileFiles,
//                modelsFiles, islandMapFile, levelListFiles);
//    }
//
//    private List<File> convertToList(Map<Integer, File> levelMapFiles) {
//        // find max value
//        int max = 0;
//        for (int i : levelMapFiles.keySet()) {
//            max = FastMath.max(max, i);
//        }
//
//        // create list of nulls
//        List<File> ret = new ArrayList<File>(max);
//        for (int i = 0; i < max; i++) {
//            ret.add(null);
//        }
//        // fill the list
//        for (Map.Entry<Integer, File> entry : levelMapFiles.entrySet()) {
//            int i = entry.getKey() - 1;
//            if (ret.get(i) != null) {
//                logger.warn("Duplicate level file: " + i);
//            }
//            ret.set(i, entry.getValue());
//        }
//        //validate
//        for (int i = 0; i < max; i++) {
//            if (ret.get(i) == null) {
//                logger.warn("Missing level file: " + (i + 1));
//            }
//        }
//        return ret;
//    }
//
//    private File getFile(Properties p, String key) {
//        if (!p.containsKey(key)) {
//            logger.warn("Missing " + key);
//            return null;
//        }
//        String fileName = p.getProperty(key).trim();
//        return new File(fileSettings.dataDir + fileName);
//    }
//
//}
