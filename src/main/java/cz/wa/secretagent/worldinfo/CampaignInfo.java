package cz.wa.secretagent.worldinfo;

import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.secretagent.worldinfo.map.MapInfo;

/**
 * Parsed and processed info about the campaign. 
 * 
 * @author Ondrej Milenovsky
 */
public class CampaignInfo {
    private final String title;
    private final MapInfo mapInfo;
    private GraphicsInfo graphicsInfo;

    public CampaignInfo(String title, MapInfo mapInfo, GraphicsInfo graphicsInfo) {
        this.title = title;
        this.mapInfo = mapInfo;
        this.graphicsInfo = graphicsInfo;
    }

    public String getTitle() {
        return title;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    public GraphicsInfo getGraphicsInfo() {
        return graphicsInfo;
    }

    public void clearGraphicsInfo() {
        graphicsInfo = null;
    }
}
