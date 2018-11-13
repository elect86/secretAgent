package secretAgent.game.controller.menuCreator

import secretAgent.game.starter.MapStarter
import secretAgent.menu.TextButtonDescriptor
import secretAgent.menu.window.GFrame
import secretAgent.menu.window.component.GButtonListener
import java.io.Serializable

/**
 * Methods for creating menus in game. Caches all frames.
 *
 * @author Ondrej Milenovsky
 */
class GameMenuCreator : GeneralMenuCreator(), Serializable {

    lateinit var mapStarter: MapStarter

    @Transient
    private lateinit var quitToMenuDialog: GFrame
    @Transient
    private lateinit var levelMainMenu: GFrame
    @Transient
    private lateinit var islandMainMenu: GFrame

    fun getLevelMainMenu(): GFrame { // TODO lazy
        if(!::levelMainMenu.isInitialized)
            levelMainMenu = createLevelMainMenu()
        return levelMainMenu
    }

    fun getIslandMainMenu(): GFrame { // TODO lazy
        if(!::islandMainMenu.isInitialized)
            islandMainMenu = createIslandMainMenu()
        return islandMainMenu
    }

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
                TextButtonDescriptor("Return to island") {
                    if (gameSettings.confirmDialogs) {
                        returnToIslandDialog.selectedIndex = 0
                        worldHolder.menuHolder.addFrame(returnToIslandDialog)
                    } else
                        quitToIsland()
                },
                createQuitToMenuButton(),
                createExitButton())

        // frame
        val closeListener: GButtonListener = {
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
        return TextButtonDescriptor("Exit the game") {
            if (gameSettings.confirmDialogs) {
                val exitDialog = exitDialog
                exitDialog.selectedIndex = 0
                worldHolder.menuHolder.addFrame(exitDialog)
            } else
                exitGame()
        }
    }

    private fun createQuitToMenuButton(): TextButtonDescriptor {
        return TextButtonDescriptor("Quit to main menu") {
            if (gameSettings.confirmDialogs) {
                quitToMenuDialog.selectedIndex = 0
                worldHolder.menuHolder.addFrame(quitToMenuDialog)
            } else
                quitToMenu()
        }
    }

    private fun quitToMenu() {
        worldHolder.menuHolder.removeAllFrames()
        mapStarter.startMainMenu()
    }

    private fun createSettingsButton() = TextButtonDescriptor("Settings") {
        val settingsMenu = settingsMenu
        settingsMenu.selectedIndex = 0
        worldHolder.menuHolder.addFrame(settingsMenu)
    }

    private fun createIslandMainMenu(): GFrame {
        init()
        // buttons
        val buttons = arrayListOf(
                createSettingsButton(),
                createQuitToMenuButton(),
                createExitButton())
        // frame
        val closeListener: GButtonListener = {
            worldHolder.menuHolder.removeTopFrame()
            worldHolder.world.isRunning = true
        }

        return dialogBuilder.createDialog("Game paused", buttons, frameColor, closeListener, false)
    }

    private fun createQuitToMenuDialog(): GFrame = createConfirmDialog("Quit to main menu?", frameColor) { quitToMenu() }

    private fun createReturnToIslandDialog(): GFrame = createConfirmDialog("Return to island map?", frameColor) { quitToIsland() }

    companion object {
        private const val serialVersionUID = 114350147422291071L
    }
}