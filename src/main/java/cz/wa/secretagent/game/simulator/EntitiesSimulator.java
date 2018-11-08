package cz.wa.secretagent.game.simulator;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.simulator.entity.EntitySimulator;
import cz.wa.secretagent.simulation.GameSimulator;
import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityOrder;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * The main game simulator, simulates only world, not menu.
 * The only things to simulate are entities. (tiles are animated by world time)
 * The entities are processed by order of the simulators.
 * 
 * @author Ondrej Milenovsky
 */
public class EntitiesSimulator implements GameSimulator {

    private static final long serialVersionUID = 5390556751042901539L;

    private WorldHolder worldHolder;
    private Map<EntityType, EntitySimulator<Entity>> simulators;
    private EntityOrder entityOrder;
    private GameSimulator cameraSimulator;

    @Override
    public boolean move(double timeS) {
        if (worldHolder.getMenuHolder().isMenuActive()) {
            return true;
        }
        EntityMap entityMap = worldHolder.getWorld().getEntityMap();
        for (Entity entity : entityOrder.getOrderedEntities(entityMap)) {
            EntityType type = entity.getEntityType();
            // something could have removed the entity, so check, if it is still there
            if (entityMap.getEntities(type).contains(entity)) {
                if (!simulators.get(type).move(entity, timeS)) {
                    return false;
                }
            }
        }
        cameraSimulator.move(timeS);
        return true;
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

    public Map<EntityType, EntitySimulator<Entity>> getSimulators() {
        return simulators;
    }

    /**
     * @param simulators simulators for entity types, the entities are simulated in this order
     */
    @Required
    public void setSimulators(Map<EntityType, EntitySimulator<Entity>> simulators) {
        this.simulators = simulators;
        entityOrder = new EntityOrder(new ArrayList<EntityType>(simulators.keySet()));
    }

    public GameSimulator getCameraSimulator() {
        return cameraSimulator;
    }

    @Required
    public void setCameraSimulator(GameSimulator cameraSimulator) {
        this.cameraSimulator = cameraSimulator;
    }
}
