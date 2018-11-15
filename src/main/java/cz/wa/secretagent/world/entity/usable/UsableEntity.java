//package cz.wa.secretagent.world.entity.usable;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import secretAgent.world.ObjectModel;
//import secretAgent.world.entity.Entity;
//import secretAgent.world.entity.EntityType;
//
///**
// * Some usable entity that creates wall (door, building), drawn behind player and is a ghost.
// *
// * @author Ondrej Milenovsky
// */
//public abstract class UsableEntity extends Entity {
//
//    /** false only if is single use and already activated */
//    private final boolean wall;
//    private boolean active;
//
//    public UsableEntity(ObjectModel model, Vector2D pos, boolean wall) {
//        super(model, pos);
//        this.wall = wall;
//        active = true;
//    }
//
//    @Override
//    public EntityType getType() {
//        return EntityType.USABLE;
//    }
//
//    public boolean isWall() {
//        return wall;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//
//    @Override
//    public abstract UsableType getSecondType();
//}
