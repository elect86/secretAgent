package cz.wa.secretagent.game.simulator.entity.projectile;

import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
import cz.wa.secretagent.game.utils.ProjectileMover;
import cz.wa.secretagent.world.entity.projectile.BulletProjectile;

/**
 * Simulates simple bullet.
 * Bullet flies single direction and expires after reached its max range. Can hit walls or agents. 
 * 
 * @author Ondrej Milenovsky
 */
public class BulletEntitySimulator extends AbstractEntitySimulator<BulletProjectile> {

    private static final long serialVersionUID = 5788654204870930575L;

    @Override
    public boolean move(BulletProjectile entity, double timeS) {
        new ProjectileMover(worldHolder.getWorld(), entity, timeS).move();
        return true;
    }
}
