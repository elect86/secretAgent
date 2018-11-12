package secretAgent.view.renderer

import cz.wa.secretagent.utils.lwjgl.Texture
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.view.SamGraphics
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.*

/**
 * Bounds on canvas of an object that is about to be drawn.
 *
 * @author Ondrej Milenovsky
 */
class DrawBounds(val x1: Double, val y1: Double,
                 val x2: Double, val y2: Double) {

    constructor(rect: Rectangle2D) : this(rect.x, rect.y, rect.x2, rect.y2)

    constructor(rect: Rectangle2D, flipX: Boolean) : this(
            if (flipX) rect.x2 else rect.x, rect.y,
            if (flipX) rect.x else rect.x2, rect.y2)

    constructor(v1: Vector2D, v2: Vector2D) : this(v1.x, v1.y, v2.x, v2.y)
}

/**
 * Holds all graphics
 *
 * @author Ondrej Milenovsky
 */
class GLGraphics : SamGraphics, Serializable {

    @Transient
    private lateinit var tileSets: MutableMap<Int, GLTileSet>

    init {
        init()
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(stream: ObjectInputStream) {
        stream.defaultReadObject()
        init()
    }

    private fun init() {
        tileSets = HashMap()
    }

    fun getTile(tileId: TileId): TextureToDraw? {
        val tileSet = tileSets[tileId.tileSetId] ?: return null
        return tileSet.getTile(tileId.tileId)
    }

    fun setTileSet(tileSetId: Int, tileSet: GLTileSet) {
        tileSets[tileSetId]?.dispose()
        tileSets[tileSetId] = tileSet
    }

    fun clearOrigImages() {
        for (set in tileSets.values)
            set.clearOrigImage()
    }

    override fun dispose() {
        for (set in tileSets.values)
            set.dispose()
        init()
    }

    companion object {
        private const val serialVersionUID = 5985274627008462545L
    }
}

/**
 * Class representing multiple tiles in single texture.
 * Holds texture and tile size, returns single tile by an index.
 *
 * @author Ondrej Milenovsky
 */
class GLTileSet(private val texture: Texture,
                private val sizeX: Int,
                private val sizeY: Int) {

    private val cache: MutableMap<Int, TextureToDraw> = HashMap()

    fun getTile(id: Int): TextureToDraw = cache.getOrPut(id){ createTexture(id) }

    private fun createTexture(id: Int): TextureToDraw {
        val width = texture.width
        val countX = width / sizeX
        val x = id % countX * sizeX
        val y = id / countX * sizeY
        return TextureToDraw(texture, Rectangle2D(x.toDouble(), y.toDouble(), sizeX.toDouble(), sizeY.toDouble()))
    }

    fun clearOrigImage() = texture.clearOrigImage()

    fun dispose() = texture.dispose()
}

/**
 * Texture with bounds, can be a cut of bigger texture.
 *
 * @author Ondrej Milenovsky
 */
class TextureToDraw(val texture: Texture,
                    /** rectangle in the texture */
                    val tileBounds: Rectangle2D) {

    /**
     * @return if the texture has at least 1 transparent pixel, no caching so should not be used often,
     * if the origImage is no more, returns true
     */
    val isTransparent: Boolean
        get() {
            val img = texture.origImage ?: return true // TODO false?
            val x1 = tileBounds.x.toInt()
            val x2 = FastMath.ceil(tileBounds.x2).toInt()
            val y1 = tileBounds.y.toInt()
            val y2 = FastMath.ceil(tileBounds.y2).toInt()
            for (y in y1 until y2)
                for (x in x1 until x2)
                    if (img.getRGB(x, y) shr 24 and 0xff != 0xff)
                        return true
            return false
        }
}