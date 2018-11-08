package cz.wa.secretagent.game.simulator.entity.projectile;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.projectile.ProjectileFactory;
import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
import cz.wa.secretagent.game.utils.ProjectileMover;
import cz.wa.secretagent.world.entity.explosion.Explosion;
import cz.wa.secretagent.world.entity.projectile.RocketProjectile;

/**
 * Simulates rocket. 
 * Rocket flies single direction and expires after reached its max range. Can hit walls or agents.
 * Explodes in every case of destruction except if it hit water. 
 * 
 * @author Ondrej Milenovsky
 */
public class RocketEntitySimulator extends AbstractEntitySimulator<RocketProjectile> {

    private static final long serialVersionUID = 5051555835278811068L;

    private ProjectileFactory projectileFactory;
    private double explosionDamage;
    private double explosionRadius;
    private double explosionDurationS;
    private String explosionModel;

    @Override
    public boolean move(RocketProjectile entity, double timeS) {
        if (!new ProjectileMover(worldHolder.getWorld(), entity, timeS).move()) {
            Explosion explosion = projectileFactory.createExplosion(entity.getPos(), explosionModel,
                    explosionRadius, explosionDamage, explosionDurationS);
            worldHolder.getWorld().getEntityMap().addEntity(explosion);
        }
        return true;
    }

    public ProjectileFactory getProjectileFactory() {
        return projectileFactory;
    }

    @Required
    public void setProjectileFactory(ProjectileFactory projectileFactory) {
        this.projectileFactory = projectileFactory;
    }

    public double getExplosionDamage() {
        return explosionDamage;
    }

    @Required
    public void setExplosionDamage(double explosionDamage) {
        this.explosionDamage = explosionDamage;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    @Required
    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public double getExplosionDurationS() {
        return explosionDurationS;
    }

    @Required
    public void setExplosionDurationS(double explosionDurationS) {
        this.explosionDurationS = explosionDurationS;
    }

    public String getExplosionModel() {
        return explosionModel;
    }

    @Required
    public void setExplosionModel(String explosionModel) {
        this.explosionModel = explosionModel;
    }

}
