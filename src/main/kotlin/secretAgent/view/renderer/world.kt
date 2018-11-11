package secretAgent.view.renderer

import cz.wa.secretagent.world.EntityMap
import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.EntityOrder
import cz.wa.secretagent.world.map.LevelMap
import cz.wa.secretagent.world.map.Tile
import cz.wa.wautils.collection.Array2D
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.slf4j.LoggerFactory
import secretAgent.game.player.Camera
import secretAgent.view.Renderer
import secretAgent.view.renderer.model.ModelRenderer
import secretAgent.world.GLModel
import secretAgent.world.ObjectModel

/**
 * Abstract world renderer.
 *
 * @author Ondrej Milenovsky
 */
abstract class AbstractWorldRenderer : Renderer {

    lateinit var modelRenderer: ModelRenderer

    protected fun renderModel(entity: Entity?, camera: Camera, model: ObjectModel, pos: Vector2D) {
        if (model is GLModel)
            modelRenderer.draw(model, entity, pos, camera)
        else
            logger.warn("Unknown model class: " + model.javaClass.simpleName)
    }

    companion object {
        private const val serialVersionUID = 3479516547509351802L
        private val logger = LoggerFactory.getLogger(AbstractWorldRenderer::class.java)
    }
}

/**
 * Renders all entities (items, agents, explosions).
 *
 * @author Ondrej Milenovsky
 */
class EntitiesRenderer : AbstractWorldRenderer() {

    /** Entities drawn between level background and foreground  */
    lateinit var entityOrder: EntityOrder
    /** Entities drawn over level foreground  */
    lateinit var entityOverOrder: EntityOrder

    override fun init() {} // empty

    override fun dispose() {} // empty

    fun drawEntities(entityMap: EntityMap, camera: Camera) {
        for (entity in entityOrder.getAllEntities(entityMap))
            renderModel(entity, camera, entity.model, entity.pos)
    }

    fun drawOverEntities(entityMap: EntityMap, camera: Camera) {
        for (entity in entityOverOrder.getAllEntities(entityMap))
            renderModel(entity, camera, entity.model, entity.pos)
    }

    companion object {
        private const val serialVersionUID = -8370881564278955844L
    }
}

/**
 * Renders the level (background, walls, spikes, tables)
 *
 * @author Ondrej Milenovsky
 */
class LevelRenderer : AbstractWorldRenderer() {

    override fun init() {} // empty

    override fun dispose() {} // empty

    /**
     * Background tiles, should be called before drawing entities.
     * @param level
     * @param camera
     */
    fun drawBackground(level: LevelMap, camera: Camera) = drawInternal(level, camera, level.background)

    /**
     * Foreground tiles, should be called after drawing entities.
     * @param level
     * @param camera
     */
    fun drawForeground(level: LevelMap, camera: Camera) = drawInternal(level, camera, level.foreground)

    private fun drawInternal(level: LevelMap, camera: Camera, map: Array2D<List<Tile>>) {
        val bounds = camera.worldBounds
        val tileSize = level.tileSize

        var x1 = FastMath.floor(bounds!!.x / tileSize.x + 0.5).toInt()
        var x2 = FastMath.ceil(bounds.x2 / tileSize.x + 0.5).toInt() - 1
        var y1 = FastMath.floor(bounds.y / tileSize.y + 0.5).toInt()
        var y2 = FastMath.ceil(bounds.y2 / tileSize.y + 0.5).toInt() - 1

        x1 = FastMath.max(x1, 0)
        x2 = FastMath.min(x2, level.size.x - 1)
        y1 = FastMath.max(y1, 0)
        y2 = FastMath.min(y2, level.size.y - 1)

        for (y in y1..y2)
            for (x in x1..x2) {
                val pos = Vector2D(x * tileSize.x, y * tileSize.y)
                drawTiles(level, map, camera, Vector2I(x, y), pos)
            }
    }

    /**
     * Draw tiles at single cell
     */
    private fun drawTiles(level: LevelMap, map: Array2D<List<Tile>>, camera: Camera, p: Vector2I, pos: Vector2D) {
        for (tile in map.get(p))
            renderModel(null, camera, tile.model, pos)
    }

    companion object {
        private const val serialVersionUID = -4093978814290475364L
    }
}