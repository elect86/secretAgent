package secretAgent.view.model

import cz.wa.secretagent.Constants
import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.AbstractModel
import cz.wa.secretagent.view.model.AgentTextures
import cz.wa.secretagent.view.model.ModelType
import cz.wa.secretagent.view.texture.TextureToDraw
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import java.util.ArrayList
import java.util.HashSet

/**
 * Model of an agent.
 *
 * @author Ondrej Milenovsky
 */
class AgentModel
/**
 * @param leftTextures textures when looking left
 * @param rightTextures textures wnen looking right
 * @param bounds model draw bounds, if null, will be computed from the texture
 * @param stayDurationMs anim cycle duration when standing
 * @param weaponCenter weapon rotation center
 */
(
        /** textures when turned left (can be null)  */
        val leftTextures: AgentTextures?,
        /** textures when turned right (can be null)  */
        val rightTextures: AgentTextures?,

        scale: Double,
        /** duration of frames when standing  */
        val stayDurationMs: Long,
        /** center position for gun when wielding one (when turned right)  */
        val weaponCenter: Vector2D) : AbstractModel(scale) {

    init {
        assert(leftTextures != null || rightTextures != null) {"both textures are null"}
    }

    override val allTileIds: Set<TileId>
        get() {
            val ret = HashSet<TileId>()
            leftTextures?.let {
                ret += it.stayIds
                ret += it.moveIds
                ret += it.jumpIds
                ret += it.deathIds
            }
            rightTextures?.let {
                ret += it.stayIds
                ret += it.moveIds
                ret += it.jumpIds
                ret += it.deathIds
            }
            return ret
        }

    override val type: ModelType
        get() = ModelType.AGENT

    override val maxBounds: Rectangle2D
        get() = MAX_BOUNDS

    override fun hasLinkedTextures()= leftTextures?.hasLinkedTextures() == true || rightTextures?.hasLinkedTextures() == true

    override fun linkTexturesInternal(graphics: SAMGraphics): Rectangle2D? {
        var modelBounds: Rectangle2D? = null
        leftTextures?.let {
            it.linkTextures(graphics)
            it.stayTextures?.let {
                modelBounds = AbstractModel.getModelBounds(it[0].tileBounds)
            }
        }
        rightTextures?.let {
            it.linkTextures(graphics)
            it.stayTextures?.let {
                modelBounds = AbstractModel.getModelBounds(it[0].tileBounds)
            }
        }
        return modelBounds
    }

    public override fun getAllTextures(): Collection<TextureToDraw> {
        val ret = ArrayList<TextureToDraw>()
        leftTextures?.let { ret += it.allTextures }
        rightTextures?.let { ret += it.allTextures }
        return ret
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + (leftTextures?.hashCode() ?: 0)
        result = prime * result + (rightTextures?.hashCode() ?: 0)
        result = prime * result + (stayDurationMs xor stayDurationMs.ushr(32)).toInt()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (!super.equals(other))
            return false
        return other is AgentModel && leftTextures == other.leftTextures && rightTextures == other.rightTextures && stayDurationMs != other.stayDurationMs
    }

    companion object {

        private val MAX_BOUNDS_TILES = 2.0
        /** max bounds to decide if the model is in viewport, default is 2 tiles to each direction from center  */
        private val MAX_BOUNDS = Rectangle2D(
                -Constants.TILE_SIZE.x * MAX_BOUNDS_TILES, -Constants.TILE_SIZE.y * MAX_BOUNDS_TILES,
                Constants.TILE_SIZE.x * MAX_BOUNDS_TILES * 2.0, Constants.TILE_SIZE.y * MAX_BOUNDS_TILES * 2.0)
    }

}

