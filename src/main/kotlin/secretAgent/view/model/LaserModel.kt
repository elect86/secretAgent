package secretAgent.view.model

import cz.wa.secretagent.view.TileId
import cz.wa.secretagent.view.model.AnimatedModel
import cz.wa.secretagent.view.model.ModelType
import cz.wa.wautils.math.Rectangle2D
import java.awt.Color

/**
 * Model of laser, same as animated but is drawn different way.
 * The model returns MAX_BOUNDS, that means the model itself is always in view.
 * LaserModelDrawer should decide, if the model is really in the view, but it is not possible now.
 *
 * @author Ondrej Milenovsky
 */
class LaserModel(tileIds: List<TileId>, durationMs: Long, var color: Color) : AnimatedModel(tileIds, 1.0, durationMs) {

    override val maxBounds: Rectangle2D
        get() = MAX_BOUNDS

    override val type: ModelType
        get() = ModelType.LASER

    override fun hashCode(): Int = 31 * super.hashCode() + color.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (!super.equals(other))
            return false
        return other is LaserModel && color == other.color
    }

    fun copyWithColor(color: Color) = LaserModel(tileIds, durationMs, color).also { it.textures = textures }
}