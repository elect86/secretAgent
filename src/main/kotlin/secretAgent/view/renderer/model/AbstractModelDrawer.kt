package secretAgent.view.renderer.model

import cz.wa.secretagent.view.renderer.PrimitivesDrawer
import cz.wa.secretagent.view.renderer.model.ModelDrawer
import cz.wa.secretagent.view.texture.TextureToDraw
import secretAgent.world.GLModel

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
