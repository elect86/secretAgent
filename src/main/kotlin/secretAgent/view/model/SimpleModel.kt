//package secretAgent.view.model
//
//import cz.wa.secretagent.view.SAMGraphics
//import cz.wa.secretagent.view.TileId
//import cz.wa.secretagent.view.model.ModelType
//import cz.wa.secretagent.view.texture.GLGraphics
//import cz.wa.secretagent.view.texture.TextureToDraw
//import cz.wa.wautils.math.Rectangle2D
//
///**
// * Simple not animated sprite.
// *
// * @author Ondrej Milenovsky
// */
//class SimpleModel
//
//@JvmOverloads constructor(
//        /** tile id to the texture  */
//        val tileId: TileId?,
//        /** model draw bounds, if null, will be computed from the texture */
//        bounds: Rectangle2D? = AbstractModel.DEFAULT_BOUNDS,
//        scale: Double = 0.0) : AbstractModel(bounds, scale) {
//
//    /** texture for openGl  */
//    @Transient
//    var texture: TextureToDraw? = null
//        protected set
//
//    override val allTileIds: Set<TileId>
//        get() = tileId?.let { setOf(tileId) } ?: setOf() // TODO check
//
//    override val type: ModelType
//        get() = ModelType.SIMPLE
//
//    override val allTextures: Collection<TextureToDraw>?
//        get() = texture?.let { listOf(it) }
//
//    constructor(tileId: TileId, scale: Double) : this(tileId, AbstractModel.DEFAULT_BOUNDS.takeIf { scale == 0.0 }, scale)
//
//    override fun linkTexturesInternal(graphics: SAMGraphics): Rectangle2D? {
//        val tile = (graphics as GLGraphics).getTile(tileId)
//        texture = tile
//        return tile?.let { AbstractModel.getModelBounds(tile.tileBounds) }
//    }
//
//    override fun hasLinkedTextures() = texture != null
//
//    override fun hashCode() = 31 * super.hashCode() + (tileId?.hashCode() ?: 0)
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) {
//            return true
//        }
//        if (!super.equals(other)) {
//            return false
//        }
//        return other is SimpleModel && tileId == other.tileId
//    }
//}
