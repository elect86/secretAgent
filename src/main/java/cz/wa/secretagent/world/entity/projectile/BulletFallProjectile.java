package cz.wa.secretagent.world.entity.projectile;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.ObjectModel;
import secretAgent.world.entity.agent.Team;

/**
 * Bullet that falls as it flies. 
 * 
 * @author Ondrej Milenovsky
 */
public class BulletFallProjectile extends ProjectileEntity {

    public BulletFallProjectile(ObjectModel model, Team team) {
        super(model, Vector2D.ZERO, team);
    }

    @Override
    public ProjectileType getSecondType() {
        return ProjectileType.BULLET_FALL;
    }

}
