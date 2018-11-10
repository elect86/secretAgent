package secretAgent.view.model

import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.AbstractModel
import cz.wa.wautils.math.Rectangle2D
import secretAgent.view.renderer.TextureToDraw
import secretAgent.world.ModelType
import java.util.HashSet

/**
 * Animated model.
 *
 * @author Ondrej Milenovsky
 */
open class AnimatedModel
/**
 * @param tileIds tile ids of the textures
 * @param scale scale of the model
 * @param durationMs anim cycle duration
 */
(
        /** tile ids to the texture  */
        val tileIds: List<TileId>,

        scale: Double,
        /** duration of all frames to cycle  */
        val durationMs: Long) : AbstractModel(scale) {

    init {
        assert(tileIds.isNotEmpty()) { "tileIds must contain at least 1 item" }
        assert(durationMs > 0) { "duration must be > 0" }
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
        get() = ModelType.ANIMATED

    override fun hasLinkedTextures() = textures != null

    override fun linkTexturesInternal(graphics: SAMGraphics): Rectangle2D? {
        textures = AbstractModel.extractTextures(graphics, tileIds)
        return textures?.let { AbstractModel.getModelBounds(it[0].tileBounds) }
    }

    public override fun getAllTextures(): Collection<TextureToDraw>? = textures

    override fun hashCode(): Int {
        if (hash == 0) {
            val prime = 31
            var result = super.hashCode()
            result = prime * result + (durationMs xor durationMs.ushr(32)).toInt()
            result = prime * result + tileIds.hashCode()
            hash = result
        }
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (!super.equals(other)) {
            return false
        }
        return other is AnimatedModel && durationMs != other.durationMs && tileIds == other.tileIds
    }
}
