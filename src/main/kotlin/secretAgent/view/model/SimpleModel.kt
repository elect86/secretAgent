package secretAgent.view.model

import cz.wa.secretagent.view.model.AbstractModel
import cz.wa.wautils.math.Rectangle2D
import secretAgent.view.renderer.GLGraphics
import secretAgent.view.SamGraphics
import secretAgent.view.renderer.TextureToDraw
import secretAgent.view.renderer.TileId
import secretAgent.world.ModelType

/**
 * Simple not animated sprite.
 *
 * @author Ondrej Milenovsky
 */
class SimpleModel

@JvmOverloads constructor(
        /** tile id to the texture  */
        val tileId: TileId,
        /** model draw bounds, if null, will be computed from the texture */
        bounds: Rectangle2D? = AbstractModel.DEFAULT_BOUNDS,

        scale: Double = 0.0) : AbstractModel(bounds, scale) {

    /** texture for openGl  */
    @Transient
    var texture: TextureToDraw? = null
        protected set

    override val allTileIds: Set<TileId>
        get() = setOf(tileId)

    override val type: ModelType
        get() = ModelType.SIMPLE

    override fun getAllTextures(): MutableCollection<TextureToDraw> {
        val res  = ArrayList<TextureToDraw>()
        texture?.let { res += it }
        return res
    }

    constructor(tileId: TileId, scale: Double) : this(tileId, AbstractModel.DEFAULT_BOUNDS.takeIf { scale == 0.0 }, scale)

    override fun linkTexturesInternal(graphics: SamGraphics): Rectangle2D? {
        val tile = (graphics as GLGraphics).getTile(tileId)
        texture = tile
        return tile?.let { AbstractModel.getModelBounds(tile.tileBounds) }
    }

    override fun hasLinkedTextures() = texture != null

    override fun hashCode() = 31 * super.hashCode() + tileId.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (!super.equals(other)) {
            return false
        }
        return other is SimpleModel && tileId == other.tileId
    }
}
