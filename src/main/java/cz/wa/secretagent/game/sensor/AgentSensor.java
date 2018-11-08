package cz.wa.secretagent.game.sensor;

import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.agent.AgentEntity;

/**
 * Sensor for an agent.
 * 
 * @author Ondrej Milenovsky
 */
public abstract class AgentSensor<E extends AgentEntity> {
    private E agent;
    private SAMWorld world;
    private SensorFactory sensorFactory;

    protected abstract void init();

    public E getEntity() {
        return agent;
    }

    void setEntity(E agent) {
        this.agent = agent;
    }

    public SAMWorld getWorld() {
        return world;
    }

    void setWorld(SAMWorld world) {
        this.world = world;
    }

    public SensorFactory getSensorFactory() {
        return sensorFactory;
    }

    void setSensorFactory(SensorFactory sensorFactory) {
        this.sensorFactory = sensorFactory;
    }

}
