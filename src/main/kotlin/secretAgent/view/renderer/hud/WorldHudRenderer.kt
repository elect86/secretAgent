package secretAgent.view.renderer.hud

import cz.wa.secretagent.world.entity.agent.HumanAgent
import org.apache.commons.lang.StringUtils
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.game.PlayerHolder
import secretAgent.game.player.Camera
import secretAgent.view.renderer.PrimitivesDrawer
import secretAgent.view.renderer.Renderer
import secretAgent.world.SamWorld
import java.awt.Color

/**
 * Renders game world HUD (text, symbols) into the map.
 *
 * @author Ondrej Milenovsky
 */
class WorldHudRenderer : Renderer {

    lateinit var primitivesDrawer: PrimitivesDrawer
    var textSize: Double = 0.0

    override fun init() {} // empty

    override fun dispose() {} // empty

    fun drawHud(world: SamWorld, player: PlayerHolder, camera: Camera) {
        // TODO draw thread symbols
        drawDisplayedText(player.agent, player.displayedText, camera)
    }

    private fun drawDisplayedText(agent: HumanAgent, text: String?, camera: Camera) {
        if (StringUtils.isEmpty(text))
            return
        val pos = camera.getScreenPos(agent.pos.add(Vector2D(0.0, -agent.sizeBounds.height)))
        primitivesDrawer.drawText(text!!, pos, textSize * camera.scale, Color.WHITE)
    }

    companion object {
        private const val serialVersionUID = -6980609084106794684L
    }
}
