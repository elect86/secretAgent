//package cz.wa.secretagent.io.graphicsfiles;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.secretagent.io.FileSettings;
//
///**
// * Parses campaign info from file.
// *
// * @author Ondrej Milenovsky
// */
//public class GraphicsFilesParser {
//    private static final Logger logger = LoggerFactory.getLogger(GraphicsFilesParser.class);
//
//    private static final String BG_COLOR_KEY = "bgColor";
//    private static final String ADDED_TILES_KEY = "tilesFiles";
//    private static final String MODELS_KEY = "modelsFiles";
//
//    protected final File file;
//    protected final FileSettings fileSettings;
//
//    public GraphicsFilesParser(File file, FileSettings fileSettings) {
//        this.file = file;
//        this.fileSettings = fileSettings;
//    }
//
//    public GraphicsFiles parse() throws IOException {
//        // read from file
//        Properties p = new Properties();
//        p.load(new FileReader(file));
//
//        // parse
//        Color bgColor = getColor(p, BG_COLOR_KEY);
//        Map<Integer, File> tilesFiles = getFileMap(p, ADDED_TILES_KEY, fileSettings.dataDir);
//        List<File> modelsFiles = getFileList(p, MODELS_KEY, fileSettings.dataDir);
//
//        // return
//        return new GraphicsFiles(bgColor, tilesFiles, modelsFiles);
//    }
//
//    public static Color getColor(Properties p, String key) {
//        if (!p.containsKey(key)) {
//            return null;
//        }
//        String str = p.getProperty(key).trim();
//        try {
//            return Color.decode(str);
//        } catch (NumberFormatException e) {
//            logger.warn("Wrong color format: " + str);
//            return Color.BLACK;
//        }
//    }
//
//    public static List<File> getFileList(Properties p, String key, String dataDir) {
//        if (!p.containsKey(key)) {
//            return null;
//        }
//        String str = p.getProperty(key).trim();
//        List<File> ret = new ArrayList<File>();
//        for (String s : str.split(";")) {
//            s = s.trim();
//            if (!s.isEmpty()) {
//                ret.add(new File(dataDir + s));
//            }
//        }
//        return ret;
//    }
//
//    public static Map<Integer, File> getFileMap(Properties p, String key, String dataDir) {
//        if (!p.containsKey(key)) {
//            return null;
//        }
//        String str = p.getProperty(key).trim();
//        Map<Integer, File> ret = new HashMap<Integer, File>();
//        for (String s : str.split(";")) {
//            s = s.trim();
//            if (!s.isEmpty()) {
//                if (!s.contains("*")) {
//                    logger.warn("Tile set " + s + " does not have id, valid example: 6*tiles/image.png");
//                    continue;
//                }
//                int ind = s.indexOf('*');
//                String num = s.substring(0, ind);
//                try {
//                    int id = Integer.parseInt(num);
//                    if (ret.containsKey(id)) {
//                        logger.warn("Duplicate tile set id: " + id);
//                    }
//                    ret.put(id, new File(dataDir + s.substring(ind + 1)));
//                } catch (NumberFormatException e) {
//                    logger.error("Tile set " + s + " has id, that is not a number");
//                }
//            }
//        }
//        return ret;
//    }
//}
