package secretAgent.io.map.orig.generator.entity.agent

import secretAgent.view.model.AgentModel
import secretAgent.view.renderer.TileId
import secretAgent.world.entity.EntityXDirection

/**
 * Utils for creating agent entities.
 *
 * @author Ondrej Milenovsky
 */
object AgentCreatorUtils {

    /**
     * Tries to find the tileId in left textures, ignores tileSetId.
     * If the tileId is in left textures, returns LEFT, else RIGHT.
     */
    fun getDirection(tileId: TileId, model: AgentModel): EntityXDirection {
        model.leftTextures?.let { left ->
            for (id in left.stayIds)
                if (id == tileId)
                    return EntityXDirection.LEFT
            for (id in left.moveIds)
                if (id == tileId)
                    return EntityXDirection.LEFT
            for (id in left.jumpIds)
                if (id == tileId)
                    return EntityXDirection.LEFT
            for (id in left.deathIds)
                if (id == tileId)
                    return EntityXDirection.LEFT
        }
        return EntityXDirection.RIGHT
    }
}