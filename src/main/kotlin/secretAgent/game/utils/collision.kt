package secretAgent.game.utils

import cz.wa.secretagent.utils.raycaster.RayHit
import cz.wa.wautils.math.Vector2I
import cz.wa.wautils.math.VectorUtils
import org.apache.commons.math3.geometry.euclidean.threed.Rotation
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.utils.RayCaster
import secretAgent.world.LevelMap
import secretAgent.world.SamWorld
import secretAgent.world.TileType
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityType
import secretAgent.world.entity.ProjectileEntity
import secretAgent.world.entity.agent.AgentEntity

/**
 * Class describing collision of projectile with entity.
 *
 * @author Ondrej Milenovsky
 */
class CollidedEntity(
        /** entity hit by the projectile         */
        val entity: Entity,
        /** point where the projectile hit the tile         */
        override val hitPos: Vector2D) : CollisionDescriptor

/**
 * Class describing collision of projectile with tile.
 *
 * @author Ondrej Milenovsky
 */
class CollidedTile(
    /** position of the tile in the grid     */
    val tilePos: Vector2I,
    /** point where the projectile hit the tile     */
    override val hitPos: Vector2D) : CollisionDescriptor {

    constructor(rayHit: RayHit) : this( rayHit.mapPos, rayHit.hitPos)
}

/**
 * Object describing collision of projectile with something.
 *
 * @author Ondrej Milenovsky
 */
interface CollisionDescriptor {
    /** @return point where the projectile hit something     */
    val hitPos: Vector2D
}

/**
 * Util class to compute collisions of projectile with objects.
 * Projectile starts at current position and flies single time step to the future with its speed.
 * The projectile has no size, can hit square tile or spherical entity.
 *
 * @author Ondrej Milenovsky
 */
