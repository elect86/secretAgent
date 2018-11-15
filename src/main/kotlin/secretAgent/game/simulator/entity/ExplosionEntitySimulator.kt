package secretAgent.game.simulator.entity

import cz.wa.secretagent.world.entity.explosion.Explosion
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.game.utils.AgentHurter
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.agent.AgentEntity

/**
 * Simulates a platform.
 *
 * @author Ondrej Milenovsky
 */
class ExplosionEntitySimulator : AbstractEntitySimulator<Explosion>() {

    var blastStrength = 0.0

    override fun move(entity: Explosion, timeS: Double): Boolean {
        if (entity.timeS == 0.0)
            hurtEntities(entity, timeS)
        entity.addTime(timeS)
        if (entity.timeS >= entity.durationS)
            worldHolder.world.entityMap.removeEntity(entity)
        return true
    }

    private fun hurtEntities(explosion: Explosion, timeS: Double) {
        val world = worldHolder.world
        for (entity in world.entityMap.getEntities(EntityType.AGENT)) {
            val dist = entity.pos.distance(explosion.pos) / explosion.radius
            // is caught by the explosion
            if (dist <= 1) {
                val agent = entity as AgentEntity
                // hurt him
                AgentHurter(world, agent).hurt((1 - dist) * explosion.damage)
                // blast him
                var distV = entity.pos.subtract(explosion.pos)
                if (distV == Vector2D.ZERO)
                    distV = Vector2D(0.0, -0.1)
                val blast = distV.normalize().scalarMultiply(blastStrength * (1 - dist))
                agent.speed = agent.speed.add(blast)
                // push him one step
                agent.pos = agent.pos.add(agent.speed.scalarMultiply(timeS))
            }
        }
        // TODO blast items
    }

    companion object {
        private const val serialVersionUID = -4048487538785333723L
    }
}