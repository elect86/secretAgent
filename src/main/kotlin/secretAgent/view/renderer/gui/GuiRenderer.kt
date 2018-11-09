package secretAgent.view.renderer.gui

import cz.wa.secretagent.menu.MenuHolder
import cz.wa.secretagent.view.Settings2D
import cz.wa.secretagent.view.renderer.DrawPosition
import cz.wa.secretagent.view.renderer.PrimitivesDrawer
import cz.wa.secretagent.view.renderer.Renderer
import cz.wa.secretagent.view.renderer.gui.FrameRenderer
import org.springframework.beans.factory.annotation.Required
import java.io.Serializable

/**
 * Renders menu windows (options).
 *
 * @author Ondrej Milenovsky
 */
class GuiRenderer : Renderer, Serializable {

    @set:Required
    lateinit var frameRenderer: FrameRenderer
    @set:Required
    lateinit var settings2d: Settings2D
    @set:Required
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