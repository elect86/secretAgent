package secretAgent.view.renderer

import cz.wa.secretagent.utils.lwjgl.FullScreenSwitcher
import cz.wa.secretagent.worldinfo.WorldHolder
import org.apache.commons.io.IOUtils
import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.newdawn.slick.opengl.PNGDecoder
import org.slf4j.LoggerFactory
import org.lwjgl.opengl.GL11.*
import secretAgent.view.SamRenderer
import secretAgent.view.Settings2D
import java.awt.Color
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.net.URL
import java.nio.ByteBuffer


/**
 * Renders the world using LWJGL.
 *
 * @author Ondrej Milenovsky
 */
class GLRenderer : SamRenderer, Serializable {

    @Transient
    override var isInitialized = false

    lateinit var settings: Settings2D
    lateinit var fullScreenSwitcher: FullScreenSwitcher
    lateinit var generalRenderer: GeneralRenderer

    @Transient
    private val lastBgColor: Color? = null

    override val isCloseRequested: Boolean
        get() = Display.isCloseRequested()

    override fun init() {
        try {
            Display.setLocation(settings.screenPosX, settings.screenPosY)
            if (settings.fullScreen)
                fullScreenSwitcher.setFullScreen()
            else
                Display.setDisplayMode(DisplayMode(settings.screenWidth, settings.screenHeight))
            Display.setVSyncEnabled(settings.vsync)
            Display.setTitle("Secret Agent")
            val icons = arrayOf(loadIcon(ClassLoader.getSystemResource("icon16.png"))!!, loadIcon(ClassLoader.getSystemResource("icon32.png"))!!)
            Display.setIcon(icons)
            Display.create()
            isInitialized = true
        } catch (e: LWJGLException) {
            logger.error("Cannot init display", e)
            return
        } catch (e: UnsatisfiedLinkError) {
            logger.error("Missing LWJGL binares, see 'start_help.txt' for instructions", e)
            return
        }

        initGL()
        generalRenderer.init()
    }

    private fun initGL() {
        glEnable(GL_TEXTURE_2D)
        glShadeModel(GL_SMOOTH)
        glClearDepth(0.1)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)

        // enable transparent textures
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        if (settings.texFilter) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        }

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, settings.screenWidth.toDouble(), settings.screenHeight.toDouble(), 0.0, -1.0, 1001.0)
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()
    }

    override fun draw(world: WorldHolder) {
        if (!isInitialized)
            return
        try {
            drawFrameImpl(world)
        } catch (e: Throwable) {
            logger.error("", e)
            if (e.message != null && e.message == "No OpenGL context found in the current thread.")
                isInitialized = false
        }

    }

    private fun drawFrameImpl(world: WorldHolder) {
        clearGL(world.graphicsInfo.bgColor)
        generalRenderer.render(world)
        swapBuffers()
    }

    private fun clearGL(bgColor: Color) {
        if (bgColor != lastBgColor)
            glClearColor(bgColor.red / 255f, bgColor.green / 255f, bgColor.blue / 255f, bgColor.alpha / 255f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    private fun swapBuffers() {
        Display.update()
        Display.sync(settings.fps)
    }

    override fun dispose() {
        generalRenderer.dispose()
        Display.destroy()
    }

    companion object {
        private const val serialVersionUID = -5530236746180863704L

        private val logger = LoggerFactory.getLogger(GLRenderer::class.java)

        private fun loadIcon(url: URL): ByteBuffer? {
            var stream: InputStream? = null
            return try {
                stream = url.openStream()
                val decoder = PNGDecoder(stream)
                val bb = ByteBuffer.allocateDirect(decoder.width * decoder.height * 4)
                decoder.decode(bb, decoder.width * 4, PNGDecoder.RGBA)
                bb.flip()
                bb
            } catch (e: IOException) {
                logger.error("Loading icon $url", e)
                null
            } finally {
                IOUtils.closeQuietly(stream)
            }
        }
    }

}
