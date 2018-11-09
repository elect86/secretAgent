package secretAgent.view.renderer

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