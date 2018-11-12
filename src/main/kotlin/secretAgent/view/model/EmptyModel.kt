package secretAgent.view.model

import cz.wa.wautils.math.Rectangle2D
import secretAgent.view.SamGraphics
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel
import secretAgent.world.ModelType

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

    override fun linkTextures(graphics: SamGraphics) {} // nothing

    override fun hasLinkedTextures(): Boolean = true

    companion object {
        @JvmField
        val INSTANCE = EmptyModel()
    }
}