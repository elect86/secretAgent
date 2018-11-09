package cz.wa.secretagent.world.entity.bgswitch.switchaction;

import cz.wa.secretagent.world.entity.agent.AgentEntity;
import secretAgent.world.SamWorld;

/**
 * Action of a switch. 
 * 
 * @author Ondrej Milenovsky
 */
public interface SwitchAction {
    /**
     * Execute the action.
     * @param agent
     * @param world
     */
    void execute(AgentEntity agent, SamWorld world);

}
