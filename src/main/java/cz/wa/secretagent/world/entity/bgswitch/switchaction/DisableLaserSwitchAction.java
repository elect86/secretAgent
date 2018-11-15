//package cz.wa.secretagent.world.entity.bgswitch.switchaction;
//
//import cz.wa.secretagent.world.EntityMap;
//import cz.wa.secretagent.world.entity.laser.LaserEntity;
//import secretAgent.world.SamWorld;
//import secretAgent.world.entity.Entity;
//import secretAgent.world.entity.EntityType;
//import secretAgent.world.entity.agent.AgentEntity;
//
//import java.util.ArrayList;
//
///**
// * Disables laser.
// *
// * @author Ondrej Milenovsky
// */
//public class DisableLaserSwitchAction implements SwitchAction {
//
//    @Override
//    public void execute(AgentEntity agent, SamWorld world) {
//        EntityMap entityMap = world.getEntityMap();
//        for (Entity entity : new ArrayList<>(entityMap.getEntities(EntityType.LASER))) {
//            if (((LaserEntity) entity).isLevelLaser()) {
//                entityMap.removeEntity(entity);
//            }
//        }
//    }
//
//}
