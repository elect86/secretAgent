package cz.wa.secretagent.worldinfo.map;

import java.io.File;
import java.util.List;

/**
 * Info about maps.
 * 
 * @author Ondrej Milenovsky
 */
public class MapInfo {

    private final File islandMapFile;
    private final List<File> levelMapFiles;

    public MapInfo(File islandMapFile, List<File> levelMapFiles) {
        this.islandMapFile = islandMapFile;
        this.levelMapFiles = levelMapFiles;
    }

    public File getIslandMapFile() {
        return islandMapFile;
    }

    public List<File> getLevelMapFiles() {
        return levelMapFiles;
    }

}
