package cz.wa.secretagent.game.starter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.PlayerHolder;
import cz.wa.secretagent.io.SAMIO;
import secretAgent.game.player.PlayerStats;

/**
 * Starts campaign, generates all required classes and loads files.
 * This class is called by main menu when selecting new game.
 * 
 * @author Ondrej Milenovsky
 */
public class CampaignStarter implements Serializable {
    private static final long serialVersionUID = -9220101832719429081L;

    private static final Logger logger = LoggerFactory.getLogger(CampaignStarter.class);

    private SAMIO samIO;
    private PlayerHolder playerHolder;
    private MapStarter mapStarter;
    private PlayerStats initialStats;

    public boolean startCampaign(File campaignFile) {
        try {
            samIO.loadCampaign(campaignFile);
            playerHolder.setPlayerStats(getInitialStats().deepCopy());
            return mapStarter.startIslandMap();
        } catch (IOException e) {
            logger.error("Cannot start campaign " + campaignFile.getAbsolutePath(), e);
            return false;
        }
    }

    public SAMIO getSamIO() {
        return samIO;
    }

    @Required
    public void setSamIO(SAMIO samIO) {
        this.samIO = samIO;
    }

    public MapStarter getMapStarter() {
        return mapStarter;
    }

    @Required
    public void setMapStarter(MapStarter mapStarter) {
        this.mapStarter = mapStarter;
    }

    public PlayerHolder getPlayerHolder() {
        return playerHolder;
    }

    @Required
    public void setPlayerHolder(PlayerHolder playerHolder) {
        this.playerHolder = playerHolder;
    }

    public PlayerStats getInitialStats() {
        return initialStats;
    }

    @Required
    public void setInitialStats(PlayerStats initialStats) {
        this.initialStats = initialStats;
    }

}
