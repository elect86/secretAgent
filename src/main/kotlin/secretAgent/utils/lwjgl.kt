package secretAgent.utils

import cz.wa.wautils.io.PropertiesUtils
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.lwjgl.BufferUtils
import org.lwjgl.LWJGLException
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.EXTFramebufferObject.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GLContext
import org.slf4j.LoggerFactory
import secretAgent.view.Settings2D
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.Serializable
import java.nio.ByteBuffer
import java.util.*
import javax.imageio.ImageIO

/**
 * Buffered texture for LWJGL.
 *
 * @author Ondrej Milenovsky
 */
class BufferedTexture(private val width: Int, private val height: Int) {

    private var frameBufferId = 0
    private var textureId = 0
    private var renderBufferId = 0

    init {
        assert(width > 0 && height > 0) { "width and height must be > 0" }
        init()
    }

    private fun init() {
        if (!isSupported)
            throw IllegalStateException("The graphics driver must support framebuffer object")

        // create a new framebuffer
        frameBufferId = glGenFramebuffersEXT()
        // and a new texture used as a color buffer
        textureId = glGenTextures()
        // And finally a new depthbuffer
        renderBufferId = glGenRenderbuffersEXT()

        // switch to the new framebuffer
        begin()

        // initialize color texture
        // Bind the colorbuffer texture
        glBindTexture(GL_TEXTURE_2D, textureId)
        // make it nearest
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST.toFloat())
        // Create the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, null as ByteBuffer?)
        // attach it to the framebuffer
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, textureId, 0)

        // initialize depth renderbuffer
        // bind the depth renderbuffer
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, renderBufferId)
        // get the data space for it
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height)
        // bind it to the renderbuffer
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
                renderBufferId)

        // Switch back to normal rendering
        end()
    }

    /**
     * Begins rendering on this buffer.
     */
    fun begin() {
        Texture.unbind()
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBufferId)
    }

    /**
     * Ends rendering on this buffer.
     */
    fun end() = glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0)

    fun clear() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

    /**
     * Draws the buffered texture on screen.
     * @param pos upper left corner
     * @param size size
     */
    fun draw(pos: Vector2D, size: Vector2D) {
        glBindTexture(GL_TEXTURE_2D, textureId)

        glBegin(GL_QUADS)
        glVertex2d(pos.x, pos.y)
        glVertex2d(pos.x + size.x, pos.y)
        glVertex2d(pos.x + size.x, pos.y + size.y)
        glVertex2d(pos.x, pos.y + size.y)
        glEnd()

        Texture.unbind()
    }

    fun dispose() {
        glDeleteTextures(textureId)
        glDeleteFramebuffersEXT(frameBufferId)
        glDeleteRenderbuffersEXT(renderBufferId)
    }

    companion object {

        val isSupported: Boolean
            get() = GLContext.getCapabilities().GL_EXT_framebuffer_object
    }
}

/**
 * Switches to full screen.
 *
 * @author Ondrej Milenovsky
 */
class FullScreenSwitcher : Serializable {

    var settings: Settings2D? = null

    @Transient
    private var mouseGrabbed = false

    /**
     * Returns if is currently full screen, never throws exceptions.
     * @return if full screen, false on fail
     */
    val isFullScreen: Boolean
        get() = try {
            Display.isFullscreen()
        } catch (e: RuntimeException) {
            logger.error(e.message)
            false
        }

    /**
     * Sets full screen on/off, never throws exceptions.
     * @param b true - full screen, false - windowed
     * @return success
     */
    fun setFullScreen(b: Boolean) = when {
        b -> setFullScreen()
        else -> setWindowed()
    }

    /**
     * Switches to windowed mode, never throws exceptions.
     * @return success
     */
    fun setWindowed(): Boolean {
        if (!isFullScreen)
            return true
        return try {
            Display.setFullscreen(false)
            Display.setDisplayMode(DisplayMode(settings!!.screenWidth, settings!!.screenHeight))
            Mouse.setGrabbed(mouseGrabbed)
            true
        } catch (e: LWJGLException) {
            logger.error("Cannot switch to window", e)
            false
        }
    }

    /**
     * Switches to full screen and grabs mouse, never throws exceptions.
     * @return success
     */
    fun setFullScreen(): Boolean {
        if (isFullScreen)
            return true
        val width: Int
        val height: Int
        var frequency = 60
        var colorDepth = 32
        val s = settings
        if (s != null) {
            width = s.screenWidth
            height = s.screenHeight
            frequency = s.refreshRateHz
            colorDepth = s.colorBitDepth
        } else {
            width = Display.getWidth()
            height = Display.getHeight()
        }
        mouseGrabbed = Mouse.isGrabbed()

        try {
            val modes = Display.getAvailableDisplayModes()
            for (i in modes.indices) {
                if (modes[i].width == width && modes[i].height == height
                        && modes[i].bitsPerPixel == colorDepth
                        && modes[i].frequency == frequency && modes[i].isFullscreenCapable) {
                    Display.setDisplayMode(modes[i])
                    Display.setFullscreen(true)
                    Mouse.setGrabbed(true)
                    return true
                }
            }
        } catch (e: LWJGLException) {
            logger.error("Cannot switch to full screen", e)
            return false
        }

        logger.warn("No full screen mode available for $width x $height x $colorDepth @${frequency}Hz")
        return false
    }

