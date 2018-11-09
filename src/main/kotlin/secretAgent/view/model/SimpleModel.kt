package secretAgent.view.model

import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.ModelType
import cz.wa.secretagent.view.texture.GLGraphics
import cz.wa.secretagent.view.texture.TextureToDraw
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.lang.Validate

/**
 * Simple not animated sprite.
 *
 * @author Ondrej Milenovsky
 */
class SimpleModel(
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
        get() = setOf<TileId>(tileId)

    override val type: ModelType
        get() = ModelType.SIMPLE

    override val allTextures: Collection<TextureToDraw>?
        get() = listOf<TextureToDraw>(texture)

    constructor(tileId: TileId, scale: Double) : this(tileId, AbstractModel.DEFAULT_BOUNDS.takeIf { scale == 0.0 }, scale)

    override fun linkTexturesInternal(graphics: SAMGraphics): Rectangle2D? {
        val tile = (graphics as GLGraphics).getTile(tileId)
        texture = tile
        return if (tile != null) {
            AbstractModel.getModelBounds(tile.tileBounds)
        } else {
            null
        }
    }

    override fun hasLinkedTextures(): Boolean {
        return texture != null
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + (tileId?.hashCode() ?: 0)
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (!super.equals(obj)) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as SimpleModel?
        if (tileId == null) {
            if (other!!.tileId != null) {
                return false
            }
        } else if (tileId != other!!.tileId) {
            return false
        }
        return true
    }

}
