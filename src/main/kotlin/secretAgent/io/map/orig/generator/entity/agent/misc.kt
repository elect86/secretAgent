package secretAgent.io.map.orig.generator.entity.agent

import cz.wa.secretagent.world.weapon.Weapon
import secretAgent.world.entity.agent.AgentCapabilities

/**
 * Weapons and capabilities for enemy human agent.
 *
 * @author Ondrej Milenovsky
 */
class EnemyHumanProperties(val capabilities: AgentCapabilities, val weapons: List<Weapon>)

/**
 * Type of enemy human agent.
 *
 * @author Ondrej Milenovsky
 */
enum class EnemyHumanType {
    WHITE_GUY,
    RED_GUY,
    NINJA_GUY,
    BLUE_GUY,
    GRAY_GUY
}