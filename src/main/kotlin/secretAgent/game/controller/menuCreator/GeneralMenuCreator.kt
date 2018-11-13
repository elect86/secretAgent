package secretAgent.game.controller.menuCreator

import cz.wa.secretagent.worldinfo.WorldHolder
import secretAgent.game.GameSettings
import secretAgent.menu.DialogMenuBuilder
import secretAgent.menu.TextButtonDescriptor
import secretAgent.menu.window.GFrame
import secretAgent.menu.window.component.GButtonListener
import java.awt.Color
import java.io.Serializable

/**
 * Creates menu dialogs.
 *
 * @author Ondrej Milenovsky
 */
abstract class GeneralMenuCreator : Serializable {

    lateinit var dialogBuilder: DialogMenuBuilder
    lateinit var worldHolder: WorldHolder
    lateinit var gameSettings: GameSettings
    lateinit var frameColor: Color

    @delegate:Transient
    val settingsMenu: GFrame by lazy { createSettingsMenu() }
    @delegate:Transient
    val exitDialog: GFrame by lazy { createExitDialog() }

    fun createConfirmDialog(message: String, color: Color, yesListener: GButtonListener): GFrame {
        // buttons
        val buttons = arrayListOf(
                TextButtonDescriptor("Yes", yesListener),
                TextButtonDescriptor("No") { worldHolder.menuHolder.removeTopFrame() })

        // frame
        val closeListener: GButtonListener = { worldHolder.menuHolder.removeTopFrame() }

        return dialogBuilder.createDialog(message, buttons, color, closeListener, true)
    }

    private fun createSettingsMenu(): GFrame {
        val saveSettingsDialog = createSaveSettingsDialog()

        // buttons
        val buttons = arrayListOf(TextButtonDescriptor("Rebind keys") {
            // TODO rebind keys menu
        })

        // frame
        val closeListener: GButtonListener = {
            // TODO check if settings changed
            if (gameSettings.confirmDialogs) {
                saveSettingsDialog.selectedIndex = 0
                worldHolder.menuHolder.addFrame(saveSettingsDialog)
            } else {
                saveSettings()
            }
        }

        return dialogBuilder.createDialog("Settings", buttons, frameColor, closeListener, false)
    }

    private fun createExitDialog(): GFrame = createConfirmDialog("Exit the game?", frameColor) { exitGame() }

    protected fun exitGame() = System.exit(0)

    private fun createSaveSettingsDialog(): GFrame {
        // buttons
        val buttons = arrayListOf(
                TextButtonDescriptor("Yes") {
                    saveSettings()
                    worldHolder.menuHolder.removeTopFrame()
                },
                TextButtonDescriptor("No") {
                    worldHolder.menuHolder.removeTopFrame()
                    worldHolder.menuHolder.removeTopFrame()
                })

        // frame
        val closeListener: GButtonListener = { worldHolder.menuHolder.removeTopFrame() }

        return dialogBuilder.createDialog("Save settings?", buttons, frameColor, closeListener, true)
    }

    private fun saveSettings() {
        worldHolder.menuHolder.removeTopFrame()
        // TODO save settings
    }

    companion object {
        private const val serialVersionUID = -8429325182641380571L
    }
}
