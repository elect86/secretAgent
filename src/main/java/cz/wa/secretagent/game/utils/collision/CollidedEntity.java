//package cz.wa.secretagent.game.utils.collision;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.world.entity.Entity;
//
///**
//  * Class describing collision of projectile with entity.
//  *
//  * @author Ondrej Milenovsky
//  */
//public class CollidedEntity implements CollisionDescriptor {
//    private final Entity entity;
//    private final Vector2D hitPos;
//
//    public CollidedEntity(Entity entity, Vector2D hitPos) {
//        this.entity = entity;
//        this.hitPos = hitPos;
//    }
//
//    /**
//     * @return point where the projectile hit the tile
//     */
//    @Override
//    public Vector2D getHitPos() {
//        return hitPos;
//    }
//
//    /**
//     * @return entity hit by the projectile
//     */
//    public Entity getEntity() {
//        return entity;
//    }
//}
