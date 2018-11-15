package secretAgent.game.utils

import cz.wa.secretagent.Constants
import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity
import cz.wa.secretagent.world.entity.platform.PlatformEntity
import cz.wa.secretagent.world.entity.platform.PlatformType
import cz.wa.secretagent.world.entity.usable.UsableEntity
import cz.wa.secretagent.world.map.TileType
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.world.SamWorld
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.agent.AgentEntity
import secretAgent.world.entity.agent.AgentType
import java.util.ArrayList

/**
 * Finds specific collections of entities from all entities.
 *
 * @author Ondrej Milenovsky
 */
class EntitiesFinder(private val world: SamWorld) {

    /**
     * @return all platforms and cans (solid usables create their walls)
     */
    val solidEntities: List<Entity>
        get() = world.entityMap.getEntities(EntityType.PLATFORM).toCollection(ArrayList())

    /**
     * @return list of entities that can fall (human agents, movable cans, some items)
     */
    val fallableEntities: List<Entity>
        get() {
            val ret = ArrayList<Entity>()
            val entityMap = world.entityMap
            for (entity in entityMap.getEntities(EntityType.AGENT))
                if (entity.secondType == AgentType.HUMAN)
                    ret += entity
            for (entity in entityMap.getEntities(EntityType.PLATFORM))
                if (entity.secondType == PlatformType.CAN)
                    ret += entity
            for (entity in entityMap.getEntities(EntityType.ITEM))
                if (!entity.isStaticPos)
                    ret += entity
            return ret
        }

    /**
     * Finds closest usable entity in front of the agent (usables and switches)
     */
    fun findUsableEntity(agent: AgentEntity, maxDist: Double): Entity? {
        var ret: Entity? = null
        val addVector = agent.direction.vector.scalarMultiply(Constants.TILE_SIZE.x)
        val pos = agent.pos.add(addVector)
        val bounds = agent.sizeBounds.move(agent.pos).expand(Vector2D(maxDist, maxDist))

        // usables
        for (entity in world.entityMap.getEntities(EntityType.USABLE)) {
            val usable = entity as UsableEntity
            val entityBounds = usable.sizeBounds.move(usable.pos)
            if (usable.isActive && bounds.intersectsOrTouches(entityBounds) && isBySide(entityBounds, agent.pos)) {
                if (ret == null || pos.distance(usable.pos) < pos.distance(ret.pos))
                    ret = usable
            }
        }
        // switches
        for (entity in world.entityMap.getEntities(EntityType.SWITCH)) {
            val entityBounds = entity.sizeBounds.move(entity.pos)
            if ((entity as SwitchEntity).isActive)
                if (bounds.intersectsOrTouches(entityBounds) && isBySide(entityBounds, agent.pos))
                    if (ret == null || pos.distance(entity.pos) < pos.distance(ret.pos))
                        ret = entity
        }
        return ret
    }

    /**
     * Finds entities standing on the platform.
     * @param lift some platform
     * @return list of entities
     */
    fun findEntitiesToCarry(lift: PlatformEntity): List<Entity> {
        val liftBounds = lift.sizeBounds.move(lift.pos)
        val carying = ArrayList<Entity>(3)
        for (e2 in fallableEntities) {
            val posSensor = EntityObserver(e2, world)
            if (posSensor.isStandingOn(lift)) {
                // check if the entity is standing more on tile
                val entityBounds = e2.sizeBounds.move(e2.pos)
                val liftDx = getXIntersect(entityBounds, liftBounds)
                var add = true
                for (tile in posSensor.tilesUnder)
                    if (tile.type == TileType.WALL && posSensor.isStandingOn(tile.bounds)) {
                        val dx = getXIntersect(entityBounds, tile.bounds)
                        if (liftDx <= dx) {
                            add = false
                            break
                        }
                    }
                if (add)
                    carying += e2
            }
        }
        return carying
    }

    /**
     * Intersecting length of x values, they already intersects.
     */
    private fun getXIntersect(bounds1: Rectangle2D, bounds2: Rectangle2D): Double =
            FastMath.min(bounds1.x2, bounds2.x2) - FastMath.max(bounds1.x, bounds2.x)

    /**
     * @param objectBounds the object bounds
     * @param entityPos the entity center
     * @return if the entity center is vertically or horizontally within the object
     */
    private fun isBySide(objectBounds: Rectangle2D, entityPos: Vector2D): Boolean =
            objectBounds.x < entityPos.x && objectBounds.x2 > entityPos.x || objectBounds.y < entityPos.y && objectBounds.y2 > entityPos.y
}