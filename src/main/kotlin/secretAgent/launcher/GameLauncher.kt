package secretAgent.launcher

import cz.wa.secretagent.menu.MenuHolder
import cz.wa.secretagent.menu.MenuKeys
import cz.wa.secretagent.simulation.GameRunner
import cz.wa.secretagent.utils.ContextWrapper
import cz.wa.secretagent.utils.PropertiesLoader
import cz.wa.secretagent.utils.lwjgl.KeysUtils
import cz.wa.secretagent.utils.lwjgl.TextureLoader
import cz.wa.wautils.collection.TypedKey
import cz.wa.wautils.io.FileObjectUtils
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.context.support.FileSystemXmlApplicationContext
import secretAgent.game.PlayerHolder
import secretAgent.game.PlayerKeys
import secretAgent.game.starter.MapStarter
import secretAgent.io.FileSettings
import secretAgent.io.SamIO
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.GridLayout
import java.io.File
import java.io.IOException
import java.io.Serializable
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.BevelBorder
import javax.swing.border.EmptyBorder

/**
 * Launches the game.
 *
 * @author Ondrej Milenovsky
 */
class GameLauncher(private val appContext: String) {

    fun launch(loadingFrame: Boolean, cacheAction: CacheAction) {
        var frame: InitFrame? = null
        if (loadingFrame) {
            frame = InitFrame()
            frame.setText("Initializing...")
        }

        // load the context
        val cacheFile = File(CONTEXT_CACHE_FILE)
        if (cacheAction == CacheAction.DELETE || cacheAction == CacheAction.RENEW)
            FileUtils.deleteQuietly(cacheFile)

        // try load cached context
        var context: ParsedContext? = null
        if (cacheAction == CacheAction.USE_OR_CREATE && cacheFile.isFile) {
            frame!!.setText("Loading cached game context...")
            context = loadCachedContext(cacheFile)
        }

        // parse new context
        if (context == null) {
            frame!!.setText("Parsing Spring xml files...")
            val contextWrapper = ContextWrapper(FileSystemXmlApplicationContext(appContext))
            context = parseBeans(contextWrapper)
        }

        // create cache
        if (cacheAction == CacheAction.USE_OR_CREATE || cacheAction == CacheAction.RENEW) {
            frame!!.setText("Creating cached game context...")
            createCachedContext(context, cacheFile)
        }

        if (loadingFrame)
            frame!!.setText("Creating game classes...")
        logger.info("Creating game classes...")

        // init game classes
        initClasses(context)

        // start the game
        logger.info("Initializing display...")
        context.gameRunner.startGame()

        if (loadingFrame) {
            frame!!.isVisible = false
            frame.dispose()
        }
    }

    private fun parseBeans(context: ContextWrapper): ParsedContext {
        val gameRunner = context.getBean(GAME_RUNNER)
        val playerHolder = context.getBean(PLAYER_HOLDER)
        val menuHolder = context.getBean(MENU_HOLDER)
        val fileSettings = context.getBean(FILE_SETTINGS)
        val io = context.getBean(SAM_IO)
        val mapStarter = context.getBean(MAP_STARTER)

        return ParsedContext(gameRunner, playerHolder, menuHolder, fileSettings, io, mapStarter)
    }

    private fun initClasses(context: ParsedContext) {
        val fileSettings = context.fileSettings
        PropertiesLoader.loadPropertiesToObject(fileSettings.settings2dFile, context.samIO.settings2d)
        PropertiesLoader.loadPropertiesToObject(fileSettings.gameSettingsFile, context.samIO.gameSettings)
        loadPlayerKeys(context.playerHolder, fileSettings)
        loadMenuKeys(context.menuHolder, fileSettings)

        val gameRunner = context.gameRunner
        gameRunner.init()
        gameRunner.setRunFirst {
            logger.info("Loading initial data...")
            TextureLoader.setBgColor(Color(0, 0, 0, 0))
            loadDefaultGraphics(context.samIO)
            context.mapStarter.startMainMenu()
            logger.info("Game initialized.")
        }
    }

