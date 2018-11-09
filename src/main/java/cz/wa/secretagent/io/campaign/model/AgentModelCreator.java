package cz.wa.secretagent.io.campaign.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
import secretAgent.view.model.AgentModel;
import secretAgent.view.model.AgentTextures;
import secretAgent.world.GLModel;

/**
 * Creates AgentModel. 
 * 
 * @author Ondrej Milenovsky
 */
public class AgentModelCreator extends AbstractModelCreator {
    private static final long serialVersionUID = -7333931381388152107L;

    private static final Logger logger = LoggerFactory.getLogger(AgentModelCreator.class);

    private static final Vector2D DEFAULT_WEAPON_CENTER = new Vector2D(0, 2.5);
    private static final long DEFAULT_DURATION_MS = 1000;

    private static final String STAY_DURATION = "stayDuration";
    private static final String STAY_LEFT = "stayLeft";
    private static final String STAY_RIGHT = "stayRight";
    private static final String MOVE_LEFT = "moveLeft";
    private static final String MOVE_RIGHT = "moveRight";
    private static final String JUMP_LEFT = "jumpLeft";
    private static final String JUMP_RIGHT = "jumpRight";
    private static final String DEATH_LEFT = "deathLeft";
    private static final String DEATH_RIGHT = "deathRight";

    @Override
    public AgentModel createModel(ModelProperties modelProperties, TileId tileId, String modelName) {
        Map<String, List<TileId>> properties = modelProperties.getProperties();

        // stay duration
        long stayDuration = DEFAULT_DURATION_MS;
        List<TileId> durationList = properties.get(STAY_DURATION);
        if (durationList != null) {
            if (durationList.size() != 1) {
                logger.warn("Agent model '" + modelName + "'." + STAY_DURATION
                        + " must have 1 value, but has " + durationList.size());
            }
            if (!durationList.isEmpty()) {
                stayDuration = durationList.get(0).getTileId();
            }
        }

        // textures
        List<TileId> stayLeft = properties.get(STAY_LEFT);
        List<TileId> stayRight = properties.get(STAY_RIGHT);
        List<TileId> moveLeft = properties.get(MOVE_LEFT);
        List<TileId> moveRight = properties.get(MOVE_RIGHT);
        List<TileId> jumpLeft = properties.get(JUMP_LEFT);
        List<TileId> jumpRight = properties.get(JUMP_RIGHT);
        List<TileId> deathLeft = properties.get(DEATH_LEFT);
        List<TileId> deathRight = properties.get(DEATH_RIGHT);

        AgentTextures[] textures = createTextures(stayLeft, stayRight, moveLeft, moveRight, jumpLeft,
                jumpRight, deathLeft, deathRight, modelName);

        // TODO weapon center
        // scale
        double scale = 0;
        TileId scaleTile = getSingleTileId(properties, SCALE_PROPERTY, modelName);
        if (scaleTile != null) {
            scale = tileToDouble(scaleTile);
        }
        return new AgentModel(textures[0], textures[1], scale, stayDuration, DEFAULT_WEAPON_CENTER);
    }

    private AgentTextures[] createTextures(List<TileId> stayLeft, List<TileId> stayRight,
            List<TileId> moveLeft, List<TileId> moveRight, List<TileId> jumpLeft, List<TileId> jumpRight,
            List<TileId> deathLeft, List<TileId> deathRight, String modelName) {

        AgentTextures[] ret = new AgentTextures[2];
        boolean missingLeft = (stayLeft == null) || (moveLeft == null) || (jumpLeft == null)
                || (deathLeft == null);
        boolean missingRight = (stayRight == null) || (moveRight == null) || (jumpRight == null)
                || (deathRight == null);

        if (missingLeft && missingRight) {
            logger.warn("Agent model '" + modelName
                    + "' does not have complete at least one direction of textures.");
            return null;
        }

        // create left
        if (!missingLeft) {
            ret[0] = new AgentTextures(stayLeft, moveLeft, jumpLeft, deathLeft);
            if (missingRight && ((stayRight != null) || (moveRight != null) || (jumpRight != null)
                    || (deathRight != null))) {
                logger.warn("Agent model '" + modelName
                        + "' has incomplete right direction, ignoring all defined right textures");
            }
        }
        //create right
        if (!missingRight) {
            ret[1] = new AgentTextures(stayRight, moveRight, jumpRight, deathRight);
            if (missingLeft && ((stayLeft != null) || (moveLeft != null) || (jumpLeft != null)
                    || (deathLeft != null))) {
                logger.warn("Agent model '" + modelName
                        + "' has incomplete left direction, ignoring all defined left textures");
            }
        }

        return ret;
    }

    @Override
    public ModelInfo createModelInfo(TileId tileId, GLModel model, String modelName) {
        return new ModelInfo(model);
    }

}
