//package cz.wa.secretagent.game.simulator.entity.platform;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
//import cz.wa.secretagent.game.utils.EntitiesFinder;
//import cz.wa.secretagent.game.utils.EntityObserver;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.platform.PlatformEntity;
//import cz.wa.secretagent.world.entity.platform.PlatformLift;
//import cz.wa.wautils.math.Rectangle2D;
//
///**
// * Simulates a platform.
// * Lift moves with things standing on it.
// *
// * @author Ondrej Milenovsky
// */
//public class LiftEntitySimulator extends AbstractEntitySimulator<PlatformEntity> {
//
//    private static final long serialVersionUID = -8448655199600104508L;
//
//    @Override
//    public boolean move(PlatformEntity entity, double timeS) {
//        moveLift((PlatformLift) entity, timeS);
//        return true;
//    }
//
//    /**
//     * Lift moves by itself and moves entities standing on it.
//     */
//    private void moveLift(PlatformLift lift, double timeS) {
//        // get entities standing on it
//        List<Entity> carying = new EntitiesFinder(worldHolder.getWorld()).findEntitiesToCarry(lift);
//
//        // move
//        Vector2D speed = lift.getSpeed();
//        if (lift.isMovingForward()) {
//            speed = speed.scalarMultiply(timeS);
//        } else {
//            speed = speed.scalarMultiply(-timeS);
//        }
//        Vector2D oldPos = lift.getPos();
//        lift.setPos(oldPos.add(speed));
//
//        // check if hit something
//        EntityObserver posSensor = new EntityObserver(lift, worldHolder.getWorld());
//        Rectangle2D objBounds = posSensor.getTouchingSolidObject();
//        if (objBounds != null) {
//            // reverse direction
//            lift.setMovingForward(!lift.isMovingForward());
//            // move a little back
//            double dist = lift.getSizeBounds().move(lift.getPos()).getIntersectingDist(objBounds);
//            speed = speed.scalarMultiply(-dist / speed.getNorm());
//            lift.setPos(lift.getPos().add(speed));
//        }
//
//        // move entities on it
//        speed = lift.getPos().subtract(oldPos);
//        for (Entity e2 : carying) {
//            e2.setPos(e2.getPos().add(speed));
//        }
//    }
//
//}
