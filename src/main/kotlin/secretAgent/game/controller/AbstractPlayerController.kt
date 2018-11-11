package secretAgent.game.controller

import cz.wa.secretagent.game.PlayerHolder
import cz.wa.secretagent.simulation.GameController
import org.springframework.beans.factory.annotation.Required

/**
 * Abstract controller of player.
 *
 * @author Ondrej Milenovsky
 */
abstract class AbstractPlayerController : GameController {

    lateinit var playerHolder: PlayerHolder

    companion object {
        private const val serialVersionUID = 2995750420934559086L
    }
}

