//package cz.wa.secretagent.game.simulator.entity.projectile;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
//import cz.wa.secretagent.game.utils.ProjectileMover;
//import cz.wa.secretagent.world.entity.projectile.BulletFallProjectile;
//
///**
// * Simulates bullet that falls to the ground.
// * Bullet flies single direction and falls, expires after reached its max range.
// * Can hit walls or agents.
// *
// * @author Ondrej Milenovsky
// */
//public class BulletFallEntitySimulator extends AbstractEntitySimulator<BulletFallProjectile> {
//
//    private static final long serialVersionUID = -8779107845319125341L;
//
//    private double fallG;
//
//    @Override
//    public boolean move(BulletFallProjectile entity, double timeS) {
//        if (new ProjectileMover(worldHolder.getWorld(), entity, timeS).move()) {
//            // if the bullet is still alive, increase y speed
//            entity.setSpeed(entity.getSpeed().add(new Vector2D(0, fallG * timeS)));
//        }
//        return true;
//    }
//
//    public double getFallG() {
//        return fallG;
//    }
//
//    @Required
//    public void setFallG(double fallG) {
//        this.fallG = fallG;
//    }
//
//}
