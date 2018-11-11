package secretAgent.world

import cz.wa.wautils.math.Rectangle2D
import secretAgent.view.SAMGraphics
import secretAgent.view.renderer.TileId

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


/**
 * Model rendered by LWJGL. All models must override hashcode and equals!!!
 *
 * @author Ondrej Milenovsky
 */
interface GLModel : ObjectModel {
    val type: ModelType
}

/**
 * Type of a model.
 *
 * @author Ondrej Milenovsky
 */
enum class ModelType {
    SIMPLE,
    ANIMATED,
    AGENT,
    LASER,
    WEAPON,
    EMPTY,
    HEALTH_BAR,
    EXPLOSION
}