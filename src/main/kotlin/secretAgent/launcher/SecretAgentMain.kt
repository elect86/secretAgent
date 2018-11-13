package secretAgent.launcher

import cz.wa.wautils.swing.component.WaDialogs
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    println("""
                Secret Agent Remake by Wanucha and further developed by Giuseppe Barbieri v${SecretAgentMain.VERSION}
                    use argument ${SecretAgentMain.DISABLE_LOADING_FRAME_ARG} to disable the first loading window
                    The game context is by default cached, to manipulate the cache, use these arguments:
                        ${SecretAgentMain.CREATE_CONTEXT_CACHE_ARG} - create new cache
                        ${SecretAgentMain.DELETE_CONTEXT_CACHE_ARG} - delete cache
                        ${SecretAgentMain.IGNORE_CONTEXT_CACHE_ARG} - ignore cache""")
    try {
        SecretAgentMain(SecretAgentMain.GAME_APP_CONTEXT).startGame(args)
    } catch (e: Throwable) {
        SecretAgentMain.logger.error("", e)
        try {
            val message = "<html>" + e.message.limit(130).replace("\n", "<br>") + "<br><br>Check 'log/log.txt' for full error report.</html>"
            WaDialogs.showDialog(null, "Error starting application", message, "Exit")
        } catch (e2: Throwable) {
            SecretAgentMain.logger.error("Error displaying error window :-)", e2)
        }
        System.exit(0)
    }
}

private fun String?.limit(maxLength: Int): String {
    return when {
        this == null -> "[no message]"
        length > maxLength -> substring(0, maxLength - 3) + "..."
        else -> this
    }
}

/**
 * The main class.
 *
 * @author Ondrej Milenovsky
 */
class SecretAgentMain(private val appContext: String) {

    internal fun startGame(args: Array<String>) {
        var loadingFrame = true
        var cacheAction = CacheAction.USE_OR_CREATE
        for (arg in args) {
            when {
                arg.equals(DISABLE_LOADING_FRAME_ARG, ignoreCase = true) -> loadingFrame = false
                arg.equals(DELETE_CONTEXT_CACHE_ARG, ignoreCase = true) -> cacheAction = CacheAction.DELETE
                arg.equals(CREATE_CONTEXT_CACHE_ARG, ignoreCase = true) -> cacheAction = CacheAction.RENEW
                arg.equals(IGNORE_CONTEXT_CACHE_ARG, ignoreCase = true) -> cacheAction = CacheAction.IGNORE
            }
        }
        GameLauncher(appContext).launch(loadingFrame, cacheAction)
    }

    companion object {
        internal val logger = LoggerFactory.getLogger(SecretAgentMain::class.java)

        const val VERSION = "0.03"

        const val GAME_APP_CONTEXT = "classpath:game_context.xml"

        const val DISABLE_LOADING_FRAME_ARG = "-noloadingframe"
        const val DELETE_CONTEXT_CACHE_ARG = "-deletecache"
        const val CREATE_CONTEXT_CACHE_ARG = "-createcache"
        const val IGNORE_CONTEXT_CACHE_ARG = "-ignorecache"
    }
}

/**
 * Parameter for cache action
 *
 * @author Ondrej Milenovsky
 */
enum class CacheAction {
    IGNORE,
    USE_OR_CREATE,
    RENEW,
    DELETE
}