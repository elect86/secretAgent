package cz.wa.secretagent.world.entity.agent;

import cz.wa.secretagent.world.entity.EntityType2;

/**
 * Types of agents. 
 * 
 * @author Ondrej Milenovsky
 */
public enum AgentType implements EntityType2 {
    /** player or human enemy */
    HUMAN,
    /** some robot (2 tiles tall) */
    ROBOT,
    /** static not moving or rotating gun */
    TURRET,
}
