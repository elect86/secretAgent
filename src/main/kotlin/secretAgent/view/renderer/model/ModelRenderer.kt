package secretAgent.view.renderer.model

import cz.wa.secretagent.world.entity.Entity
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.game.player.Camera
import secretAgent.world.GLModel
import secretAgent.world.ModelType
import java.io.Serializable

/**
 * Renders any model using single drawers, decides which drawer will be used.
 *
 * @author Ondrej Milenovsky
 */
class ModelRenderer : Serializable {

    lateinit var drawers: Map<ModelType, ModelDrawer<GLModel>>

    /**
     * Draws the model
     * @param model model to draw
     * @param entity entity for the model, can be null
     * @param pos position of the model in world
     * @param camera camera
     */
    fun draw(model: GLModel, entity: Entity?, pos: Vector2D, camera: Camera) {
        // check if model in viewport
        val modelBounds = model.maxBounds.move(pos)
        val worldBounds = camera.worldBounds
        if (worldBounds != null && !modelBounds.intersects(worldBounds)) {
            return
        }

        // draw the model
        val type = model.type
        if (type != ModelType.EMPTY)
            drawers[type]?.draw(model, entity, camera.getScreenPos(pos), camera.scale)
                    ?: logger.warn("Unknown model type: $type")
    }

    companion object {
        private const val serialVersionUID = -3548741344017331536L
        private val logger = LoggerFactory.getLogger(ModelRenderer::class.java)
    }
}
