package cz.wa.secretagent.simulation;

import java.io.Serializable;

/**
 * Some controller for GameRunner.
 *
 * @author Ondrej Milenovsky
 */
public interface GameController extends Serializable {

    /**
     * Method called when the controller was not activate and will be active now.
     */
    void activate();

    /**
     * World moved, process input.
     * @param timeS
     */
    void processInput(double timeS);
}
