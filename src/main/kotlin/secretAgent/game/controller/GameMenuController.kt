package secretAgent.game.controller

import cz.wa.secretagent.simulation.GameController
import cz.wa.secretagent.worldinfo.WorldHolder
import org.lwjgl.input.Keyboard
import org.springframework.beans.factory.annotation.Required

/**
 * Controls all menus. (move cursor, enter, back)
 *
 * @author Ondrej Milenovsky
 */
class GameMenuController : GameController {

    lateinit var worldHolder: WorldHolder
    @Transient
    private var canActivate = false

    override fun activate() {
        canActivate = false
    }

    override fun processInput(timeS: Double) {
        val menuKeys = worldHolder.menuHolder.keys!!

        when {
            Keyboard.isKeyDown(menuKeys.kBack) -> if (canActivate) {
                val frame = worldHolder.menuHolder.topFrame
                frame!!.callClose()
                canActivate = false
            }
            Keyboard.isKeyDown(menuKeys.kUp) -> if (canActivate) {
                val frame = worldHolder.menuHolder.topFrame
                frame!!.selectPrevComponent()
                canActivate = false
            }
            Keyboard.isKeyDown(menuKeys.kDown) -> if (canActivate) {
                val frame = worldHolder.menuHolder.topFrame
                frame!!.selectNextComponent()
                canActivate = false
            }
            Keyboard.isKeyDown(menuKeys.kEnter) -> if (canActivate) {
                val frame = worldHolder.menuHolder.topFrame
                val selected = frame!!.selectedComponent
                selected?.activate()
                canActivate = false
            }
            Keyboard.isKeyDown(menuKeys.kLeft) -> if (canActivate) {
                val frame = worldHolder.menuHolder.topFrame
                val selected = frame!!.selectedComponent
                if (selected?.changeLeft() == false)
                    frame.selectPrevComponent()
                canActivate = false
            }
            Keyboard.isKeyDown(menuKeys.kRight) -> if (canActivate) {
                val frame = worldHolder.menuHolder.topFrame
                val selected = frame!!.selectedComponent
                if (selected?.changeRight() == false)
                    frame.selectNextComponent()
                canActivate = false
            }
            else -> canActivate = true
        }
    }

    companion object {
        private const val serialVersionUID = -8439774283488072370L
    }
}