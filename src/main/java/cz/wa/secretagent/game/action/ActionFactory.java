//package cz.wa.secretagent.game.action;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import cz.wa.secretagent.game.sensor.SensorFactory;
//import cz.wa.secretagent.game.starter.MapStarter;
//import cz.wa.secretagent.world.entity.agent.AgentEntity;
//import secretAgent.game.ProjectileFactory;
//import secretAgent.game.action.AgentAction;
//import secretAgent.world.SamWorld;
//
///**
// * Creates actions with caching.
// *
// * @author Ondrej Milenovsky
// */
//public class ActionFactory {
//    private final AgentEntity agent;
//    private final MapStarter mapStarter;
//    private final ProjectileFactory projectileFactory;
//    private final SensorFactory sensorFactory;
//    private final SamWorld world;
//
//    private final Map<Object, AgentAction<AgentEntity>> cache;
//
//    public ActionFactory(AgentEntity agent, SamWorld world, SensorFactory sensorFactory,
//            MapStarter mapStarter, ProjectileFactory projectileFactory) {
//        this.agent = agent;
//        this.world = world;
//        this.sensorFactory = sensorFactory;
//        this.mapStarter = mapStarter;
//        this.projectileFactory = projectileFactory;
//        cache = new HashMap<Object, AgentAction<AgentEntity>>();
//    }
//
//    @SuppressWarnings("unchecked")
//    public <C> C getAction(Class<C> clazz) {
//        if (cache.containsKey(clazz)) {
//            return (C) cache.get(clazz);
//        } else {
//            C instance;
//            try {
//                instance = clazz.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            AgentAction<AgentEntity> object = (AgentAction<AgentEntity>) instance;
//            object.setAgent(agent);
//            object.setWorld(world);
//            object.setActionFactory(this);
//            object.setSensorFactory(sensorFactory);
//            object.init();
//            cache.put(clazz, object);
//            return instance;
//        }
//    }
//
//    public MapStarter getMapStarter() {
//        return mapStarter;
//    }
//
//    public ProjectileFactory getProjectileFactory() {
//        return projectileFactory;
//    }
//}
