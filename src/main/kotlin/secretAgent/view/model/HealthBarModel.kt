package secretAgent.view.model

import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.AbstractModel
import cz.wa.wautils.math.Rectangle2D
import secretAgent.view.renderer.GLGraphics
import secretAgent.view.renderer.TextureToDraw
import secretAgent.world.ModelType
import java.util.*
import kotlin.collections.HashSet

/**
 * Model of health bar.
 *
 * @author Ondrej Milenovsky
 */
class HealthBarModel(
        private val frameTileId: TileId?,
        private val healthTileId: TileId?, scale: Double) : AbstractModel(scale) {

    @Transient
    var frameTexture: TextureToDraw? = null
        private set
    @Transient
    var healthTexture: TextureToDraw? = null
        private set

    override val type: ModelType
        get() = ModelType.HEALTH_BAR

    override val allTileIds: Set<TileId>
        get() {
            val res = HashSet<TileId>()
            frameTileId?.let { res += it }
            healthTileId?.let { res += it }
            return res
        }

    override fun hasLinkedTextures() = healthTexture != null

    override fun linkTexturesInternal(graphics: SAMGraphics): Rectangle2D? {
        frameTexture = (graphics as GLGraphics).getTile(frameTileId!!) ?: throw Error("missing tile $frameTileId")
        healthTexture = graphics.getTile(healthTileId!!) ?: throw Error("missing tile $healthTileId")
        return frameTexture?.let { AbstractModel.getModelBounds(it.tileBounds) }
    }

    // TODO nullability
    public override fun getAllTextures(): Collection<TextureToDraw> = Arrays.asList<TextureToDraw>(frameTexture, healthTexture)

    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + (frameTileId?.hashCode() ?: 0)
        result = prime * result + (healthTileId?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (!super.equals(other)) {
            return false
        }
        return other is HealthBarModel && frameTileId == other.frameTileId && healthTileId == other.healthTileId
    }
}