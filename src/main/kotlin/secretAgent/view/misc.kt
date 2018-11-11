package secretAgent.view

import cz.wa.secretagent.worldinfo.WorldHolder
import java.io.Serializable

/**
 * When some primitive is drawn, what the position means.
 *
 * @author Ondrej Milenovsky
 */
enum class DrawPosition { UPPER_LEFT, UPPER_RIGHT, CENTER }

/**
 * Some renderer, can render whole world, part of it or single entity.
 *
 * @author Ondrej Milenovsky
 */
interface Renderer : Serializable {
    fun init()
    fun dispose()
}

/**
 * Renders the world.
 *
 * @author Ondrej Milenovsky
 */
interface SamRenderer : Renderer {

    val isInitialized: Boolean

    val isCloseRequested: Boolean

    fun draw(world: WorldHolder)
}

/**
 * Interface for graphics.
 *
 * @author Ondrej Milenovsky
 */
interface SAMGraphics {
    fun dispose()
}

/**
 * Settings for renderer.
 *
 * @author Ondrej Milenovsky
 */
class Settings2D : Serializable {
    @JvmField
    var screenPosX = 0
    @JvmField
    var screenPosY = 0

    @JvmField
    var screenWidth = 0
    @JvmField
    var screenHeight = 0

    @JvmField
    var texFilter = false
    @JvmField
    var niceMap = false

    @JvmField
    var colorBitDepth = 0
    @JvmField
    var refreshRateHz = 0

    @JvmField
    var fullScreen = false
    @JvmField
    var vsync = false

    @JvmField
    var fps = 0

//    companion object {
//        private const val serialVersionUID = -4654618092028293538L
//    }
}
