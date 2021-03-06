package secretAgent.game.utils

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.SamWorld
import secretAgent.world.entity.EntityYDirection
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.ItemType
import secretAgent.world.entity.ProjectileEntity
import secretAgent.world.entity.agent.AgentAction
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentType
import java.util.*

/**
 * Class used to hurt (or heal) agents.
 *
 * @author Ondrej Milenovsky
 */
class AgentHurter(private val world: SamWorld,
                  private val agent: AgentEntity) {

    /**
     * Hit agent with the projectile. If the agent's health < 0, it will kill him.
     * @param projectile
     */
    fun hit(projectile: ProjectileEntity) = hurt(projectile.damage)

    fun hurt(damage: Double) {
        val health = agent.health - damage
        // TODO bleed
        if (health < 0) {
            agent.health = 0.0
            agent.currentAction = AgentAction.DEATH
            agent.isFiring = false
            agent.moveSpeed = Vector2D.ZERO
            if (agent.secondType == AgentType.HUMAN) {
                val human = agent as HumanAgent
                human.isJumping = false
                human.aiming = EntityYDirection.NONE
                human.weapon = null
                // drop items
                // TODO drop weapons, moving items
                val inventory = human.inventory
                for (item in ArrayList(inventory.getItems(ItemType.JUNK))) {
                    item.pos = human.pos
                    inventory.removeItem(item)
                    world.entityMap.addEntity(item)
                }
            }
        } else
            agent.health = health
    }
}