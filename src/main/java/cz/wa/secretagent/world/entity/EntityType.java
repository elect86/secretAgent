package cz.wa.secretagent.world.entity;

/**
 * Type of entity.
 * 
 * @author Ondrej Milenovsky
 */
public enum EntityType {
    /** item that can be picked up */
    ITEM,
    /** switch to change something in the map */
    SWITCH,
    /** usable entity (door, building) */
    USABLE,
    /** projectile that hurts agents (bullet, spike, fan, laser, explosion, ...) */
    PROJECTILE,
    /** moving platform, can */
    PLATFORM,
    /** some agent (enemy or player) */
    AGENT,
    /** explosion */
    EXPLOSION,
    /** laser */
    LASER,
}
