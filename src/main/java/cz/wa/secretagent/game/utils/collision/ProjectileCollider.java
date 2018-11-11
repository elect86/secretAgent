//package cz.wa.secretagent.game.utils.collision;
//
//import cz.wa.secretagent.utils.raycaster.RayHit;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.EntityType;
//import cz.wa.secretagent.world.entity.agent.AgentEntity;
//import cz.wa.secretagent.world.entity.projectile.ProjectileEntity;
//import cz.wa.secretagent.world.map.LevelMap;
//import cz.wa.secretagent.world.map.TileType;
//import cz.wa.wautils.collection.Array2D;
//import cz.wa.wautils.math.Vector2I;
//import cz.wa.wautils.math.VectorUtils;
//import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
//import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//import secretAgent.world.SamWorld;
//
//import java.util.Collection;
//import java.util.Set;
//
///**
// * Util class to compute collisions of projectile with objects.
// * Projectile starts at current position and flies single time step to the future with its speed.
// * The projectile has no size, can hit square tile or spherical entity.
// *
// * @author Ondrej Milenovsky
// */
//public class ProjectileCollider {
//
//    private final SamWorld world;
//    private final ProjectileEntity projectile;
//    private final Vector2D moveVector;
//    private final double timeS;
//
//    public ProjectileCollider(SamWorld world, ProjectileEntity projectile, double timeS) {
//        this.world = world;
//        this.projectile = projectile;
//        this.timeS = timeS;
//        // move vector limited by range
//        Vector2D v = projectile.getSpeed().scalarMultiply(timeS);
//        double remainingDist = projectile.getRemainingDist();
//        double moveDist = v.getNorm();
//        if (moveDist > remainingDist) {
//            v = v.scalarMultiply(remainingDist / moveDist);
//        }
//        moveVector = v;
//    }
//
//    public Vector2D getMoveVector() {
//        return moveVector;
//    }
//
//    /**
//     * Finds nearest collision with solid tile or entity.
//     * @param tileTypes tile types that can be hit
//     * @param entityTypes entity types that can be hit (agents from same team as the projectile cannot be hit)
//     * @return CollidedTile, CollidedEntity or null if the projectile expired before hitting anything
//     */
//    public CollisionDescriptor findNearestCollision(Set<TileType> tileTypes,
//            Collection<EntityType> entityTypes, double timeS) {
//        CollisionDescriptor ret = null;
//        double minDist = Double.MAX_VALUE;
//        // try hit a tile
//        CollidedTile collidedTile = findNearestTileCollision(tileTypes);
//        if (collidedTile != null) {
//            ret = collidedTile;
//            minDist = projectile.getPos().distance(collidedTile.getHitPos());
//        }
//        // try hit all entity types
//        for (EntityType entityType : entityTypes) {
//            CollidedEntity collidedEntity = findNearestEntityCollision(entityType);
//            if (collidedEntity != null) {
//                double dist = projectile.getPos().distance(collidedEntity.getHitPos());
//                if (dist < minDist) {
//                    ret = collidedEntity;
//                    minDist = dist;
//                }
//            }
//        }
//        return ret;
//    }
//
//    /**
//     * Finds nearest collision with specified types.
//     * Ignores entities.
//     * @param types tile types that are considered solid
//     * @param timeS step time
//     * @return collision descriptor or null if the projectile expired before hitting solid tile
//     */
//    public CollidedTile findNearestTileCollision(Set<TileType> types) {
//        LevelMap levelMap = world.getLevelMap();
//
//        // check current tile
//        Vector2D pos = projectile.getPos();
//        Vector2I currTile = levelMap.getNearestTilePos(pos);
//        TileType currType = getTileAt(currTile);
//        if (types.contains(currType)) {
//            // hit current tile
//            return new CollidedTile(currTile, pos);
//        }
//        double norm = moveVector.getNorm();
//        if (norm == 0) {
//            // not moving
//            return null;
//        }
//        // check final tile
//        Vector2D finalPos = pos.add(moveVector);
//        Vector2I finalTile = levelMap.getNearestTilePos(finalPos);
//        if (finalTile.equals(currTile)) {
//            // didn't move to another tile, no collision
//            return null;
//        }
//        // check all tiles that can hit
//        double maxDist = Math.min(projectile.getRemainingDist(), norm * timeS);
//        RayHit rayHit = new RayCasterSAM(pos, moveVector, maxDist, world.getLevelMap(), types).castRay();
//        if (rayHit == null) {
//            return null;
//        } else {
//            return new CollidedTile(rayHit);
//        }
//    }
//
//    private TileType getTileAt(Vector2I pos) {
//        Array2D<TileType> types = world.getLevelMap().getTypes();
//        if (types.isInside(pos)) {
//            return types.get(pos);
//        } else {
//            return TileType.WALL;
//        }
//    }
//
//    /**
//     * Finds nearest collision with entity of specified type that is not from same team as the projectile.
//     * Entity shape is sphere computed from its bounds. Ignores tiles.
//     * @param entityType entity type that will be hit
//     * @param timeS step time
//     * @return collision descriptor or null if the projectile expired before hitting an entity or didn't hit any entity
//     */
//    public CollidedEntity findNearestEntityCollision(EntityType entityType) {
//        Entity nearestEntity = null;
//        double minDist = Double.MAX_VALUE;
//        Vector2D rotatedPos = null;
//        double entitySize = 0;
//
//        Vector2D pPos = projectile.getPos();
//        double angle = VectorUtils.getAngle(projectile.getSpeed());
//        double moveDist = FastMath.min(projectile.getSpeed().getNorm(), projectile.getRemainingDist())
//                * timeS;
//
//        Rotation r = new Rotation(new Vector3D(0, 0, 1), -angle);
//
//        for (Entity entity : world.getEntityMap().getEntities(entityType)) {
//            if ((entity.getType() == EntityType.AGENT)
//                    && (((AgentEntity) entity).getTeam() == projectile.getTeam())) {
//                continue;
//            }
//            Vector2D pos = entity.getPos().subtract(pPos);
//            pos = VectorUtils.getVector2D(r.applyTo(VectorUtils.getVector3D(pos)));
//            double size = entity.getSizeBounds().getY2();
//            if (((pos.getX() < minDist) && (pos.getX() > 0) && (pos.getX() < moveDist) && (FastMath.abs(pos
//                    .getY()) < size)) || (pos.getNorm() < size)) {
//                nearestEntity = entity;
//                minDist = pos.getX();
//                rotatedPos = pos;
//                entitySize = size;
//            }
//        }
//
//        if (nearestEntity != null) {
//            double hitDist = minDist - FastMath.sqrt(sqr(entitySize) - sqr(rotatedPos.getY()));
//            Vector2D hitPos = new Vector2D(hitDist, 0);
//            hitPos = VectorUtils.getVector2D(r.applyInverseTo(VectorUtils.getVector3D(hitPos)));
//            return new CollidedEntity(nearestEntity, projectile.getPos().add(hitPos));
//        } else {
//            return null;
//        }
//    }
//
//    private static double sqr(double d) {
//        return d * d;
//    }
//}
