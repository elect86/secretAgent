package secretAgent.game.controller.menuCreator

import cz.wa.secretagent.game.starter.CampaignStarter
import cz.wa.secretagent.io.FileSettings
import cz.wa.secretagent.io.campaign.CampaignPropertiesParser
import cz.wa.secretagent.launcher.SecretAgentMain
import cz.wa.secretagent.menu.builder.TextButtonDescriptor
import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.menu.window.component.selectable.GButtonListener
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Required
import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * Creates main menu. Caches all frames.
 *
 * @author Ondrej Milenovsky
 */
class MainMenuCreator : GeneralMenuCreator() {

    lateinit var fileSettings: FileSettings
    lateinit var campaignStarter: CampaignStarter

    @Transient
    private lateinit var mainMenu: GFrame
    @Transient
    private lateinit var aboutDialog: GFrame

    private fun createMainMenu(): GFrame {
        // buttons
        val buttons = arrayListOf(
                TextButtonDescriptor("Load game", GButtonListener {
                    // TODO load all saved games
                }),
                TextButtonDescriptor("New game", GButtonListener { worldHolder.menuHolder.addFrame(createNewGameMenu()) }),
                createSettingsButton(),
                TextButtonDescriptor("About", GButtonListener { worldHolder.menuHolder.addFrame(aboutDialog) }),
                createExitButton())
        // frame

        return dialogBuilder.createDialog("Secret Agent Remake", buttons, frameColor, null, false)
    }

    fun getMainMenu(): GFrame {
        if (!::mainMenu.isInitialized)
            mainMenu = createMainMenu()
        return mainMenu
    }

    fun getAboutDialog(): GFrame {
        if (!::aboutDialog.isInitialized)
            aboutDialog = createAboutDialog()
        return aboutDialog
    }

    private fun createSettingsButton() = TextButtonDescriptor("Settings", GButtonListener {
        settingsMenu.selectedIndex = 0
        worldHolder.menuHolder.addFrame(settingsMenu)
    })

    private fun createExitButton() = TextButtonDescriptor("Exit the game", GButtonListener {
        if (gameSettings.confirmDialogs) {
            exitDialog.selectedIndex = 0
            worldHolder.menuHolder.addFrame(exitDialog)
        } else
            exitGame()
    })

    private fun createNewGameMenu(): GFrame {
        // files

        val files = FileUtils.listFiles(File(fileSettings.campaignsDir),
                arrayOf(fileSettings.campaignExt), false)

        // buttons
        val buttons = ArrayList<TextButtonDescriptor>(files.size)
        for (file in files)
            createCampaignButton(file)?.let { buttons += it }

        // frame
        val closeListener = GButtonListener { worldHolder.menuHolder.removeTopFrame() }

        return dialogBuilder.createDialog("Select campaign to start", buttons, frameColor, closeListener, false)
    }

    /**
     * Loads the file and reads campaign name. On exception logs the exception and returns null.
     * @param file file containing campaign description
     * @return new button descriptor or null
     */
    private fun createCampaignButton(file: File): TextButtonDescriptor? {
        return try {
            val cp = CampaignPropertiesParser(file, fileSettings).parse()
            TextButtonDescriptor(cp.title, GButtonListener {
                if (campaignStarter.startCampaign(file))
                    worldHolder.menuHolder.removeAllFrames()
            })
        } catch (e: IOException) {
            logger.error("Failed to create campaign button", e)
            null
        }
    }

    private fun createAboutDialog(): GFrame = dialogBuilder.createDialog(ABOUT_MESSAGE, emptyList(),
            frameColor, { worldHolder.menuHolder.removeTopFrame() }, false)

    companion object {

        private const val serialVersionUID = 3451448102156807094L
        private val logger = LoggerFactory.getLogger(MainMenuCreator::class.java)

        private val ABOUT_MESSAGE = ("""
            About Secret Agent Remake

            Version: ${SecretAgentMain.VERSION}
            Author: Giuseppe Barbieri
            Author original remake: Ondrej Milenovsky
            Level parsing inspired
                by Camoto Studio
            Additional images from games:
                Duke Nukem 3D
                Commando""")
    }
}