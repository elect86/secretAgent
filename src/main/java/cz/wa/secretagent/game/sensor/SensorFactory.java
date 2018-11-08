package cz.wa.secretagent.game.sensor;

import java.util.HashMap;
import java.util.Map;

import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.agent.AgentEntity;

/**
 * Factory creating sensors.
 * 
 * @author Ondrej Milenovsky
 */
public class SensorFactory {
    private final AgentEntity agent;
    private final SAMWorld world;
    private final Map<Object, AgentSensor<AgentEntity>> cache;

    public SensorFactory(AgentEntity agent, SAMWorld world) {
        this.agent = agent;
        this.world = world;
        cache = new HashMap<Object, AgentSensor<AgentEntity>>();
    }

    @SuppressWarnings("unchecked")
    public <C> C getSensor(Class<C> clazz) {
        if (cache.containsKey(clazz)) {
            return (C) cache.get(clazz);
        } else {
            C instance;
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            AgentSensor<AgentEntity> object = (AgentSensor<AgentEntity>) instance;
            object.setEntity(agent);
            object.setWorld(world);
            object.init();
            cache.put(clazz, object);
            return instance;
        }
    }
}
