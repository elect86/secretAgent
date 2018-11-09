//package cz.wa.secretagent.game.action;
//
//import cz.wa.secretagent.game.sensor.SensorFactory;
//import cz.wa.secretagent.world.SAMWorld;
//import cz.wa.secretagent.world.entity.agent.AgentEntity;
//
///**
// * Action to control some agent.
// * Most actions are on/off, if an agent calls fire(true), then he fires until called fire(false).
// *
// * @author Ondrej Milenovsky
// */
//public abstract class AgentAction<E extends AgentEntity> {
//    private E entity;
//    private SAMWorld world;
//    private ActionFactory actionFactory;
//    private SensorFactory sensorFactory;
//
//    protected abstract void init();
//
//    public E getAgent() {
//        return entity;
//    }
//
//    void setEntity(E entity) {
//        this.entity = entity;
//    }
//
//    public SAMWorld getWorld() {
//        return world;
//    }
//
//    void setWorld(SAMWorld world) {
//        this.world = world;
//    }
//
//    public ActionFactory getActionFactory() {
//        return actionFactory;
//    }
//
//    void setActionFactory(ActionFactory actionFactory) {
//        this.actionFactory = actionFactory;
//    }
//
//    public SensorFactory getSensorFactory() {
//        return sensorFactory;
//    }
//
//    void setSensorFactory(SensorFactory sensorFactory) {
//        this.sensorFactory = sensorFactory;
//    }
//
//}
