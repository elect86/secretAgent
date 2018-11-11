package secretAgent.game.controller

import cz.wa.secretagent.simulation.GameController
import cz.wa.secretagent.utils.lwjgl.FullScreenSwitcher
import cz.wa.secretagent.worldinfo.WorldHolder
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.lwjgl.input.Keyboard
import org.springframework.beans.factory.annotation.Required

/**
 * Main game controller holding other controllers.
 *
 * @author Ondrej Milenovsky
 */
class GeneralController : GameController {

    lateinit var worldHolder: WorldHolder

    lateinit var levelController: GameController
    lateinit var islandController: GameController
    lateinit var menuController: GameController

    lateinit var fullScreenSwitcher: FullScreenSwitcher

    private var activeController: GameController? = null

    private val posCheatValue = 32

    override fun activate() {} // empty

    override fun processInput(timeS: Double) {
        if (!processGeneralInput(timeS))
            return
        val world = worldHolder.world
        if (worldHolder.menuHolder.isMenuActive) {
            if (activeController !== menuController) {
                menuController.activate()
                activeController = menuController
            }
            menuController.processInput(timeS)
        } else if (world.isRunning) {
            processCheats(timeS)
            if (world.isIsland) {
                if (activeController !== islandController) {
                    islandController.activate()
                    activeController = islandController
                }
                islandController.processInput(timeS)
            } else {
                if (activeController !== levelController) {
                    levelController.activate()
                    activeController = levelController
                }
                levelController.processInput(timeS)
            }
        }
    }

    private fun processGeneralInput(timeS: Double): Boolean {
        // exit
        if (Keyboard.isKeyDown(Keyboard.KEY_F4) && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) {
            System.exit(0)
            return false
        }
        // full screen
        if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) {
            worldHolder.world.isRunning = false
            fullScreenSwitcher.isFullScreen = !fullScreenSwitcher.isFullScreen
            worldHolder.world.isRunning = true
            return false
        }
        return true
    }

    private fun processCheats(timeS: Double) {
        if (posCheatValue == 0)
            return
        // move the agent
        val agent = worldHolder.playerHolder.agent
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4))
            agent.pos = agent.pos.add(Vector2D((-posCheatValue).toDouble(), 0.0))
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))
            agent.pos = agent.pos.add(Vector2D(posCheatValue.toDouble(), 0.0))
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))
            agent.pos = agent.pos.add(Vector2D(0.0, (-posCheatValue).toDouble()))
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5))
            agent.pos = agent.pos.add(Vector2D(0.0, posCheatValue.toDouble()))
    }

    companion object {
        private val serialVersionUID = -1763109597070594041L
    }
}