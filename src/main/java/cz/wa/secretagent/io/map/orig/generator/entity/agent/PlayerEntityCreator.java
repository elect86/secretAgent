package cz.wa.secretagent.io.map.orig.generator.entity.agent;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.world.entity.agent.Team;
import cz.wa.secretagent.world.entity.agent.capabilities.AgentCapabilities;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.game.PlayerHolder;
import secretAgent.game.player.PlayerWeapons;
import secretAgent.view.model.AgentModel;
import secretAgent.view.renderer.TileId;
import secretAgent.world.ObjectModel;
import secretAgent.world.entity.EntityXDirection;

/**
 * Creates player start position. 
 * 
 * @author Ondrej Milenovsky
 */
public class PlayerEntityCreator implements EntityCreator<HumanAgent> {
    private static final long serialVersionUID = 8821866142500203428L;

    private AgentCapabilities capabilities;
    private Rectangle2D sizeBounds;
    private Team team;
    private PlayerHolder playerHolder;

    @Override
    public HumanAgent createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        EntityXDirection dir;
        if (args.isEmpty()) {
            dir = AgentCreatorUtils.getDirection(tileId, (AgentModel) model);
        } else {
            dir = EntityXDirection.valueOf(args.remove(0));
        }
        HumanAgent agent = new HumanAgent(model, pos, team, dir, sizeBounds);
        agent.setCapabilities(capabilities);
        agent.setHealth(capabilities.getMaxHealth());
        PlayerWeapons weapons = playerHolder.getPlayerStats().getWeapons();
        weapons.loadToInventory(agent.getInventory());
        return agent;
    }

    public AgentCapabilities getCapabilities() {
        return capabilities;
    }

    @Required
    public void setCapabilities(AgentCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Rectangle2D getSizeBounds() {
        return sizeBounds;
    }

    @Required
    public void setSizeBounds(Rectangle2D sizeBounds) {
        this.sizeBounds = sizeBounds;
    }

    public Team getTeam() {
        return team;
    }

    @Required
    public void setTeam(Team team) {
        this.team = team;
    }

    public PlayerHolder getPlayerHolder() {
        return playerHolder;
    }

    @Required
    public void setPlayerHolder(PlayerHolder playerHolder) {
        this.playerHolder = playerHolder;
    }

}
