package secretAgent.view.renderer.model

import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.explosion.Explosion
import cz.wa.secretagent.worldinfo.WorldHolder
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Required
import secretAgent.view.model.ExplosionModel
import secretAgent.view.renderer.DrawBounds

/**
 * Draws explosion model.
 *
 * @author Ondrej Milenovsky
 */
class ExplosionModelDrawer : AbstractModelDrawer<ExplosionModel>() {

    lateinit var worldHolder: WorldHolder

    override fun draw(model: ExplosionModel, entity: Entity?, pos: Vector2D, scale: Double) {
        if (entity !is Explosion) {
            logger.warn("Entity must be Explosion, but is: ${entity?.javaClass?.simpleName}")
            return
        }
        val timeMs = Math.round(entity.timeS * 1000)
        val durationMs = Math.round(entity.durationS * 1000)

        // draw the texture
        val textures = model.textures
        val tex = AbstractModelDrawer.getFrame(timeMs, durationMs, textures!!)
        val radius = entity.radius
        val height = tex!!.tileBounds.height / tex.tileBounds.width
        val bounds = Rectangle2D(-radius, -radius * height * 2.0 + radius, radius * 2.0, radius * height * 2.0)
        primitivesDrawer.drawTexture(tex, pos, DrawBounds(bounds), scale)
    }

    companion object {
        private const val serialVersionUID = -7708654087126840532L
        private val logger = LoggerFactory.getLogger(ExplosionModelDrawer::class.java)
    }
}