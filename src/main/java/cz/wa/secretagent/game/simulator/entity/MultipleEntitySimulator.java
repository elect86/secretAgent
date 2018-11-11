//package cz.wa.secretagent.game.simulator.entity;
//
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.EntityType2;
//
///**
// * Simulates entity according to its second type.
// * Has map of simulators for entity second type.
// *
// * @author Ondrej Milenovsky
// */
//public class MultipleEntitySimulator<E extends Entity> implements EntitySimulator<E> {
//    private static final long serialVersionUID = -4024151035446413564L;
//
//    private static final Logger logger = LoggerFactory.getLogger(MultipleEntitySimulator.class);
//
//    private Map<EntityType2, EntitySimulator<E>> simulators;
//
//    @Override
//    public boolean move(E entity, double timeS) {
//        EntityType2 type2 = entity.getSecondType();
//        if (simulators.containsKey(type2)) {
//            return simulators.get(type2).move(entity, timeS);
//        } else {
//            logger.warn("No simulator for " + entity.getType() + "." + type2);
//            return true;
//        }
//    }
//
//    public Map<EntityType2, EntitySimulator<E>> getSimulators() {
//        return simulators;
//    }
//
//    @Required
//    public void setSimulators(Map<EntityType2, EntitySimulator<E>> simulators) {
//        this.simulators = simulators;
//    }
//}
