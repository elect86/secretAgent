package secretAgent.game.controller.menuCreator

import cz.wa.secretagent.game.starter.MapStarter
import cz.wa.secretagent.menu.builder.TextButtonDescriptor
import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.menu.window.component.selectable.GButtonListener
import org.springframework.beans.factory.annotation.Required
import java.io.Serializable
import java.util.ArrayList

/**
 * Methods for creating menus in game. Caches all frames.
 *
 * @author Ondrej Milenovsky
 */
class GameMenuCreator : GeneralMenuCreator(), Serializable {

    lateinit var mapStarter: MapStarter

    @Transient
    private lateinit var quitToMenuDialog: GFrame
    @delegate:Transient
    val levelMainMenu: GFrame by lazy { createLevelMainMenu() }
    @delegate:Transient
    val islandMainMenu: GFrame by lazy { createIslandMainMenu() }

    private fun init() {
        if (!::quitToMenuDialog.isInitialized)
            quitToMenuDialog = createQuitToMenuDialog()
    }

    private fun createLevelMainMenu(): GFrame {
        init()
        val returnToIslandDialog = createReturnToIslandDialog()

        // buttons
        val buttons = arrayListOf(
                createSettingsButton(),
                TextButtonDescriptor("Return to island", GButtonListener {
                    if (gameSettings.confirmDialogs) {
                        returnToIslandDialog.selectedIndex = 0
                        worldHolder.menuHolder.addFrame(returnToIslandDialog)
                    } else
                        quitToIsland()
                }),
                createQuitToMenuButton(),
                createExitButton())

        // frame
        val closeListener = GButtonListener {
            worldHolder.menuHolder.removeTopFrame()
            worldHolder.world.isRunning = true
        }

        return dialogBuilder.createDialog("Game paused", buttons, frameColor, closeListener, false)
    }

    private fun quitToIsland() {
        mapStarter.startIslandMap()
        worldHolder.menuHolder.removeAllFrames()
    }

    private fun createExitButton(): TextButtonDescriptor {
        return TextButtonDescriptor("Exit the game", GButtonListener {
            if (gameSettings.confirmDialogs) {
                val exitDialog = exitDialog
                exitDialog.selectedIndex = 0
                worldHolder.menuHolder.addFrame(exitDialog)
            } else
                exitGame()
        })
    }

    private fun createQuitToMenuButton(): TextButtonDescriptor {
        return TextButtonDescriptor("Quit to main menu", GButtonListener {
            if (gameSettings.confirmDialogs) {
                quitToMenuDialog.selectedIndex = 0
                worldHolder.menuHolder.addFrame(quitToMenuDialog)
            } else
                quitToMenu()
        })
    }

    private fun quitToMenu() {
        worldHolder.menuHolder.removeAllFrames()
        mapStarter.startMainMenu()
    }

    private fun createSettingsButton() = TextButtonDescriptor("Settings", GButtonListener {
        val settingsMenu = settingsMenu
        settingsMenu.selectedIndex = 0
        worldHolder.menuHolder.addFrame(settingsMenu)
    })

    private fun createIslandMainMenu(): GFrame {
        init()
        // buttons
        val buttons = arrayListOf(
                createSettingsButton(),
                createQuitToMenuButton(),
                createExitButton())
        // frame
        val closeListener = GButtonListener {
            worldHolder.menuHolder.removeTopFrame()
            worldHolder.world.isRunning = true
        }

        return dialogBuilder.createDialog("Game paused", buttons, frameColor, closeListener, false)
    }

    private fun createQuitToMenuDialog(): GFrame = createConfirmDialog("Quit to main menu?", GButtonListener{ quitToMenu() }, frameColor)

    private fun createReturnToIslandDialog(): GFrame = createConfirmDialog("Return to island map?", GButtonListener{ quitToIsland() }, frameColor)

    companion object {
        private const val serialVersionUID = 114350147422291071L
    }
}