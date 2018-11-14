package secretAgent.io.map.orig.generator.entity.agent

import cz.wa.secretagent.world.entity.item.ItemType
import cz.wa.secretagent.world.weapon.Weapon
import secretAgent.world.entity.agent.AgentCapabilities
import secretAgent.world.entity.agent.AgentCapabilitiesTmp
import secretAgent.world.entity.agent.InventoryLimits
import java.io.Serializable

/**
 * Creates weapons and capabilities for enemy human agents.
 *
 * @author Ondrej Milenovsky
 */
class EnemyHumanPropertiesCreator : Serializable {

    /**
     * Creates properties for an enemy agent.
     * @param type agent type
     * @return new properties
     */
    fun createProperties(type: EnemyHumanType): EnemyHumanProperties {
        // TODO make enemies much more evil
        val capabilities = AgentCapabilitiesTmp()

        val inventoryLimits = InventoryLimits()
        inventoryLimits.picksItems!!.add(ItemType.JUNK)

        capabilities.inventoryLimits = inventoryLimits
        capabilities.isCanAim = true
        when {
            type == EnemyHumanType.WHITE_GUY -> {
                capabilities.maxHealth = 100.0
                capabilities.maxSpeed = 20.0
            }
            type == EnemyHumanType.RED_GUY -> {
                capabilities.maxHealth = 200.0
                capabilities.maxSpeed = 40.0
            }
            type == EnemyHumanType.NINJA_GUY -> {
                capabilities.maxHealth = 300.0
                capabilities.maxSpeed = 120.0
                capabilities.jumpStrength = 200.0
                capabilities.jumpTimeS = 0.1
            }
            type == EnemyHumanType.BLUE_GUY -> {
                capabilities.maxHealth = 400.0
                capabilities.maxSpeed = 80.0
            }
            type == EnemyHumanType.GRAY_GUY -> {
                capabilities.maxHealth = 500.0
                capabilities.maxSpeed = 100.0
                capabilities.jumpStrength = 120.0
                capabilities.jumpTimeS = 0.15
            }
            else -> capabilities.maxHealth = 1.0
        }
        val weapons = emptyList<Weapon>()
        return EnemyHumanProperties(AgentCapabilities(capabilities), weapons)
    }

    companion object {
        private const val serialVersionUID = 5141884147037917924L
    }
}