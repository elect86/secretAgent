//package cz.wa.secretagent.io.campaign;
//
//import java.awt.Color;
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//
///**
// * Parsed campaign containing title and file information.
// *
// * @author Ondrej Milenovsky
// */
//public class CampaignProperties {
//    private final String title;
//    private final String description;
//    private final Color bgColor;
//    private final File levelTilesFile;
//    private final Map<Integer, File> addedTileFiles;
//    private final List<File> modelsFiles;
//    private final File islandMapFile;
//    private final List<File> levelMapFiles;
//
//    public CampaignProperties(String title, String description, Color bgColor, File levelTilesFile,
//            Map<Integer, File> addedTileFiles, List<File> modelsFiles, File islandMapFile,
//            List<File> levelMapFiles) {
//        this.title = title;
//        this.description = description;
//        this.bgColor = bgColor;
//        this.levelTilesFile = levelTilesFile;
//        this.modelsFiles = modelsFiles;
//        this.addedTileFiles = addedTileFiles;
//        this.islandMapFile = islandMapFile;
//        this.levelMapFiles = levelMapFiles;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    /**
//     * @return background color, can be null
//     */
//    public Color getBgColor() {
//        return bgColor;
//    }
//
//    public File getLevelTilesFile() {
//        return levelTilesFile;
//    }
//
//    public Map<Integer, File> getAddedTileFiles() {
//        return addedTileFiles;
//    }
//
//    public List<File> getModelsFiles() {
//        return modelsFiles;
//    }
//
//    public File getIslandMapFile() {
//        return islandMapFile;
//    }
//
//    public List<File> getLevelMapFiles() {
//        return levelMapFiles;
//    }
//
//}
