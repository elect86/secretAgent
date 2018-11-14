package secretAgent.view.renderer

import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Rectangle2I
import cz.wa.wautils.math.Vector2F
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.lwjgl.opengl.GL11.*
import org.newdawn.slick.SlickException
import org.newdawn.slick.UnicodeFont
import org.newdawn.slick.font.effects.ColorEffect
import org.newdawn.slick.font.effects.ShadowEffect
import org.newdawn.slick.opengl.TextureImpl
import secretAgent.utils.Texture
import secretAgent.view.DrawPosition
import secretAgent.view.Settings2D
import java.awt.Color
import java.awt.Font
import java.io.Serializable

/**
 * Draws primitives to canvas. No other class should draw anything because of the viewport.
 *
 * @author Ondrej Milenovsky
 */
class PrimitivesDrawer : Serializable {

    lateinit var settings: Settings2D
    lateinit var srcFont: Font

    @Transient
    var drawPosition = DrawPosition.CENTER
    @Transient
    private lateinit var font: UnicodeFont
    @Transient
    private var shadowEffect: ShadowEffect? = null
    @Transient
    private var texColor: Color? = null
    @Transient
    private var singleUseColor: Boolean = false

    fun init() {
        font = UnicodeFont(srcFont)
        font.addAsciiGlyphs()
        shadowEffect = ShadowEffect(Color.BLACK, 1, 1, 1f)
        font.effects += shadowEffect
        font.effects += ColorEffect()
        try {
            font.loadGlyphs()
        } catch (e: SlickException) {
            throw RuntimeException(e)
        }
    }

    fun dispose() {
        font.clearGlyphs()
//        font = null TODO check
        shadowEffect = null
    }

