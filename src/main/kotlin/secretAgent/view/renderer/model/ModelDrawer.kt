package secretAgent.view.renderer.model

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.renderer.PrimitivesDrawer
import secretAgent.view.renderer.TextureToDraw
import secretAgent.world.GLModel
import secretAgent.world.entity.Entity
import java.io.Serializable

/**
 * @author Ondrej Milenovsky
 */
abstract class AbstractModelDrawer<M : GLModel> : ModelDrawer<M> {

    lateinit var primitivesDrawer: PrimitivesDrawer

    companion object {

        private const val serialVersionUID = -5570243308467570348L

        @JvmStatic
        protected fun getFrame(currTimeMs_: Long, durationMs: Long, textures: List<TextureToDraw?>): TextureToDraw? {
            val currTimeMs = currTimeMs_ % durationMs
            val num = (currTimeMs / durationMs.toDouble() * textures.size).toInt()
            return textures[num]
        }
    }
}

/**
 * Draws some particular model on screen.
 *
 * @author Ondrej Milenovsky
 */
interface ModelDrawer<M : GLModel> : Serializable {
    /**
     * Draws particular model.
     * @param model model to draw
     * @param entity entity or level corresponding to the model
     * @param pos position on screen
     * @param scale scale of the model
     */
    fun draw(model: M, entity: Entity?, pos: Vector2D, scale: Double)
}