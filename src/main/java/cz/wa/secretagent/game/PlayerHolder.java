//package cz.wa.secretagent.game;
//
//import java.io.Serializable;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.entity.agent.Team;
//import secretAgent.game.controller.GameIslandController;
//import secretAgent.game.controller.GameLevelController;
//import secretAgent.game.player.Camera;
//import secretAgent.game.player.PlayerStats;
//
///**
// * Holds classes for player.
// *
// * @author Ondrej Milenovsky
// */
//public class PlayerHolder implements Serializable {
//    private static final long serialVersionUID = -6134555295498514501L;
//
//    private transient Camera camera;
//    private transient HumanAgent agent;
//    private transient PlayerKeys keys;
//    private transient PlayerStats playerStats;
//    private transient String displayedText;
//
//    private Team team;
//    private GameIslandController islandController;
//    private GameLevelController levelController;
//
//    public Camera getCamera() {
//        return camera;
//    }
//
//    public void setCamera(Camera camera) {
//        this.camera = camera;
//    }
//
//    public HumanAgent getAgent() {
//        return agent;
//    }
//
//    public void setAgent(HumanAgent agent) {
//        this.agent = agent;
//    }
//
//    public PlayerKeys getKeys() {
//        return keys;
//    }
//
//    public void setKeys(PlayerKeys keys) {
//        this.keys = keys;
//    }
//
//    public PlayerStats getPlayerStats() {
//        return playerStats;
//    }
//
//    public void setPlayerStats(PlayerStats playerStats) {
//        this.playerStats = playerStats;
//    }
//
//    public Team getTeam() {
//        return team;
//    }
//
//    @Required
//    public void setTeam(Team team) {
//        this.team = team;
//    }
//
//    public GameIslandController getIslandController() {
//        return islandController;
//    }
//
//    @Required
//    public void setIslandController(GameIslandController islandController) {
//        this.islandController = islandController;
//    }
//
//    public GameLevelController getLevelController() {
//        return levelController;
//    }
//
//    @Required
//    public void setLevelController(GameLevelController levelController) {
//        this.levelController = levelController;
//    }
//
//    public String getDisplayedText() {
//        return displayedText;
//    }
//
//    public void setDisplayedText(String displayedText) {
//        this.displayedText = displayedText;
//    }
//
//}
