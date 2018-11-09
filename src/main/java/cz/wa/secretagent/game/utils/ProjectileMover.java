package cz.wa.secretagent.game.utils;

import cz.wa.secretagent.game.simulator.entity.projectile.ProjectileHitTypes;
import cz.wa.secretagent.game.utils.collision.CollidedEntity;
import cz.wa.secretagent.game.utils.collision.CollidedTile;
import cz.wa.secretagent.game.utils.collision.CollisionDescriptor;
import cz.wa.secretagent.game.utils.collision.ProjectileCollider;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.projectile.ProjectileEntity;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.SamWorld;

/**
 * Util class to move and collide projectile. 
 * 
 * @author Ondrej Milenovsky
 */
public class ProjectileMover {
    private final SamWorld world;
    private final ProjectileEntity projectile;
    private final double timeS;

    public ProjectileMover(SamWorld world, ProjectileEntity projectile, double timeS) {
        super();
        this.world = world;
        this.projectile = projectile;
        this.timeS = timeS;
    }

    /**
     * Move the bullet in the world and do all collisions.
     * @return true if the bullet is still alive
     */
    public boolean move() {
        ProjectileCollider projectileCollider = new ProjectileCollider(world, projectile, timeS);
        CollisionDescriptor collision = projectileCollider.findNearestCollision(
                ProjectileHitTypes.TILE_TYPES, ProjectileHitTypes.ENTITY_TYPES, timeS);

        if (collision != null) {
            // projectile collided
            if (collision instanceof CollidedTile) {
                // collided with tile
                // TODO impact effect
            } else if (collision instanceof CollidedEntity) {
                // collided with entity
                Entity entity = ((CollidedEntity) collision).getEntity();
                if (entity.getType() == EntityType.AGENT) {
                    new AgentHurter(world, (AgentEntity) entity).hit(projectile);
                }
            }
            world.getEntityMap().removeEntity(projectile);
            return false;
        } else {
            // no collision, just move the projectile
            double remainingDist = projectile.getRemainingDist();
            if (remainingDist <= 0) {
                // bullet expired
                world.getEntityMap().removeEntity(projectile);
                return false;
            }
            // move the projectile
            Vector2D moveVector = projectileCollider.getMoveVector();
            projectile.setRemainingDist(projectile.getRemainingDist() - projectile.getSpeed().getNorm()
                    * timeS);
            projectile.setPos(projectile.getPos().add(moveVector));
            return true;
        }
    }
}
