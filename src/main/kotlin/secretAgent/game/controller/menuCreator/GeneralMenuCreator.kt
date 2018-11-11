package secretAgent.game.controller.menuCreator

import cz.wa.secretagent.game.GameSettings
import cz.wa.secretagent.menu.builder.DialogMenuBuilder
import cz.wa.secretagent.menu.builder.TextButtonDescriptor
import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.menu.window.component.selectable.GButtonListener
import cz.wa.secretagent.worldinfo.WorldHolder
import org.springframework.beans.factory.annotation.Required
import java.awt.Color
import java.io.Serializable
import java.util.ArrayList

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

    fun createConfirmDialog(message: String, yesListener: GButtonListener, color: Color): GFrame {
        // buttons
        val buttons = arrayListOf(
                TextButtonDescriptor("Yes", yesListener),
                TextButtonDescriptor("No", GButtonListener { worldHolder.menuHolder.removeTopFrame() }))

        // frame
        val closeListener = GButtonListener { worldHolder.menuHolder.removeTopFrame() }

        return dialogBuilder.createDialog(message, buttons, color, closeListener, true)
    }

    private fun createSettingsMenu(): GFrame {
        val saveSettingsDialog = createSaveSettingsDialog()

        // buttons
        val buttons = arrayListOf(TextButtonDescriptor("Rebind keys", GButtonListener {
            // TODO rebind keys menu
        }))

        // frame
        val closeListener = GButtonListener {
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

    private fun createExitDialog(): GFrame {
        return createConfirmDialog("Exit the game?", GButtonListener { exitGame() }, frameColor)
    }

    protected fun exitGame() {
        System.exit(0)
    }

    private fun createSaveSettingsDialog(): GFrame {
        // buttons
        val buttons = arrayListOf(
                TextButtonDescriptor("Yes", GButtonListener {
                    saveSettings()
                    worldHolder.menuHolder.removeTopFrame()
                }),
                TextButtonDescriptor("No", GButtonListener {
                    worldHolder.menuHolder.removeTopFrame()
                    worldHolder.menuHolder.removeTopFrame()
                }))

        // frame
        val closeListener = GButtonListener { worldHolder.menuHolder.removeTopFrame() }

        return dialogBuilder.createDialog("Save settings?", buttons, frameColor, closeListener,
                true)
    }

    private fun saveSettings() {
        worldHolder.menuHolder.removeTopFrame()
        // TODO save settings
    }

    companion object {
        private const val serialVersionUID = -8429325182641380571L
    }
}
