package cz.wa.secretagent.game.simulator.entity;

import java.io.Serializable;

import cz.wa.secretagent.world.entity.Entity;

/**
 * Simulates an entity by type.
 * 
 * @author Ondrej Milenovsky
 */
public interface EntitySimulator<E extends Entity> extends Serializable {
    /**
     * @param entity entity to simulate
     * @param timeS time diff in seconds
     * @return false if stop further moving of other entities
     */
    boolean move(E entity, double timeS);
}
