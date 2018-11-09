package secretAgent.view.renderer.model

import cz.wa.secretagent.view.texture.DrawBounds
import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.HasDuration
import cz.wa.secretagent.world.entity.HasModelAngle
import cz.wa.secretagent.world.entity.HasTime
import cz.wa.secretagent.worldinfo.WorldHolder
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.springframework.beans.factory.annotation.Required
import secretAgent.view.model.AnimatedModel

/**
 * Draws animated model.
 *
 * @author Ondrej Milenovsky
 */
class AnimatedModelDrawer : AbstractModelDrawer<AnimatedModel>() {

    lateinit var worldHolder: WorldHolder

    override fun draw(model: AnimatedModel, entity: Entity?, pos: Vector2D, scale: Double) {
        // time from the entity or from level
        val timeMs = when (entity) {
            is HasTime -> (entity as HasTime).timeMs
            else -> worldHolder.world.levelMap.timeMs
        }

        // duration from the entity or from the model
        val durationMs = when (entity) {
            is HasDuration -> (entity as HasDuration).durationMs
            else -> model.durationMs
        }

        // draw the texture
        val textures = model.textures
        val tex = AbstractModelDrawer.getFrame(timeMs, durationMs, textures!!)
        if (entity is HasModelAngle)
            primitivesDrawer.drawTexture(tex, pos, DrawBounds(model.bounds), scale, (entity as HasModelAngle).modelAngle)
        else
            primitivesDrawer.drawTexture(tex, pos, DrawBounds(model.bounds), scale)
    }

    companion object {
        private const val serialVersionUID = 6233405165118049099L
    }
}