    companion object {
        private const val serialVersionUID = -2717407019091038128L

        private val logger = LoggerFactory.getLogger(FullScreenSwitcher::class.java)
    }
}

/**
 * Parses and saves keys definition from a properties file.
 *
 * @author Ondrej Milenovsky
 */
object KeysUtils {
    private val logger = LoggerFactory.getLogger(KeysUtils::class.java)

    @Throws(IOException::class)
    fun loadKeys(file: File, keys: Any) {
        val pr = Properties()
        pr.load(FileReader(file))
        for (key in ArrayList(pr.keys))
            convertKeyToInt(pr, key as String)
        PropertiesUtils.injectToObject(pr, keys, false)
    }

    @Throws(IOException::class)
    fun saveKeys(file: File, keys: Any) {
        val map = LinkedHashMap<String, String>()
        for (field in keys.javaClass.fields)
            try {
                val value = field.get(keys) as? Int
                value?.let { map[field.name] = Keyboard.getKeyName(it) }
            } catch (e: Exception) {
                logger.error("Error saving keys to " + file.absolutePath, e)
            }
        PropertiesUtils.saveMapAsProperties(file, map)
    }

    private fun convertKeyToInt(pr: Properties, key: String) {
        val value = pr.remove(key) as String
        val code = Keyboard.getKeyIndex(value)
        if (code == Keyboard.KEY_NONE)
            logger.warn("$key - unknown key: $value")
        else
            pr.setProperty(key, code.toString() + "")
    }
}

/**
 * Loaded texture.
 * The texture holds reference to buffered image, it should be cleared to save memory.
 *
 * @author Ondrej Milenovsky
 */
class Texture(val id: Int, var origImage: BufferedImage?) {

    val width = origImage!!.width
    val height = origImage!!.height

    fun bind() {
        if (lastBind !== this) {
            lastBind = this
            glEnable(GL_TEXTURE_2D)
            glBindTexture(GL_TEXTURE_2D, id)
        }
    }

    fun dispose() {
        clearOrigImage()
        TextureLoader.disposeTexture(id)
    }

    fun clearOrigImage() {
        origImage = null
    }

    companion object {
        private var lastBind: Texture? = null

        fun unbind() {
            lastBind = null
            glDisable(GL_TEXTURE_2D)
        }
    }
}

/**
 * Loads texture from file and uses in LWJGL. GL must be initialized before loading!
 *
 * Code found on Stack overflow and modified.
 */
object TextureLoader {

    private const val BYTES_PER_PIXEL = 4 //3 for RGB, 4 for RGBA
    /** If the color is set, then all pixels, that have alpha=0 are set to this color.
     * If texture filter is on, then the color makes difference even if its alpha=0.
     */
    var bgColor: Color? = null

    @Throws(IOException::class)
    fun loadTexture(file: File, texFilter: Boolean): Texture {
        //org.newdawn.slick.opengl.TextureLoader.getTexture("png", new FileInputStream(file));
        val img = ImageIO.read(file)
        return useTexture(img, texFilter)
    }

    fun useTexture(image: BufferedImage, texFilter: Boolean): Texture {

        val pixels = IntArray(image.width * image.height)
        image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)

        val buffer = BufferUtils.createByteBuffer(image.width * image.height
                * BYTES_PER_PIXEL) //4 for RGBA, 3 for RGB

        for (y in 0 until image.height)
            for (x in 0 until image.width) {
                var pixel = pixels[y * image.width + x]
                if (pixel shr 24 and 0xFF == 0)
                    pixel = bgColor!!.rgb
                buffer.put((pixel shr 16 and 0xFF).toByte()) // Red component
                buffer.put((pixel shr 8 and 0xFF).toByte()) // Green component
                buffer.put((pixel and 0xFF).toByte()) // Blue component
                buffer.put((pixel shr 24 and 0xFF).toByte()) // Alpha component. Only for RGBA
            }

        buffer.flip() //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:

        val textureId = glGenTextures() //Generate texture ID

        glBindTexture(GL_TEXTURE_2D, textureId) //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)

        //Setup texture scaling filtering
        if (texFilter) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        }

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA,
                GL_UNSIGNED_BYTE, buffer)

        //Return the texture ID so we can bind it later again
        return Texture(textureId, image)
    }

    fun disposeTexture(textureId: Int) = glDeleteTextures(textureId)
}