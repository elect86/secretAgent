package secretAgent.io.map.orig.generator.entity.agent

import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.game.PlayerHolder
import secretAgent.io.map.orig.generator.entity.EntityCreator
import secretAgent.view.model.AgentModel
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentCapabilities
import secretAgent.world.entity.agent.Team

/**
 * Creates player start position.
 *
 * @author Ondrej Milenovsky
 */
class PlayerEntityCreator : EntityCreator<HumanAgent> {

    lateinit var capabilities: AgentCapabilities
    lateinit var sizeBounds: Rectangle2D
    lateinit var team: Team
    lateinit var playerHolder: PlayerHolder

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): HumanAgent {
        val dir: EntityXDirection = when {
            args.isEmpty() -> AgentCreatorUtils.getDirection(tileId, model as AgentModel)
            else -> EntityXDirection.valueOf(args.removeAt(0))
        }
        val agent = HumanAgent(model, pos, team, dir, sizeBounds)
        agent.capabilities = capabilities
        agent.health = capabilities.maxHealth
        val weapons = playerHolder.playerStats.weapons
        weapons.loadToInventory(agent.inventory)
        return agent
    }

    companion object {
        private const val serialVersionUID = 8821866142500203428L
    }
}