package cz.wa.secretagent.world.weapon;

import java.awt.Color;
import java.io.Serializable;

import org.apache.commons.lang.Validate;
import cz.wa.secretagent.world.entity.projectile.ProjectileType;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import secretAgent.world.ObjectModel;

/**
 * Weapon immutable class containing parameters and model.
 * 
 * @author Ondrej Milenovsky
 */
public class Weapon implements Serializable {

    private static final long serialVersionUID = -738671197536970567L;

    /** name of the weapon */
    private final String name;
    /** weapon model name */
    private final String modelName;
    /** weapon item model name */
    private final String itemModelName;
    /** weapon ammo model name */
    private final String ammoModelName;
    /** false if an agent does not need to pick the weapon to use it (grenades, mines) */
    private final boolean weaponNeeded;
    /** projectile type */
    private final ProjectileType projectileType;
    /** projectile model name */
    private final String projectileModelName;
    /** damage of the projectile */
    private final double projectileDamage;
    /** speed of the projectile */
    private final double projectileSpeed;
    /** reload time between single projectiles, < 0 means fires as fast as the agent presses the trigger */
    private final double reloadTimeS;
    /** if > 0, then holding the trigger charges the shot, this value represents max charge in seconds */
    private final double chargeTimeS;
    /** true - when holding the trigger, can change the aim angle, releasing the trigger fires,
     * false - can aim to sides, up and down */
    private final boolean aimRotate;
    /** if can move while aiming */
    private final boolean aimMove;
    /** color of laser sights when aiming or null */
    private final Color laserSights;
    /** distance when enemies can hear shooting */
    private final double soundDist;
    /** max incaccuracy angle in degrees */
    private final double inaccuracyDg;
    /** projectile range */
    private final double range;
    /** number of fired projectiles */
    private final int projectileCount;

    private transient ObjectModel model;

    public Weapon(String name, String modelName, String itemModelName, String ammoModelName,
            boolean weaponNeeded, ProjectileType projectileType, String projectileModelName,
            double projectileDamage, double projectileSpeed, double reloadTimeS, double chargeTimeS,
            boolean aimRotate, boolean aimMove, Color laserSights, double soundDist, double inaccuracyDg,
            double range, int projectileCount) {
        Validate.notEmpty(name, "name is empty");
        Validate.notEmpty(modelName, "model is empty");
        Validate.notEmpty(itemModelName, "itemModel is empty");
        Validate.notEmpty(ammoModelName, "ammoModel is empty");
        Validate.notNull(projectileType, "projectileType is null");
        Validate.notEmpty(projectileModelName, "projectileModel is empty");
        this.name = name;
        this.modelName = modelName;
        this.itemModelName = itemModelName;
        this.ammoModelName = ammoModelName;
        this.weaponNeeded = weaponNeeded;
        this.projectileType = projectileType;
        this.projectileModelName = projectileModelName;
        this.projectileDamage = projectileDamage;
        this.projectileSpeed = projectileSpeed;
        this.reloadTimeS = reloadTimeS;
        this.chargeTimeS = chargeTimeS;
        this.aimRotate = aimRotate;
        this.aimMove = aimMove;
        this.laserSights = laserSights;
        this.soundDist = soundDist;
        this.inaccuracyDg = inaccuracyDg;
        this.range = range;
        this.projectileCount = projectileCount;
    }

    public Weapon(WeaponTmp w) {
        this(w.getName(), w.getModelName(), w.getItemModelName(), w.getAmmoModelName(), w.isWeaponNeeded(),
                w.getProjectileType(), w.getProjectileModelName(), w.getProjectileDamage(),
                w.getProjectileSpeed(), w.getReloadTimeS(), w.getChargeTimeS(), w.isAimRotate(),
                w.isAimMove(), w.getLaserSights(), w.getSoundDist(), w.getInaccuracyDg(), w.getRange(),
                w.getProjectileCount());
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

    public boolean isExplosive() {
        return (projectileType == ProjectileType.GRENADE) || (projectileType == ProjectileType.ROCKET)
                || (projectileType == ProjectileType.MINE);
    }

    /**
     * Link only weapon model
     * @param gri
     */
    public void linkModel(GraphicsInfo gri) {
        model = gri.getModel(modelName);
    }

    /**
     * @return if the weapon model is linked
     */
    public boolean hasLinkedModel() {
        return model != null;
    }

    public ObjectModel getModel() {
        return model;
    }

    public double getRange() {
        return range;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    @Override
    public String toString() {
        return "Weapon " + name;
    }
}
