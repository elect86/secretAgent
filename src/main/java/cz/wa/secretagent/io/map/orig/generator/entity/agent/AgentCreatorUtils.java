package cz.wa.secretagent.io.map.orig.generator.entity.agent;

import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.model.AgentTextures;
import secretAgent.view.model.AgentModel;
import secretAgent.world.entity.EntityXDirection;

/**
 * Utils for creating agent entities. 
 * 
 * @author Ondrej Milenovsky
 */
public class AgentCreatorUtils {
    private AgentCreatorUtils() {
    }

    /**
     * Tries to find the tileId in left textures, ignores tileSetId.
     * If the tileId is in left textures, returns LEFT, else RIGHT. 
     */
    public static EntityXDirection getDirection(TileId tileId, AgentModel model) {
        AgentTextures left = model.getLeftTextures();
        if (left != null) {
            for (TileId id : left.getStayIds()) {
                if (id.equals(tileId)) {
                    return EntityXDirection.LEFT;
                }
            }
            for (TileId id : left.getMoveIds()) {
                if (id.equals(tileId)) {
                    return EntityXDirection.LEFT;
                }
            }
            for (TileId id : left.getJumpIds()) {
                if (id.equals(tileId)) {
                    return EntityXDirection.LEFT;
                }
            }
            for (TileId id : left.getDeathIds()) {
                if (id.equals(tileId)) {
                    return EntityXDirection.LEFT;
                }
            }
        }
        return EntityXDirection.RIGHT;
    }
}
