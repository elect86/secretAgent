package secretAgent.game.controller

import secretAgent.GameController
import secretAgent.game.PlayerHolder

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

