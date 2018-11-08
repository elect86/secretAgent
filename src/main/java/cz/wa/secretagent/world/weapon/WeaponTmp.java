package cz.wa.secretagent.world.weapon;

import java.awt.Color;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.world.entity.projectile.ProjectileType;

/**
 * Temporary mutable class used by spring to define the weapon.
 * 
 * @author Ondrej Milenovsky
 */
public class WeaponTmp {

    private String name;
    private String modelName;
    private String itemModelName;
    private String ammoModelName;
    private boolean weaponNeeded;
    private ProjectileType projectileType;
    private String projectileModelName;
    private double projectileDamage;
    private double projectileSpeed;
    private double reloadTimeS;
    private double chargeTimeS;
    private boolean aimRotate;
    private boolean aimMove;
    private Color laserSights;
    private double soundDist;
    private double inaccuracyDg;
    private double range;
    private int projectileCount;

    public WeaponTmp() {
        // default values, the others are needed
        weaponNeeded = true;
        chargeTimeS = 0;
        aimRotate = false;
        aimMove = true;
        laserSights = null;
        inaccuracyDg = 0;
        projectileCount = 1;
    }

    public String getName() {
        return name;
    }

    public String getModelName() {
        return modelName;
    }

    public String getItemModelName() {
        return itemModelName;
    }

    public String getAmmoModelName() {
        return ammoModelName;
    }

    public boolean isWeaponNeeded() {
        return weaponNeeded;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public String getProjectileModelName() {
        return projectileModelName;
    }

    public double getProjectileDamage() {
        return projectileDamage;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    public double getReloadTimeS() {
        return reloadTimeS;
    }

    public double getChargeTimeS() {
        return chargeTimeS;
    }

    public boolean isAimRotate() {
        return aimRotate;
    }

    public boolean isAimMove() {
        return aimMove;
    }

    public Color getLaserSights() {
        return laserSights;
    }

    public double getSoundDist() {
        return soundDist;
    }

    public double getInaccuracyDg() {
        return inaccuracyDg;
    }

    public double getRange() {
        return range;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    @Required
    public void setName(String name) {
        this.name = name;
    }

    @Required
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Required
    public void setItemModelName(String itemModelName) {
        this.itemModelName = itemModelName;
    }

    @Required
    public void setAmmoModelName(String ammoModelName) {
        this.ammoModelName = ammoModelName;
    }

    @Required
    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    @Required
    public void setProjectileModelName(String projectileModelName) {
        this.projectileModelName = projectileModelName;
    }

    @Required
    public void setProjectileDamage(double projectileDamage) {
        this.projectileDamage = projectileDamage;
    }

    @Required
    public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    @Required
    public void setReloadTimeS(double reloadTimeS) {
        this.reloadTimeS = reloadTimeS;
    }

    @Required
    public void setSoundDist(double soundDist) {
        this.soundDist = soundDist;
    }

    @Required
    public void setRange(double rangeT) {
        this.range = rangeT;
    }

    public void setWeaponNeeded(boolean weaponNeeded) {
        this.weaponNeeded = weaponNeeded;
    }

    public void setChargeTimeS(double chargeTimeS) {
        this.chargeTimeS = chargeTimeS;
    }

    public void setAimRotate(boolean aimRotate) {
        this.aimRotate = aimRotate;
    }

    public void setAimMove(boolean aimMove) {
        this.aimMove = aimMove;
    }

    public void setLaserSights(Color laserSights) {
        this.laserSights = laserSights;
    }

    public void setInaccuracyDg(double inaccuracyDg) {
        this.inaccuracyDg = inaccuracyDg;
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

}
