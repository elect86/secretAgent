package secretAgent.game.utils

import secretAgent.game.simulator.entity.ProjectileHitTypes
import secretAgent.world.SamWorld
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.ProjectileEntity
import secretAgent.world.entity.agent.AgentEntity

/**
 * Util class to move and collide projectile.
 *
 * @author Ondrej Milenovsky
 */
class ProjectileMover(private val world: SamWorld,
                      private val projectile: ProjectileEntity,
                      private val timeS: Double) {

    /**
     * Move the bullet in the world and do all collisions.
     * @return true if the bullet is still alive
     */
    fun move(): Boolean {
        val projectileCollider = ProjectileCollider(world, projectile, timeS)
        val collision = projectileCollider.findNearestCollision(
                ProjectileHitTypes.TILE_TYPES, ProjectileHitTypes.ENTITY_TYPES, timeS)

        if (collision != null) {
            // projectile collided
            if (collision is CollidedTile) {
                // collided with tile
                // TODO impact effect
            } else if (collision is CollidedEntity) {
                // collided with entity
                val entity = collision.entity
                if (entity.type == EntityType.AGENT)
                    AgentHurter(world, entity as AgentEntity).hit(projectile)
            }
            world.entityMap.removeEntity(projectile)
            return false
        } else {
            // no collision, just move the projectile
            val remainingDist = projectile.remainingDist
            if (remainingDist <= 0) {
                // bullet expired
                world.entityMap.removeEntity(projectile)
                return false
            }
            // move the projectile
            val moveVector = projectileCollider.moveVector
            projectile.remainingDist = projectile.remainingDist - projectile.speed.norm * timeS
            projectile.pos = projectile.pos.add(moveVector)
            return true
        }
    }
}