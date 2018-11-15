package secretAgent.world.entity.agent

import secretAgent.world.entity.EntityType2
import java.io.Serializable

/**
 * Current displayable action of agent.
 *
 * @author Ondrej Milenovsky
 */
enum class AgentAction {
    STAY,
    MOVE,
    JUMP,
    DEATH
}


/**
 * Types of agents.
 *
 * @author Ondrej Milenovsky
 */
enum class AgentType : EntityType2 {
    /** player or human enemy  */
    HUMAN,
    /** some robot (2 tiles tall)  */
    ROBOT,
    /** static not moving or rotating gun  */
    TURRET
}

/**
 * Team of an agent.
 *
 * @author Ondrej Milenovsky
 */
class Team(val name: String) : Serializable {

    override fun hashCode() = 31 + name.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        return other is Team && name == other.name
    }

    override fun toString() = "Team $name"

    companion object {
        private const val serialVersionUID = -4859957595199661132L
    }
}