package secretAgent.io.map.orig.generator.entity.agent

import cz.wa.secretagent.world.entity.agent.HumanAgent
import cz.wa.secretagent.world.entity.agent.Team
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.io.map.orig.generator.entity.EntityCreator
import secretAgent.view.model.AgentModel
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.entity.EntityXDirection

/**
 * Creates player start position.
 *
 * @author Ondrej Milenovsky
 */
class HumanEntityCreator : EntityCreator<HumanAgent> {

    lateinit var sizeBounds: Rectangle2D
    lateinit var team: Team
    lateinit var propertiesCreator: EnemyHumanPropertiesCreator

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): HumanAgent? {

        if (args.isEmpty()) {
            logger.warn("Human agent model requires one more argument for human type: $tileId")
            return null
        }
        val type = getType(args.removeAt(0))!!
        val dir: EntityXDirection = when {
            args.isEmpty() -> AgentCreatorUtils.getDirection(tileId, model as AgentModel)
            else -> EntityXDirection.valueOf(args.removeAt(0))
        }
        val human = HumanAgent(model, pos, team, dir, sizeBounds)

        val humanProperties = propertiesCreator.createProperties(type)
        human.capabilities = humanProperties.capabilities
        human.health = humanProperties.capabilities.maxHealth
        val weapons = humanProperties.weapons
        if (weapons.isNotEmpty())
            human.weapon = weapons[0]
        for (weapon in weapons) {
            human.inventory.addWeapon(weapon)
            human.inventory.addAmmo(weapon, Integer.MAX_VALUE)
        }
        return human
    }

    private fun getType(str: String): EnemyHumanType? {
        try {
            return EnemyHumanType.valueOf(str)
        } catch (e: IllegalArgumentException) {
            logger.error("Unknown enemy human type: $str", e)
        }

        return null
    }

    companion object {
        private const val serialVersionUID = -9044910312669697692L
        private val logger = LoggerFactory.getLogger(HumanEntityCreator::class.java)
    }
}