package secretAgent.io.map.orig.generator.entity.agent

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator
import cz.wa.secretagent.io.map.orig.generator.entity.TypeEntityCreator
import cz.wa.secretagent.world.entity.agent.AgentEntity
import cz.wa.secretagent.world.entity.agent.AgentType
import cz.wa.secretagent.world.entity.agent.HumanAgent
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.springframework.beans.factory.annotation.Required
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel

/**
 * Creates agents.
 *
 * @author Ondrej Milenovsky
 */
class AgentEntityCreator : TypeEntityCreator<AgentEntity>() {

    lateinit var playerCreator: EntityCreator<HumanAgent>

    override fun createEntity(args: MutableList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): AgentEntity? {
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