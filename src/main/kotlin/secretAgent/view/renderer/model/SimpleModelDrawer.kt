package secretAgent.view.renderer.model

import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.HasModelAngle
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.model.SimpleModel
import secretAgent.view.renderer.DrawBounds

/**
 * Draws simple not animated model.
 *
 * @author Ondrej Milenovsky
 */
class SimpleModelDrawer : AbstractModelDrawer<SimpleModel>() {

    override fun draw(model: SimpleModel, entity: Entity?, pos: Vector2D, scale: Double) {
        if (entity is HasModelAngle)
            primitivesDrawer.drawTexture(model.texture!!, pos, DrawBounds(model.bounds), scale, (entity as HasModelAngle).modelAngle)
        else
            primitivesDrawer.drawTexture(model.texture!!, pos, DrawBounds(model.bounds), scale)
    }

    companion object {
        private const val serialVersionUID = -21991880645460102L
    }
}