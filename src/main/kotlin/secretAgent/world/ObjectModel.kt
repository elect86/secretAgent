package secretAgent.world

import cz.wa.secretagent.view.SAMGraphics
import cz.wa.secretagent.view.TileId
import cz.wa.wautils.math.Rectangle2D

/**
 * Model of tile or entity. Can be any class, the renderer must be able to process it.
 *
 * @author Ondrej Milenovsky
 */
interface ObjectModel {

    /**
     * @return set of all tile ids that the model uses
     */
    val allTileIds: Set<TileId>

    /**
     * @return if the model has a transparent pixel in any texture
     */
    val isTransparent: Boolean

    /**
     * @return max visible bounds relative to center
     */
    val maxBounds: Rectangle2D

    /**
     * Link textures from the graphics (must be performed before rendering)
     * @param graphics
     */
    fun linkTextures(graphics: SAMGraphics)

    /**
     * @return if the textures are linked
     */
    fun hasLinkedTextures(): Boolean
}