    private fun loadPlayerKeys(playerHolder: PlayerHolder, fileSettings: FileSettings) {
        val keys = PlayerKeys()
        if (fileSettings.playerKeysFile == null)
            return
        val file = File(fileSettings.playerKeysFile)
        try {
            KeysUtils.loadKeys(file, keys)
        } catch (e: IOException) {
            logger.error("Cannot load keys from file: " + file.absolutePath, e)
        }

        playerHolder.keys = keys
    }

    private fun loadMenuKeys(menuHolder: MenuHolder, fileSettings: FileSettings) {
        val keys = MenuKeys()
        if (fileSettings.menuKeysFile == null)
            return
        val file = File(fileSettings.menuKeysFile)
        try {
            KeysUtils.loadKeys(file, keys)
        } catch (e: IOException) {
            logger.error("Cannot load keys from file: " + file.absolutePath, e)
        }

        menuHolder.keys = keys
    }

    private fun loadDefaultGraphics(io: SamIO) {
        try {
            io.loadDefaultGraphics()
        } catch (e: IOException) {
            logger.error("Error loading default graphics.", e)
        }

    }

    private fun loadCachedContext(cacheFile: File): ParsedContext? {
        logger.info("Loading cached context...")
        return try {
            FileObjectUtils.readObjectFromFile(cacheFile) as ParsedContext
        } catch (e: ClassNotFoundException) {
            logger.error("Failed to read cached context from file: $cacheFile", e)
            null
        } catch (e: IOException) {
            logger.error("Failed to read cached context from file: $cacheFile", e)
            null
        }

    }

    private fun createCachedContext(context: ParsedContext, cacheFile: File) {
        logger.info("Creating cached context...")
        try {
            FileObjectUtils.writeObjectToFile(cacheFile, context)
        } catch (e: IOException) {
            logger.error("Failed to create cached context to file: $cacheFile", e)
        }

    }

    /**
     * Class holding all context beans.
     */
    private class ParsedContext(internal val gameRunner: GameRunner,
                                internal val playerHolder: PlayerHolder,
                                internal val menuHolder: MenuHolder,
                                internal val fileSettings: FileSettings,
                                internal val samIO: SamIO,
                                internal val mapStarter: MapStarter) : Serializable {
        companion object {
            private const val serialVersionUID = -5357344627896702434L
        }
    }

    /**
     * Frame displayed at start of the game.
     */
    private class InitFrame : JFrame("Secret Agent") {

        private var label: JLabel? = null

        init {
            setBounds(400, 300, 230, 72)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            isResizable = false
            initComponents()
            isVisible = true
        }

        fun setText(text: String) {
            label!!.text = text
        }

        private fun initComponents() {
            val p1 = JPanel()
            p1.border = BevelBorder(BevelBorder.RAISED)
            p1.layout = BorderLayout()
            add(p1, BorderLayout.CENTER)

            val p2 = JPanel()
            p2.border = EmptyBorder(8, 8, 0, 8)
            p2.layout = GridLayout(2, 1)
            label = JLabel()
            label!!.font = Font("Arial", Font.BOLD, 16)
            label!!.border = EmptyBorder(0, 0, 3, 0)
            p2.add(label)
            p2.add(JLabel(" "))

            p1.add(p2, BorderLayout.CENTER)
        }

        companion object {
            private const val serialVersionUID = -8806569485541236883L
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GameLauncher::class.java)

        val GAME_RUNNER = TypedKey<GameRunner>("gameRunner")
        val SAM_IO = TypedKey<SamIO>("samIO")
        val PLAYER_HOLDER = TypedKey<PlayerHolder>("playerHolder")
        val MENU_HOLDER = TypedKey<MenuHolder>("menuHolder")
        val FILE_SETTINGS = TypedKey<FileSettings>("fileSettings")
        val MAP_STARTER = TypedKey<MapStarter>("mapStarter")

        // files used for caching
        const val CONTEXT_CACHE_FILE = "cache/game_context.dat"
    }
}