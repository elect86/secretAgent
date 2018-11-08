package cz.wa.secretagent.simulation;

import java.io.Serializable;

/**
 * Some class that will move the world.
 *
 * @author Ondrej Milenovsky
 */
public interface GameSimulator extends Serializable {

    /**
     * Moves the world.
     * @param timeS time diff
     * @return false if stop further moving
     */
    boolean move(double timeS);
}
