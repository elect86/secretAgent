package cz.wa.secretagent.world.entity.projectile;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import cz.wa.secretagent.world.entity.agent.Team;
import secretAgent.world.ObjectModel;

/**
 * Rocket that flies like bullet and then explodes. 
 * 
 * @author Ondrej Milenovsky
 */
public class RocketProjectile extends ProjectileEntity {

    public RocketProjectile(ObjectModel model, Team team) {
        super(model, Vector2D.ZERO, team);
    }

    @Override
    public ProjectileType getSecondType() {
        return ProjectileType.ROCKET;
    }

}
