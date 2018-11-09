package cz.wa.secretagent.world.entity.projectile;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.world.entity.agent.Team;
import secretAgent.world.ObjectModel;

/**
 * Simple bullet flying single direction. 
 * 
 * @author Ondrej Milenovsky
 */
public class BulletProjectile extends ProjectileEntity {

    public BulletProjectile(ObjectModel model, Team team) {
        super(model, Vector2D.ZERO, team);
    }

    @Override
    public ProjectileType getSecondType() {
        return ProjectileType.BULLET;
    }

}
