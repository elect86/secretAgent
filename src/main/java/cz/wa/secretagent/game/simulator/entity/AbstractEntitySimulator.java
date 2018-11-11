//package cz.wa.secretagent.game.simulator.entity;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.worldinfo.WorldHolder;
//
///**
// * Simulates an entity by type. Has reference to the world.
// *
// * @author Ondrej Milenovsky
// */
//public abstract class AbstractEntitySimulator<E extends Entity> implements EntitySimulator<E> {
//    private static final long serialVersionUID = -1392493042338721293L;
//
//    protected WorldHolder worldHolder;
//
//    public WorldHolder getWorldHolder() {
//        return worldHolder;
//    }
//
//    @Required
//    public void setWorldHolder(WorldHolder worldHolder) {
//        this.worldHolder = worldHolder;
//    }
//}
