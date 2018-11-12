package secretAgent.io.campaign.model

import cz.wa.secretagent.worldinfo.graphics.ModelInfo
import secretAgent.io.tiles.ModelProperties
import secretAgent.view.model.SimpleModel
import secretAgent.view.renderer.TileId
import secretAgent.world.GLModel

/**
 * Creates simple model.
 *
 * @author Ondrej Milenovsky
 */
class SimpleModelCreator : AbstractModelCreator() {

    override fun createModel(modelProperties: ModelProperties, tileId: TileId, modelName: String): SimpleModel {
        val properties = modelProperties.properties
        // frame
        val tileID = getSingleTileId(properties, FRAME_PROPERTY, modelName) ?: tileId
        // scale
        val scaleTile = getSingleTileId(properties, SCALE_PROPERTY, modelName)
        val scale = scaleTile?.let(::tileToDouble) ?: 0.0

        return SimpleModel(tileID, scale)
    }

    override fun createModelInfo(tileId: TileId, model: GLModel, modelName: String) = ModelInfo(model)

    companion object {
        private const val serialVersionUID = 620155121207717964L
        private const val FRAME_PROPERTY = "frame"
    }
}