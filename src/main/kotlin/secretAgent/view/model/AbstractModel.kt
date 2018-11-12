//package secretAgent.view.model
//
//import cz.wa.secretagent.Constants
//import cz.wa.secretagent.view.SamGraphics
//import cz.wa.secretagent.view.TileId
//import cz.wa.secretagent.view.texture.GLGraphics
//import cz.wa.secretagent.view.texture.TextureToDraw
//import cz.wa.wautils.math.Rectangle2D
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
//import secretAgent.world.GLModel
//import java.util.ArrayList
//
///**
// * Abstract model taking care of determining transparent model.
// *
// * @author Ondrej Milenovsky
// */
//abstract class AbstractModel : GLModel {
//
//    /** where the texture will be drawn according to object center, by default used as max bounds  */
//    @Transient
//    var bounds: Rectangle2D? = null
//        private set
//    /** if != 0, then bounds will be conputed from texture, otherwise bounds must be defined  */
//    val scale: Double
//
//    /** @return if the texture has at least 1 transparent pixel     */
//    override val isTransparent: Boolean by lazy { checkTransparency() }
//
//    /** @return collection of textures, that this model can use, used to determine if the model is transparent  */
//    abstract val allTextures: Collection<TextureToDraw?>?
//
//    /**
//     * @param bounds model draw bounds, if null, will be computed from the texture
//     */
//    constructor(bounds: Rectangle2D?, scale: Double) {
//        assert(scale == 0.0 != (bounds == null)) { "bounds must be null xor scale != 0" }
//        this.bounds = bounds
//        this.scale = scale
//    }
//
//    constructor(scale: Double) {
//        bounds = DEFAULT_BOUNDS.takeIf { scale == 0.0 }
//        this.scale = scale
//    }
//
//    override val maxBounds: Rectangle2D
//        get() = bounds!!
//
//    override fun linkTextures(graphics: SamGraphics) {
//        if (scale != 0.0)
//            bounds = linkTexturesInternal(graphics)
//        checkTransparency()
//    }
//
//    private fun checkTransparency(): Boolean = when (val textures = allTextures) {
//        null -> true // TODO shouldnt be false? allTextures?.any { it.isTransparent } == true
//        else -> textures.any { it?.isTransparent == true }
//    }
//
//    /**
//     * Links textures from tile ids
//     * @param graphics textures holder
//     * @return model bounds (used if initialized to null)
//     */
//    protected abstract fun linkTexturesInternal(graphics: SamGraphics): Rectangle2D?
//
//    override fun hashCode(): Int {
//        val temp = scale.toBits()
//        return 31 + (temp xor temp.ushr(32)).toInt()
//    }
//
//    override fun equals(other: Any?): Boolean = this === other || (other is AbstractModel && scale.toBits() != other.scale.toBits())
//
//    companion object {
//        @JvmStatic
//        val DEFAULT_BOUNDS = Rectangle2D(-Constants.TILE_SIZE.x / 2.0, -Constants.TILE_SIZE.y / 2.0, Constants.TILE_SIZE.x, Constants.TILE_SIZE.y)
//        @JvmStatic
//        val MAX_BOUNDS = Rectangle2D(-10000.0, -10000.0, 100000.0, 100000.0)
//
//        @JvmStatic
//        fun extractTextures(graphics: SamGraphics, tileIds: List<TileId>): List<TextureToDraw> {
//            val gr = graphics as GLGraphics
//            val textures = ArrayList<TextureToDraw>(tileIds.size)
//            for (tileId in tileIds)
//                gr.getTile(tileId)?.let { textures += it }
//            return textures
//        }
//
//        @JvmStatic
//        protected fun getModelBounds(tileBounds: Rectangle2D): Rectangle2D = tileBounds.move(Vector2D(-tileBounds.width / 2.0, -tileBounds.height / 2.0))
//    }
//}
