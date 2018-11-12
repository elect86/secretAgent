package secretAgent.view.model

import cz.wa.secretagent.view.model.AbstractModel
import cz.wa.wautils.math.Rectangle2D
import secretAgent.view.SamGraphics
import secretAgent.view.renderer.TextureToDraw
import secretAgent.view.renderer.TileId
import secretAgent.world.ModelType
import java.util.HashSet

/**
 * Model of an explosion, contains only the frames.
 *
 * @author Ondrej Milenovsky
 */
class ExplosionModel(
        /** tile ids to the texture  */
        val tileIds: List<TileId>) : AbstractModel(1.0) {

    init {
        assert(tileIds.isNotEmpty()){"tileIds must contain at least 1 item"}
    }
    /** texture for openGl  */
    @Transient
    var textures: List<TextureToDraw>? = null
        protected set

    @Transient
    private var hash: Int = 0

    override val allTileIds: Set<TileId>
        get() = HashSet(tileIds)

    override val type: ModelType
        get() = ModelType.EXPLOSION

    override fun hasLinkedTextures() = textures != null

    override fun linkTexturesInternal(graphics: SamGraphics): Rectangle2D? {
        textures = AbstractModel.extractTextures(graphics, tileIds)
        return textures?.let { AbstractModel.getModelBounds(it[0].tileBounds) }
    }

    public override fun getAllTextures(): Collection<TextureToDraw>? = textures

    override fun hashCode(): Int {
        if (hash == 0)
            hash = 31 * super.hashCode() + tileIds.hashCode()
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (!super.equals(other))
            return false
        return other is ExplosionModel && tileIds == other.tileIds
    }
}