class ProjectileCollider(
        private val world: SamWorld,
        private val projectile: ProjectileEntity,
        private val timeS: Double) {

    val moveVector: Vector2D

    init {
        // move vector limited by range
        var v = projectile.speed.scalarMultiply(timeS)
        val remainingDist = projectile.remainingDist
        val moveDist = v.norm
        if (moveDist > remainingDist)
            v = v.scalarMultiply(remainingDist / moveDist)
        moveVector = v
    }

    /**
     * Finds nearest collision with solid tile or entity.
     * @param tileTypes tile types that can be hit
     * @param entityTypes entity types that can be hit (agents from same team as the projectile cannot be hit)
     * @return CollidedTile, CollidedEntity or null if the projectile expired before hitting anything
     */
    fun findNearestCollision(tileTypes: Set<TileType>,
                             entityTypes: Collection<EntityType>, timeS: Double): CollisionDescriptor? {
        var ret: CollisionDescriptor? = null
        var minDist = java.lang.Double.MAX_VALUE
        // try hit a tile
        val collidedTile = findNearestTileCollision(tileTypes)
        if (collidedTile != null) {
            ret = collidedTile
            minDist = projectile.pos.distance(collidedTile.hitPos)
        }
        // try hit all entity types
        for (entityType in entityTypes)
            findNearestEntityCollision(entityType)?.let { collidedEntity ->
                val dist = projectile.pos.distance(collidedEntity.hitPos)
                if (dist < minDist) {
                    ret = collidedEntity
                    minDist = dist
                }
            }
        return ret
    }

    /**
     * Finds nearest collision with specified types.
     * Ignores entities.
     * @param types tile types that are considered solid
     * @param timeS step time
     * @return collision descriptor or null if the projectile expired before hitting solid tile
     */
    fun findNearestTileCollision(types: Set<TileType>): CollidedTile? {
        val levelMap = world.levelMap

        // check current tile
        val pos = projectile.pos
        val currTile = levelMap.getNearestTilePos(pos)
        val currType = getTileAt(currTile)
        if (types.contains(currType))
            return CollidedTile(currTile, pos) // hit current tile
        val norm = moveVector.norm
        if (norm == 0.0)
            return null // not moving
        // check final tile
        val finalPos = pos.add(moveVector)
        val finalTile = levelMap.getNearestTilePos(finalPos)
        if (finalTile == currTile)
            return null // didn't move to another tile, no collision
        // check all tiles that can hit
        val maxDist = Math.min(projectile.remainingDist, norm * timeS)
        val rayHit = RayCasterSAM(pos, moveVector, maxDist, world.levelMap, types).castRay()
        return rayHit?.let(::CollidedTile)
    }

    private fun getTileAt(pos: Vector2I): TileType {
        val types = world.levelMap.types
        return when {
            types.isInside(pos) -> types.get(pos)
            else -> TileType.WALL
        }
    }

    /**
     * Finds nearest collision with entity of specified type that is not from same team as the projectile.
     * Entity shape is sphere computed from its bounds. Ignores tiles.
     * @param entityType entity type that will be hit
     * @param timeS step time
     * @return collision descriptor or null if the projectile expired before hitting an entity or didn't hit any entity
     */
    fun findNearestEntityCollision(entityType: EntityType): CollidedEntity? {
        var nearestEntity: Entity? = null
        var minDist = Double.MAX_VALUE
        var rotatedPos: Vector2D? = null
        var entitySize = 0.0

        val pPos = projectile.pos
        val angle = VectorUtils.getAngle(projectile.speed)
        val moveDist = FastMath.min(projectile.speed.norm, projectile.remainingDist) * timeS

        val r = Rotation(Vector3D(0.0, 0.0, 1.0), -angle)

        for (entity in world.entityMap.getEntities(entityType)) {
            if (entity.type == EntityType.AGENT && (entity as AgentEntity).team === projectile.team)
                continue
            var pos = entity.pos.subtract(pPos)
            pos = VectorUtils.getVector2D(r.applyTo(VectorUtils.getVector3D(pos)))
            val size = entity.sizeBounds.y2
            if (pos.x < minDist && pos.x > 0 && pos.x < moveDist && FastMath.abs(pos.y) < size || pos.norm < size) {
                nearestEntity = entity!!
                minDist = pos.x
                rotatedPos = pos
                entitySize = size
            }
        }

        return when {
            nearestEntity != null -> {
                val hitDist = minDist - FastMath.sqrt(sqr(entitySize) - sqr(rotatedPos!!.y))
                var hitPos = Vector2D(hitDist, 0.0)
                hitPos = VectorUtils.getVector2D(r.applyInverseTo(VectorUtils.getVector3D(hitPos)))
                CollidedEntity(nearestEntity, projectile.pos.add(hitPos))
            }
            else -> null
        }
    }

    private fun sqr(d: Double) = d * d
}

/**
 * Ray casting.
 *
 * @author Ondrej Milenovsky
 */
class RayCasterSAM(startPos: Vector2D, dir: Vector2D, maxDist: Double,
                   private val map: LevelMap,
                   private val hitTypes: Set<TileType>) : RayCaster(convertTo1(startPos, map.tileSize), dir, maxDist / map.tileSize.x) {

    /**
     * Inverse to convertTo1
     * @param v
     * @param tileSize
     * @return
     */
    private fun convertFrom1(v: Vector2D, tileSize: Vector2D) = Vector2D((v.x - 0.5) * tileSize.x, (v.y - 0.5) * tileSize.y)

    override fun castRay(): RayHit? {
        val ret = super.castRay()
        return ret?.let { RayHit(ret.mapPos, convertFrom1(ret.hitPos, map.tileSize))        }
    }

    override fun isWall(i: Vector2I): Boolean {
        val types = map.types
        return when {
            types.isInside(i) -> hitTypes.contains(types.get(i))
            else -> true
        }
    }

    companion object {
        /**
         * Converts vector from world position to coords where 0:0 is upper left corner and tile size is 1x1.
         * @param v
         * @param tileSize
         * @return
         */
        private fun convertTo1(v: Vector2D, tileSize: Vector2D)= Vector2D(0.5 + v.x / tileSize.x, 0.5 + v.y / tileSize.y)
    }
}