package secretAgent.game.simulator.entity

import cz.wa.secretagent.io.SAMIO
import cz.wa.secretagent.world.entity.EntityType
import cz.wa.secretagent.world.entity.projectile.BulletFallProjectile
import cz.wa.secretagent.world.entity.projectile.BulletProjectile
import cz.wa.secretagent.world.entity.projectile.DynamiteProjectile
import cz.wa.secretagent.world.entity.projectile.RocketProjectile
import cz.wa.secretagent.world.entity.usable.ExitDoorUsable
import cz.wa.secretagent.world.entity.usable.ExitUsable
import cz.wa.secretagent.world.entity.usable.UsableType
import cz.wa.secretagent.world.map.StoredTile
import cz.wa.secretagent.world.map.TileType
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.game.ProjectileFactory
import secretAgent.game.starter.MapStarter
import secretAgent.game.utils.EntityObserver
import secretAgent.game.utils.ProjectileMover
import java.util.*

/**
 * Simulates simple bullet.
 * Bullet flies single direction and expires after reached its max range. Can hit walls or agents.
 *
 * @author Ondrej Milenovsky
 */
class BulletEntitySimulator : AbstractEntitySimulator<BulletProjectile>() {

    override fun move(entity: BulletProjectile, timeS: Double): Boolean {
        ProjectileMover(worldHolder.world, entity, timeS).move()
        return true
    }

    companion object {
        private const val serialVersionUID = 5788654204870930575L
    }
}

/**
 * Simulates bullet that falls to the ground.
 * Bullet flies single direction and falls, expires after reached its max range.
 * Can hit walls or agents.
 *
 * @author Ondrej Milenovsky
 */
class BulletFallEntitySimulator : AbstractEntitySimulator<BulletFallProjectile>() {

    var fallG = 0.0

    override fun move(entity: BulletFallProjectile, timeS: Double): Boolean {
        if (ProjectileMover(worldHolder.world, entity, timeS).move())
            // if the bullet is still alive, increase y speed
            entity.speed = entity.speed.add(Vector2D(0.0, fallG * timeS))
        return true
    }

    companion object {
        private const val serialVersionUID = -8779107845319125341L
    }
}

/**
 * Simulates dynamite. At first the dynamite burns, then explodes.
 * Opens all exits near the dynamite, also creates explosion to hurt agents.
 *
 * @author Ondrej Milenovsky
 */
class DynamiteEntitySimulator : AbstractEntitySimulator<DynamiteProjectile>() {

    lateinit var mapStarter: MapStarter
    lateinit var samIO: SAMIO
    lateinit var projectileFactory: ProjectileFactory

    var explosionDamage = 0.0
    var explosionRadius = 0.0
    var explosionDurationS = 0.0
    lateinit var explosionModel: String

    override fun move(entity: DynamiteProjectile, timeS: Double): Boolean {
        entity.addTime(timeS)
        if (entity.remainingTimeS <= 0) {
            worldHolder.world.entityMap.removeEntity(entity)
            val exitDoor = replaceExit()
            replaceTiles(exitDoor!!)
            explode(entity.pos)
        }
        return true
    }

    private fun explode(pos: Vector2D) {
        val explosion = projectileFactory.createExplosion(pos, explosionModel, explosionRadius, explosionDamage, explosionDurationS)
        worldHolder.world.entityMap.addEntity(explosion)
    }

    private fun replaceTiles(exitDoor: ExitDoorUsable) {
        val replaceTiles = exitDoor.replaceTiles
        val map = worldHolder.world.levelMap
        for (sensing in EntityObserver(exitDoor, worldHolder.world).get9TilesAround(replaceTiles.keys)) {
            val pos = sensing.pos
            map.removeTile(pos, sensing.tile)
            map.addTile(StoredTile(pos, replaceTiles[sensing.tileId]))
            map.updateType(pos)
        }
    }

    /**
     * Replace the closed exit door by open, returns the closed exit door
     */
    private fun replaceExit(): ExitDoorUsable? {
        val entityMap = worldHolder.world.entityMap
        val levelMap = worldHolder.world.levelMap

        for (usable in ArrayList(entityMap.getEntities(EntityType.USABLE))) {
            // TODO closest
            if (usable.secondType == UsableType.EXIT_DOOR) {
                val exitDoor = usable as ExitDoorUsable
                // remove the old switch
                entityMap.removeEntity(usable)
                // add exit switch
                val model = samIO.worldHolder.graphicsInfo.getModel(exitDoor.openModel)
                val exit = ExitUsable(model, usable.getPos())
                entityMap.addEntity(exit)
                val pos = levelMap.getNearestTilePos(exit.pos)
                levelMap.updateType(pos)
                return exitDoor
            }
        }
        return null
    }

    companion object {
        private const val serialVersionUID = 6710267400479925390L
    }
}

/**
 * Class holding types that can be hit by projectiles.
 *
 * @author Ondrej Milenovsky
 */
object ProjectileHitTypes {

    @JvmField
    val TILE_TYPES: Set<TileType> = HashSet(Arrays.asList(TileType.WALL, TileType.WATER))
    @JvmField
    val ENTITY_TYPES = Arrays.asList(EntityType.AGENT)
}

/**
 * Simulates rocket.
 * Rocket flies single direction and expires after reached its max range. Can hit walls or agents.
 * Explodes in every case of destruction except if it hit water.
 *
 * @author Ondrej Milenovsky
 */
class RocketEntitySimulator : AbstractEntitySimulator<RocketProjectile>() {

    lateinit var projectileFactory: ProjectileFactory
    var explosionDamage = 0.0
    var explosionRadius = 0.0
    var explosionDurationS = 0.0
    lateinit var explosionModel: String

    override fun move(entity: RocketProjectile, timeS: Double): Boolean {
        if (!ProjectileMover(worldHolder.world, entity, timeS).move()) {
            val explosion = projectileFactory.createExplosion(entity.pos, explosionModel, explosionRadius, explosionDamage, explosionDurationS)
            worldHolder.world.entityMap.addEntity(explosion)
        }
        return true
    }

    companion object {
        private const val serialVersionUID = 5051555835278811068L
    }
}