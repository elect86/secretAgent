package cz.wa.secretagent.world.entity.bgswitch.switchaction;

import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.laser.LaserEntity;
import secretAgent.world.SamWorld;

import java.util.ArrayList;

/**
 * Disables laser.
 *
 * @author Ondrej Milenovsky
 */
public class DisableLaserSwitchAction implements SwitchAction {

    @Override
    public void execute(AgentEntity agent, SamWorld world) {
        EntityMap entityMap = world.getEntityMap();
        for (Entity entity : new ArrayList<Entity>(entityMap.getEntities(EntityType.LASER))) {
            if (((LaserEntity) entity).isLevelLaser()) {
                entityMap.removeEntity(entity);
            }
        }
    }

}
