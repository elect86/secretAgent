package cz.wa.secretagent.game.simulator.entity.explosion;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
import cz.wa.secretagent.game.utils.AgentHurter;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.explosion.Explosion;

/**
 * Simulates a platform.
 * 
 * @author Ondrej Milenovsky
 */
public class ExplosionEntitySimulator extends AbstractEntitySimulator<Explosion> {

    private static final long serialVersionUID = -4048487538785333723L;

    private double blastStrength;

    @Override
    public boolean move(Explosion entity, double timeS) {
        if (entity.getTimeS() == 0) {
            hurtEntities(entity, timeS);
        }
        entity.addTime(timeS);
        if (entity.getTimeS() >= entity.getDurationS()) {
            worldHolder.getWorld().getEntityMap().removeEntity(entity);
        }
        return true;
    }

    private void hurtEntities(Explosion explosion, double timeS) {
        SAMWorld world = worldHolder.getWorld();
        for (Entity entity : world.getEntityMap().getEntities(EntityType.AGENT)) {
            double dist = entity.getPos().distance(explosion.getPos()) / explosion.getRadius();
            // is caught by the explosion
            if (dist <= 1) {
                AgentEntity agent = (AgentEntity) entity;
                // hurt him
                new AgentHurter(world, agent).hurt((1 - dist) * explosion.getDamage());
                // blast him
                Vector2D distV = entity.getPos().subtract(explosion.getPos());
                if (distV.equals(Vector2D.ZERO)) {
                    distV = new Vector2D(0, -0.1);
                }
                Vector2D blast = distV.normalize().scalarMultiply(blastStrength * (1 - dist));
                agent.setSpeed(agent.getSpeed().add(blast));
                // push him one step
                agent.setPos(agent.getPos().add(agent.getSpeed().scalarMultiply(timeS)));
            }
        }
        // TODO blast items
    }

    public double getBlastStrength() {
        return blastStrength;
    }

    @Required
    public void setBlastStrength(double blastStrength) {
        this.blastStrength = blastStrength;
    }

}
