package secretAgent.view.renderer.gui

import cz.wa.secretagent.view.renderer.gui.FrameRenderer
import org.springframework.beans.factory.annotation.Required
import secretAgent.menu.MenuHolder
import secretAgent.view.DrawPosition
import secretAgent.view.renderer.PrimitivesDrawer
import secretAgent.view.Renderer
import secretAgent.view.Settings2D
import java.io.Serializable

/**
 * Renders menu windows (options).
 *
 * @author Ondrej Milenovsky
 */
class GuiRenderer : Renderer, Serializable {

    lateinit var frameRenderer: FrameRenderer
    lateinit var settings2d: Settings2D
    lateinit var primitivesDrawer: PrimitivesDrawer

    override fun init() {} // empty

    override fun dispose() {} // empty

    fun drawGUI(menuHolder: MenuHolder) {
        primitivesDrawer.drawPosition = DrawPosition.UPPER_LEFT
        for (frame in menuHolder.frames)
            frameRenderer.drawFrame(frame)  // draw the window
        primitivesDrawer.drawPosition = DrawPosition.CENTER
    }

    companion object {
        private const val serialVersionUID = -8156972018160404103L
    }
}