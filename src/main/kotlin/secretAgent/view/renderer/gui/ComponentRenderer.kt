package secretAgent.view.renderer.gui

import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.menu.window.component.ComponentType
import cz.wa.secretagent.menu.window.component.GComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Required
import secretAgent.game.player.Camera
import java.io.Serializable

/**
 * Class that renders any component (except frame).
 *
 * @author Ondrej Milenovsky
 */
class ComponentRenderer : Serializable {

    @set:Required
    lateinit var componentDrawers: Map<ComponentType, ComponentDrawer<GComponent>>

    fun renderComponent(component: GComponent, frame: GFrame, camera: Camera) {
        val type = component.type
        componentDrawers[type]?.draw(component, frame, camera) ?: logger.warn("No renderer for component type: $type")
    }

    companion object {
        private const val serialVersionUID = -7689554478280250593L
        private val logger = LoggerFactory.getLogger(ComponentRenderer::class.java)
    }
}