package secretAgent.view.renderer.model

import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.laser.LaserEntity
import cz.wa.secretagent.world.entity.laser.LaserType
import cz.wa.secretagent.world.entity.laser.LineLaser
import cz.wa.secretagent.worldinfo.WorldHolder
import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.VectorUtils
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Required
import secretAgent.view.model.LaserModel
import secretAgent.view.renderer.DrawBounds
import secretAgent.view.renderer.TextureToDraw

/**
 * Draws simple not animated model.
 *
 * @author Ondrej Milenovsky
 */
class LaserModelDrawer : AbstractModelDrawer<LaserModel>() {

    lateinit var worldHolder: WorldHolder

    override fun draw(model: LaserModel, entity: Entity?, pos: Vector2D, scale: Double) {
        if (entity !is LaserEntity) {
            logger.warn("Entity must be laser, but is: ${entity?.javaClass?.simpleName}")
            return
        }

        val textures = model.textures
        if (textures == null) {
            logger.warn("Laser model has not linked textures: " + entity.secondType)
            return
        }
        val timeMs = worldHolder.world.levelMap.timeMs
        var tex = AbstractModelDrawer.getFrame(timeMs, model.durationMs, textures)
        if (entity.secondType == LaserType.LINE) {
            val line = entity as LineLaser
            val length = line.pos.distance(line.pos2)
            val angle = VectorUtils.getAngle(line.pos, line.pos2)
            val width = line.width
            tex = getTextureToDraw(tex!!, length / width)
            val bounds = DrawBounds(0.0, -width / 2.0, length, width / 2.0)
            // draw laser
            primitivesDrawer.setTexColor(model.color)
            primitivesDrawer.drawTexture(tex, pos, bounds, scale, angle)
        } else {
            // modify texture bounds
            val length = entity.sizeBounds.width / entity.sizeBounds.height
            tex = getTextureToDraw(tex!!, length)
            // draw laser
            primitivesDrawer.setTexColor(model.color)
            primitivesDrawer.drawTexture(tex, pos, DrawBounds(entity.sizeBounds), scale)
        }
    }

    private fun getTextureToDraw(tex: TextureToDraw, length: Double): TextureToDraw {
        var tb = tex.tileBounds
        tb = Rectangle2D(tb.x, tb.y, length * tb.height, tb.height)
        return TextureToDraw(tex.texture, tb)
    }

    companion object {
        private const val serialVersionUID = -7866747539212406576L
        private val logger = LoggerFactory.getLogger(LaserModelDrawer::class.java)
    }
}