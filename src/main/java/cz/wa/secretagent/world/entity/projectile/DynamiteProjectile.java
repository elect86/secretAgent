//package cz.wa.secretagent.world.entity.projectile;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import secretAgent.world.ObjectModel;
//
///**
// * Dynamite that explodes and opens exit door.
// *
// * @author Ondrej Milenovsky
// */
//public class DynamiteProjectile extends ProjectileEntity {
//
//    /** remaining fuse time in seconds */
//    private double remainingTimeS;
//
//    public DynamiteProjectile(ObjectModel model, Vector2D pos, double fuseTimeS) {
//        super(model, pos, null);
//        setStaticPos(true);
//        remainingTimeS = fuseTimeS;
//    }
//
//    @Override
//    public ProjectileType getSecondType() {
//        return ProjectileType.DYNAMITE;
//    }
//
//    public double getRemainingTimeS() {
//        return remainingTimeS;
//    }
//
//    public void addTime(double timeS) {
//        remainingTimeS -= timeS;
//    }
//
//}
