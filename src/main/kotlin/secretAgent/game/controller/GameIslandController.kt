package secretAgent.game.controller

import cz.wa.secretagent.worldinfo.WorldHolder
import org.lwjgl.input.Keyboard
import secretAgent.game.action.ActionFactory
import secretAgent.game.action.AgentIslandAction
import secretAgent.game.controller.menuCreator.GameMenuCreator
import secretAgent.menu.window.GFrame
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.EntityYDirection

/**
 * Controller that works when the player is moving on island map. (moving, entering)
 *
 * @author Ondrej Milenovsky
 */
class GameIslandController : AbstractPlayerController() {

    lateinit var worldHolder: WorldHolder
    lateinit var gameMenuCreator: GameMenuCreator

    @Transient
    private var action: AgentIslandAction? = null

    @Transient
    private var menuFrame: GFrame? = null
    @Transient
    private var canActivate = false
    @Transient
    private var canMenu = false

    fun init(actionFactory: ActionFactory?) {
        if (actionFactory == null)
            action = null
        else {
            action = actionFactory.getAction(AgentIslandAction::class.java)
            if (menuFrame == null)
                menuFrame = gameMenuCreator.getIslandMainMenu()
        }
    }

    override fun activate() {
        canActivate = false
        canMenu = false
    }

    override fun processInput(timeS: Double) {
        if (action == null)
            return

        val keys = playerHolder.keys

        when {
            Keyboard.isKeyDown(keys.kLeft) -> action!!.moveX(EntityXDirection.LEFT)
            Keyboard.isKeyDown(keys.kRight) -> action!!.moveX(EntityXDirection.RIGHT)
            else -> action!!.moveX(EntityXDirection.NONE)
        }

        when {
            Keyboard.isKeyDown(keys.kUp) -> action!!.moveY(EntityYDirection.UP)
            Keyboard.isKeyDown(keys.kDown) -> action!!.moveY(EntityYDirection.DOWN)
            else -> action!!.moveY(EntityYDirection.NONE)
        }

        if (Keyboard.isKeyDown(keys.kActivate)) {
            if (canActivate) {
                action!!.activate(true)
                canActivate = false
            }
        } else {
            action!!.activate(false)
            canActivate = true
        }

        if (Keyboard.isKeyDown(keys.kMenu)) {
            if (canMenu) {
                worldHolder.world.isRunning = false
                menuFrame!!.selectedIndex = 0
                worldHolder.menuHolder.addFrame(menuFrame!!)
                canMenu = false
            }
        } else
            canMenu = true
    }

    companion object {
        private const val serialVersionUID = 943816424295202253L
    }
}