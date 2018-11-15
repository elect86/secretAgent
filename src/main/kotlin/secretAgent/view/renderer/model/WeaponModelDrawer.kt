package secretAgent.view.renderer.model

import cz.wa.secretagent.worldinfo.WorldHolder
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.model.WeaponModel
import secretAgent.view.renderer.DrawBounds
import secretAgent.world.entity.Entity

/**
 * Draws weapon model, used when drawing on screen only!
 * Weapon in world is drawn with the agent.
 *
 * @author Ondrej Milenovsky
 */
class WeaponModelDrawer : AbstractModelDrawer<WeaponModel>() {

    lateinit var worldHolder: WorldHolder

    override fun draw(model: WeaponModel, entity: Entity?, pos: Vector2D, scale: Double) {
        val model2 = model.getModel(false)
        val textures = model2.textures
        val tex = AbstractModelDrawer.getFrame(worldHolder.world.levelMap.timeMs, model2.durationMs, textures!!)
        primitivesDrawer.drawTexture(tex!!, pos, DrawBounds(model.bounds), scale)
    }

    companion object {
        private const val serialVersionUID = 4014057635662109586L
    }
}
