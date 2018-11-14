//package cz.wa.secretagent.world.entity.agent.capabilities;
//
///**
// * Capabilities of an agent's actions, mutable temporary implementation used in spring, cannot be used by agent.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentCapabilitiesTmp {
//    private double maxHealth;
//    /** max move speed */
//    private double maxSpeed;
//    /** jump strength, 0 = cannot jump */
//    private double jumpStrength;
//    /** jump time in seconds, 0 = cannot jump */
//    private double jumpTimeS;
//    /** if can aim up and down */
//    private boolean canAim;
//    /** if can activate doors, defuse mines, ... */
//    private boolean canActivate;
//    /** inventory limits, null if cannot pick up items */
//    private InventoryLimits inventoryLimits;
//
//    public AgentCapabilitiesTmp() {
//        maxHealth = 0;
//        maxSpeed = 0;
//        jumpStrength = 0;
//        jumpTimeS = 0;
//        canAim = false;
//        canActivate = false;
//        inventoryLimits = null;
//    }
//
//    public double getMaxHealth() {
//        return maxHealth;
//    }
//
//    public void setMaxHealth(double maxHealth) {
//        this.maxHealth = maxHealth;
//    }
//
//    public double getMaxSpeed() {
//        return maxSpeed;
//    }
//
//    public void setMaxSpeed(double maxSpeed) {
//        this.maxSpeed = maxSpeed;
//    }
//
//    public double getJumpStrength() {
//        return jumpStrength;
//    }
//
//    public void setJumpStrength(double jumpStrength) {
//        this.jumpStrength = jumpStrength;
//    }
//
//    public double getJumpTimeS() {
//        return jumpTimeS;
//    }
//
//    public void setJumpTimeS(double jumpTimeS) {
//        this.jumpTimeS = jumpTimeS;
//    }
//
//    public boolean isCanAim() {
//        return canAim;
//    }
//
//    public void setCanAim(boolean canAim) {
//        this.canAim = canAim;
//    }
//
//    public boolean isCanActivate() {
//        return canActivate;
//    }
//
//    public void setCanActivate(boolean canActivate) {
//        this.canActivate = canActivate;
//    }
//
//    public InventoryLimits getInventoryLimits() {
//        return inventoryLimits;
//    }
//
//    public void setInventoryLimits(InventoryLimits inventoryLimits) {
//        this.inventoryLimits = inventoryLimits;
//    }
//
//}
