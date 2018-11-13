package secretAgent.view.renderer.gui

import cz.wa.secretagent.view.renderer.gui.ComponentRenderer
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.springframework.beans.factory.annotation.Required
import secretAgent.game.player.Camera
import secretAgent.menu.window.GFrame
import secretAgent.menu.window.component.GComponent
import secretAgent.menu.window.component.GImage
import secretAgent.menu.window.component.GLabel
import secretAgent.menu.window.component.GSelectable
import secretAgent.view.renderer.PrimitivesDrawer
import secretAgent.view.renderer.model.ModelRenderer
import secretAgent.world.GLModel
import java.awt.Color
import java.io.Serializable

abstract class AbstractComponentDrawer<C : GComponent> : ComponentDrawer<C> {

    lateinit var primitivesDrawer: PrimitivesDrawer

    protected val screenHeight: Double
        get() = primitivesDrawer.settings.screenHeight.toDouble()

    protected fun getDrawPos(component: GComponent, camera: Camera): Vector2D =
        component.posSH.scalarMultiply(screenHeight).add(camera.pos)

    protected fun getDrawTextH(component: GComponent): Double = component.sizeSH.y * screenHeight

    companion object {
        private val serialVersionUID = -7861001462889943667L
    }
}

/**
 * Draws single GComponent.
 *
 * @author Ondrej Milenovsky
 */
interface ComponentDrawer<C : GComponent> : Serializable {

    /**
     * Draws the component.
     * @param component component to draw
     * @param frame parent frame
     * @param camera camera
     */
    fun draw(component: C, frame: GFrame, camera: Camera)
}

/**
 * Draws the model of the image.
 *
 * @author Ondrej Milenovsky
 */
class ImageComponentDrawer : AbstractComponentDrawer<GImage>() {

    @set:Required
    lateinit var modelRenderer: ModelRenderer

    override fun draw(component: GImage, frame: GFrame, camera: Camera) =
        modelRenderer.draw(component.model as GLModel, null, component.posSH, camera)

    companion object {
        private val serialVersionUID = 5453934946621231128L
    }
}

/**
 * Draws the string.
 *
 * @author Ondrej Milenovsky
 */
class LabelComponentDrawer : AbstractComponentDrawer<GLabel>() {

    override fun draw(component: GLabel, frame: GFrame, camera: Camera) =
        primitivesDrawer.drawText(component.text, getDrawPos(component, camera), getDrawTextH(component), component.color)

    companion object {
        private val serialVersionUID = 6905293103157502862L
    }
}

/**
 * Draws the display component and selected frame around it.
 *
 * @author Ondrej Milenovsky
 */
class SelectedComponentDrawer : AbstractComponentDrawer<GSelectable>() {

    lateinit var selectedColor: Color

    lateinit var notSelectedColor: Color

    @set:Required
    var borderWidthSH: Double = 0.0

    @set:Required
    var componentRenderer: ComponentRenderer? = null

    override fun draw(component: GSelectable, frame: GFrame, camera: Camera) {
        // the frame
        val color = if (component.isSelected) selectedColor else notSelectedColor
        val screenHeight = primitivesDrawer.settings.screenHeight
        var rect = Rectangle2D(component.posSH.x, component.posSH.y, component.sizeSH.x, component.sizeSH.y)
        rect = rect.scalarMultiply(screenHeight).move(camera.pos)
        primitivesDrawer.drawRect(rect, borderWidthSH * screenHeight, color)
        // the sub component
        componentRenderer!!.renderComponent(component.displayComponent, frame, camera)
    }

    companion object {
        private val serialVersionUID = -650455454002484030L
    }
}