    /**
     * All textures will be covered by this color.
     * @param color color of textures
     */
    private fun setOverlayColor(color: Color) {
        glColor4f(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    }

    /**
     * Draws rotated (90 deg) or flipped texture as rectangle to canvas.
     * @param tex the texture
     * @param pos position in pixels (center)
     * @param bounds bounds where the texture will be drawn relative to pos, will be scaled
     * @param scale size multiplier in pixels
     * @param color overlay color (null = white)
     * @param flip true if the texture will be flipped horizontally before rotating
     */
    @JvmOverloads
    fun drawTexture(tex: TextureToDraw, pos: Vector2D, bounds: DrawBounds, scale: Double, flip: Boolean = false) {
        // tex coords
        val t = Array(4) { Vector2F(0f, 0f) }
        val r = tex.tileBounds
        val w = tex.texture.width.toDouble()
        val h = tex.texture.height.toDouble()
        val cut = if (settings.texFilter) 0.5 else 0.01
        t[0] = Vector2F(((r.x + cut) / w).toFloat(), ((r.y + cut) / h).toFloat())
        t[2] = Vector2F(((r.x2 - cut) / w).toFloat(), ((r.y2 - cut) / h).toFloat())
        t[1] = Vector2F(t[2].x, t[0].y)
        t[3] = Vector2F(t[0].x, t[2].y)
        if (flip) {
            flipTexCoords(t)
        }

        // canvas coords
        val p = Array(4) { Vector2F(0f, 0f) }
        val s1: Vector2D
        val s2: Vector2D
        when {
            drawPosition == DrawPosition.CENTER -> {
                s1 = Vector2D(bounds.x1 * scale, bounds.y1 * scale)
                s2 = Vector2D(bounds.x2 * scale, bounds.y2 * scale)
            }
            drawPosition == DrawPosition.UPPER_LEFT -> {
                s1 = Vector2D.ZERO
                s2 = Vector2D(bounds.x2 - bounds.x1, bounds.y2 - bounds.y1).scalarMultiply(scale)
            }
            else -> {
                // upper right
                s1 = Vector2D(bounds.x2 - bounds.x1, 0.0).scalarMultiply(scale)
                s2 = Vector2D(0.0, bounds.y2 - bounds.y1).scalarMultiply(scale)
            }
        }
        p[0] = Vector2F(s1)
        p[2] = Vector2F(s2)
        p[1] = Vector2F(p[2].x, p[0].y)
        p[3] = Vector2F(p[0].x, p[2].y)

        // draw
        setOverlayColor(texColor ?: Color.WHITE)
        glTranslated(pos.x, pos.y, 0.0)
        drawTexture(tex.texture, t, p)
        glLoadIdentity()
        if (singleUseColor)
            texColor = null
    }

    /**
     * Draws rotated texture.
     * @param tex the texture
     * @param pos position in pixels (center)
     * @param bounds bounds where the texture will be drawn relative to pos, will be scaled
     * @param scale size multiplier in pixels
     * @param color overlay color (null = white)
     * @param angle angle in radians to rotate
     * @param flip true if the texture will be flipped horizontally before rotating
     */
    @JvmOverloads
    fun drawTexture(tex: TextureToDraw, pos: Vector2D, bounds: DrawBounds, scale: Double, angle: Double,
                    flip: Boolean = false) {
        // translate and rotate
        glTranslated(pos.x, pos.y, 0.0)
        glRotated(angle * 180 / FastMath.PI, 0.0, 0.0, 1.0)
        // draw to 0:0
        drawTexture(tex, Vector2D.ZERO, bounds, scale, flip)
        // revert
        glLoadIdentity()
    }

    private fun flipTexCoords(t: Array<Vector2F>) {
        var u = t[0]
        t[0] = t[1]
        t[1] = u
        u = t[2]
        t[2] = t[3]
        t[3] = u
    }

    private fun drawTexture(texture: Texture, t: Array<Vector2F>, p: Array<Vector2F>) {
        texture.bind()
        glBegin(GL_QUADS)
        glTexCoord2f(t[0].x, t[0].y)
        glVertex2f(p[0].x, p[0].y)
        glTexCoord2f(t[1].x, t[1].y)
        glVertex2f(p[1].x, p[1].y)
        glTexCoord2f(t[2].x, t[2].y)
        glVertex2f(p[2].x, p[2].y)
        glTexCoord2f(t[3].x, t[3].y)
        glVertex2f(p[3].x, p[3].y)
        glEnd()
    }

    fun getTextSize(text: String) = Vector2D(font.getWidth(text).toDouble(), font.font.size.toDouble())

    /**
     * Draws text on screen.
     * @param text the text
     * @param pos_ center position in pixels
     * @param fontSize_ size in pixels
     * @param color color
     */
    fun drawText(text: String, pos_: Vector2D, fontSize_: Double, color: Color) {
        var pos = pos_
        var fontSize = fontSize_
        // the slick font uses different textures, need to unbind them manually
        // also the slick does not revert the color
        TextureImpl.bindNone()
        val slickColor = org.newdawn.slick.Color(color.red, color.green, color.blue, color.alpha)
        fontSize /= srcFont.size.toDouble()
        val width = FastMath.round(font.getWidth(text) * fontSize).toInt()
        val height = FastMath.round(font.getHeight(text) * fontSize).toInt()

        // shadow
        shadowEffect!!.opacity = color.alpha / 255f * SHADOW_ALPHA
        val shadowDist = (fontSize * SHADOW_DIST).toFloat()
        shadowEffect!!.xDistance = shadowDist
        shadowEffect!!.yDistance = shadowDist

        // draw text
        when (drawPosition) {
            DrawPosition.CENTER -> pos = pos.subtract(Vector2D(width / 2.0, height / 2.0))
            DrawPosition.UPPER_RIGHT -> pos = pos.subtract(Vector2D(width.toDouble(), 0.0))
        }
        glTranslated(pos.x, pos.y, 0.0)
        glScaled(fontSize, fontSize, 0.0)
        font.drawString(0f, 0f, text, slickColor)
                // revert
        // revert
        glLoadIdentity()
        Texture.unbind()
    }

    /**
     * Fills rectangle on screen, no position remapping
     * @param rect
     * @param color
     */
    fun fillRect(rect: Rectangle2I, color: Color) {
        Texture.unbind()
        setOverlayColor(color)
        glBegin(GL_QUADS)
        glVertex2f(rect.x.toFloat(), rect.y.toFloat())
        glVertex2f(rect.x2.toFloat(), rect.y.toFloat())
        glVertex2f(rect.x2.toFloat(), rect.y2.toFloat())
        glVertex2f(rect.x.toFloat(), rect.y2.toFloat())
        glEnd()
    }

    /**
     * Draws rectangle on screen, no position remapping
     * @param rect
     * @param width
     * @param color
     */
    fun drawRect(rect: Rectangle2D, width: Double, color: Color) {
        val x1 = rect.x.toFloat()
        val y1 = rect.y.toFloat()
        val x2 = rect.x2.toFloat()
        val y2 = rect.y2.toFloat()
        val w = width.toFloat()

        Texture.unbind()
        setOverlayColor(color)

        // upper
        glBegin(GL_QUADS)
        glVertex2f(x1, y1)
        glVertex2f(x2, y1)
        glVertex2f(x2, y1 + w)
        glVertex2f(x1, y1 + w)
        glEnd()
        // lower
        glBegin(GL_QUADS)
        glVertex2f(x1, y2)
        glVertex2f(x2, y2)
        glVertex2f(x2, y2 - w)
        glVertex2f(x1, y2 - w)
        glEnd()
        // left
        glBegin(GL_QUADS)
        glVertex2f(x1, y1 + w)
        glVertex2f(x1 + w, y1 + w)
        glVertex2f(x1 + w, y2 - w)
        glVertex2f(x1, y2 - w)
        glEnd()
        // right
        glBegin(GL_QUADS)
        glVertex2f(x2, y1 + w)
        glVertex2f(x2 - w, y1 + w)
        glVertex2f(x2 - w, y2 - w)
        glVertex2f(x2, y2 - w)
        glEnd()
    }

    /**
     * Set color for next texture that will be drawn.
     * @param color the color
     */
    fun setTexColor(color: Color) {
        texColor = color
        singleUseColor = true
    }

    fun setColor(color: Color, singleUse: Boolean) {
        texColor = color
        singleUseColor = singleUse
    }

    companion object {

        private const val serialVersionUID = -4323747666228324745L

        private val SHADOW_ALPHA = 1f
        private val SHADOW_DIST = 0.1
    }

}