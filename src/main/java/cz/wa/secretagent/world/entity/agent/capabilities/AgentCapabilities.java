//package cz.wa.secretagent.world.entity.agent.capabilities;
//
//import java.io.Serializable;
//
///**
// * Capabilities of an agent's actions, immutable implementation.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentCapabilities implements Serializable {
//    private static final long serialVersionUID = -6591972558599041031L;
//
//    private final double maxHealth;
//    /** max move speed */
//    private final double maxSpeed;
//    /** jump strength, 0 = cannot jump */
//    private final double jumpStrength;
//    /** jump time, 0 = cannot jump */
//    private final double jumpTimeS;
//    /** if can aim up and down */
//    private final boolean canAim;
//    /** if can activate doors, defuse mines, ... */
//    private final boolean canActivate;
//    /** limits for inventory */
//    private final InventoryLimits inventoryLimits;
//
//    public AgentCapabilities(AgentCapabilitiesTmp c) {
//        this(c.getMaxHealth(), c.getMaxSpeed(), c.getJumpStrength(), c.getJumpTimeS(), c.isCanAim(),
//                c.isCanActivate(), c.getInventoryLimits());
//    }
//
//    /**
//     * @param maxSpeed max move speed [pixel/s]
//     * @param jumpStrength jump strength
//     * @param jumpTimeS max jump duration [s]
//     * @param canAim can aim weapon
//     * @param canActivate can activate
//     * @param inventoryLimits inventory limits, if null, then cannot pick anything
//     */
//    public AgentCapabilities(double maxHealth, double maxSpeed, double jumpStrength, double jumpTimeS,
//            boolean canAim, boolean canActivate, InventoryLimits inventoryLimits) {
//        this.maxHealth = maxHealth;
//        this.maxSpeed = maxSpeed;
//        this.jumpStrength = jumpStrength;
//        this.jumpTimeS = jumpTimeS;
//        this.canAim = canAim;
//        this.canActivate = canActivate;
//        if (inventoryLimits != null) {
//            this.inventoryLimits = inventoryLimits;
//        } else {
//            this.inventoryLimits = InventoryLimits.NOTHING;
//        }
//    }
//
//    public double getMaxHealth() {
//        return maxHealth;
//    }
//
//    public double getMaxSpeed() {
//        return maxSpeed;
//    }
//
//    public double getJumpStrength() {
//        return jumpStrength;
//    }
//
//    public boolean canAim() {
//        return canAim;
//    }
//
//    public boolean canActivate() {
//        return canActivate;
//    }
//
//    public double getJumpTimeS() {
//        return jumpTimeS;
//    }
//
//    public InventoryLimits getInventoryLimits() {
//        return inventoryLimits;
//    }
//}
