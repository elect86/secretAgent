package secretAgent.view.model

import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.ModelType
import cz.wa.wautils.math.Rectangle2D
import secretAgent.world.GLModel

/**
 * Invisible model.
 *
 * @author Ondrej Milenovsky
 */
class EmptyModel private constructor() : GLModel {

    override val allTileIds: Set<TileId>
        get() = emptySet()

    override val isTransparent: Boolean
        get() = true

    override val maxBounds: Rectangle2D
        get() = Rectangle2D.ZERO

    override val type: ModelType
        get() = ModelType.EMPTY

    override fun linkTextures(graphics: SAMGraphics) {} // nothing

    override fun hasLinkedTextures(): Boolean = true

    companion object {
        @JvmField
        val INSTANCE = EmptyModel()
    }
}