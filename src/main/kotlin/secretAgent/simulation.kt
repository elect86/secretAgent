package secretAgent

import cz.wa.secretagent.worldinfo.WorldHolder
import cz.wa.wautils.swing.listener.RunnableLog
import org.apache.commons.math3.util.FastMath
import org.slf4j.LoggerFactory
import secretAgent.view.SamRenderer
import java.io.Serializable


/**
 * Some controller for GameRunner.
 *
 * @author Ondrej Milenovsky
 */
interface GameController : Serializable {

    /** Method called when the controller was not activate and will be active now.     */
    fun activate()

    /** World moved, process input.  */
    fun processInput(timeS: Double)
}

/**
 * Starts and runs the whole simulation. Cares about input, moving the world and rendering.
 *
 * @author Ondrej Milenovsky
 */
interface GameRunner : Serializable {

    fun startGame()

    fun endGame()

    fun init()

    /** This will be run as the first thing after initialization.     */
    fun setRunFirst(r: Runnable)
}

/**
 * Game runner that starts new thread for game simulation and rendering.
 * In single step processes input, moves the world, renders the frame (all in single thread).
 *
 * @author Ondrej Milenovsky
 */
class GameRunnerImpl : GameRunner, Serializable {

    var maxStepMs: Long = 40

    lateinit var worldHolder: WorldHolder
    var renderer: SamRenderer? = null
    var controller: GameController? = null
    var simulator: GameSimulator? = null

    @Transient
    private var runFirst: Runnable? = null
    @Transient
    private lateinit var thread: Thread
    @Transient
    private var running: Boolean = false

    override fun init() {
        running = false
        thread = Thread(object : RunnableLog() {
            override fun run2() = runGame()
        }, THREAD_NAME)
    }

    override fun setRunFirst(r: Runnable) {
        runFirst = r
    }

    private fun runGame() {
        try {
            renderer?.let {
                it.init()
                if (!it.isInitialized)
                    return
            }
            runFirst?.run()
        } catch (e: Throwable) {
            logger.error("", e)
        }

        var lastTime = System.currentTimeMillis()
        while (running)
            lastTime = nextStep(lastTime)
    }

    private fun nextStep(lastTime: Long): Long {
        val currentTime = System.currentTimeMillis()
        val timeDiff = FastMath.min(currentTime - lastTime, maxStepMs)
        val timeS = timeDiff / 1000.0
        val world = worldHolder.world
        val activeWorld = world?.isRunning == true

        // first add the time to the world
        if (activeWorld)
            world!!.addSimTimeMs(timeDiff)
        // process controller
        processInput(timeS)
        // move the world
        if (activeWorld)
            moveWorld(timeS)
        // finally render screen
        render()

        return currentTime // ~lastTime
    }

    override fun startGame() {
        running = true
        thread.start()
    }

    override fun endGame() {
        running = false
    }

    private fun processInput(timeS: Double) {
        controller?.let {
            try {
                it.processInput(timeS)
            } catch (e: Throwable) {
                logger.error("", e)
            }
        }
    }

    private fun moveWorld(timeS: Double) {
        simulator?.let {
            try {
                it.move(timeS)
            } catch (e: Throwable) {
                logger.error("", e)
            }

        }
    }

    private fun render() {
        renderer?.let {
            try {
                it.draw(worldHolder)
            } catch (e: Throwable) {
                logger.error("", e)
            }

            if (it.isCloseRequested)
                System.exit(0)
        }
    }

    companion object {
        private const val serialVersionUID = -5925062471372147489L

        private val logger = LoggerFactory.getLogger(GameRunnerImpl::class.java)

        const val THREAD_NAME = "Simulation and renderer"
    }
}

/**
 * Some class that will move the world.
 *
 * @author Ondrej Milenovsky
 */
interface GameSimulator : Serializable {

    /**
     * Moves the world.
     * @param timeS time diff
     * @return false if stop further moving
     */
    fun move(timeS: Double): Boolean
}
