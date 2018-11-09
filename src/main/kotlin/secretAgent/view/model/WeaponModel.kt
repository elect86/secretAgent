package secretAgent.view.model

import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.AnimatedModel
import cz.wa.secretagent.view.model.ModelType
import cz.wa.secretagent.view.texture.TextureToDraw
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import java.util.ArrayList
import java.util.HashSet

/**
 * Model of weapon, consists of two models, one for wielding the weapon, second for firing
 *
 * @author Ondrej Milenovsky
 */
class WeaponModel(
        private val wieldModel: AnimatedModel,
        private val wieldModelCenter: Vector2D,
        private val fireModel: AnimatedModel,
        private val fireModelCenter: Vector2D, scale: Double) : AbstractModel(scale) {

    @Transient
    private var hash = 0

    override val maxBounds: Rectangle2D
        get() = MAX_BOUNDS

    override val type: ModelType
        get() = ModelType.WEAPON

    override val allTileIds: Set<TileId>
        get() = HashSet<TileId>().also {
            it += wieldModel.allTileIds
            it += fireModel.allTileIds
        }

    override val allTextures: Collection<TextureToDraw>?
        get() {
            val res = ArrayList<TextureToDraw>()
            wieldModel.allTextures?.let { res += it }
            fireModel.allTextures?.let { res += it }
            return res
        }

    fun getModel(firing: Boolean): AnimatedModel = if (firing) fireModel else wieldModel

    fun getCenter(firing: Boolean): Vector2D = if (firing) fireModelCenter else wieldModelCenter

    override fun hasLinkedTextures(): Boolean = wieldModel.hasLinkedTextures() && fireModel.hasLinkedTextures()

    override fun linkTexturesInternal(graphics: SAMGraphics): Rectangle2D? {
        wieldModel.linkTextures(graphics)
        fireModel.linkTextures(graphics)
        return wieldModel.bounds
    }

    override fun hashCode(): Int {
        if (hash == 0) {
            val prime = 31
            var result = super.hashCode()
            result = prime * result + fireModel.hashCode()
            result = prime * result + fireModelCenter.hashCode()
            result = prime * result + wieldModel.hashCode()
            result = prime * result + wieldModelCenter.hashCode()
            hash = result
        }
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (!super.equals(other))
            return false
        return other is WeaponModel && fireModel == other.fireModel && fireModelCenter == other.fireModelCenter &&
                wieldModel == other.wieldModel && wieldModelCenter == other.wieldModelCenter
    }
}