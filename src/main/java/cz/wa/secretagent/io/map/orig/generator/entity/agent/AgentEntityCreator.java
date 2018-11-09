package cz.wa.secretagent.io.map.orig.generator.entity.agent;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.io.map.orig.generator.entity.TypeEntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.agent.AgentType;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import secretAgent.world.ObjectModel;

/**
 * Creates agents.
 * 
 * @author Ondrej Milenovsky
 */
public class AgentEntityCreator extends TypeEntityCreator<AgentEntity> {

    private static final long serialVersionUID = -5703564412285324276L;

    private static final String PLAYER_ARG = "PLAYER";
    private EntityCreator<HumanAgent> playerCreator;

    @Override
    public AgentEntity createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        String arg0 = args.get(0);
        if (PLAYER_ARG.equals(arg0)) {
            args.remove(0);
            return playerCreator.createEntity(args, pos, tileId, model);
        }
        return super.createEntity(args, pos, tileId, model);
    }

    @Override
    protected Object getEnum(String arg0) {
        return AgentType.valueOf(arg0);
    }

    public EntityCreator<HumanAgent> getPlayerCreator() {
        return playerCreator;
    }

    @Required
    public void setPlayerCreator(EntityCreator<HumanAgent> playerCreator) {
        this.playerCreator = playerCreator;
    }

}
