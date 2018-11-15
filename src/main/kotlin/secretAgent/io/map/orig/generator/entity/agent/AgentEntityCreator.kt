package secretAgent.io.map.orig.generator.entity.agent

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.io.map.orig.generator.entity.EntityCreator
import secretAgent.io.map.orig.generator.entity.TypeEntityCreator
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentType

/**
 * Creates agents.
 *
 * @author Ondrej Milenovsky
 */
class AgentEntityCreator : TypeEntityCreator<AgentEntity>() {

    lateinit var playerCreator: EntityCreator<HumanAgent>

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): AgentEntity? {
        val arg0 = args[0]
        if (PLAYER_ARG == arg0) {
            args.removeAt(0)
            return playerCreator.createEntity(args, pos, tileId, model)
        }
        return super.createEntity(args, pos, tileId, model)
    }

    override fun getEnum(arg0: String): Any = AgentType.valueOf(arg0)

    companion object {
        private const val serialVersionUID = -5703564412285324276L
        private const val PLAYER_ARG = "PLAYER"
    }
}