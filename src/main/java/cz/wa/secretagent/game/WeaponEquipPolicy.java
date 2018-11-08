package cz.wa.secretagent.game;

/**
 * When equip a weapon when picked up.
 * 
 * @author Ondrej Milenovsky
 */
public enum WeaponEquipPolicy {
    /** never equip new weapon */
    NEVER,
    /** equip only if no weapon is active */
    NO_ACTIVE,
    /** equip if no weapon is active or no ammo for current weapon */
    NO_AMMO,
    /** equip if the weapon is not in inventory */
    NEW,
    /** always equip picked weapon */
    AWAYS
}
