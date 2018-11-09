package cz.wa.secretagent.game.sensor;

import cz.wa.secretagent.world.entity.agent.AgentEntity;
import secretAgent.world.SamWorld;

/**
 * Sensor for an agent.
 * 
 * @author Ondrej Milenovsky
 */
public abstract class AgentSensor<E extends AgentEntity> {
    private E agent;
    private SamWorld world;
    private SensorFactory sensorFactory;

    protected abstract void init();

    public E getEntity() {
        return agent;
    }

    void setEntity(E agent) {
        this.agent = agent;
    }

    public SamWorld getWorld() {
        return world;
    }

    void setWorld(SamWorld world) {
        this.world = world;
    }

    public SensorFactory getSensorFactory() {
        return sensorFactory;
    }

    void setSensorFactory(SensorFactory sensorFactory) {
        this.sensorFactory = sensorFactory;
    }

}
