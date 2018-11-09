package secretAgent.view.renderer.gui

import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.view.Settings2D
import cz.wa.secretagent.view.renderer.PrimitivesDrawer
import cz.wa.secretagent.view.renderer.gui.ComponentRenderer
import cz.wa.secretagent.view.renderer.gui.FrameRenderer
import cz.wa.secretagent.view.texture.DrawBounds
import cz.wa.secretagent.worldinfo.WorldHolder
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.secretagent.worldinfo.graphics.TilesInfo
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.springframework.beans.factory.annotation.Required
import secretAgent.game.player.Camera
import secretAgent.view.model.SimpleModel
import secretAgent.view.renderer.model.ModelRenderer
import secretAgent.world.GLModel
import secretAgent.world.ModelType
import secretAgent.world.ObjectModel
import java.awt.Color
import java.io.Serializable

/**
 * Renders frame made of tiles.
 *
 * @author Ondrej Milenovsky
 */
class TileFrameRenderer : FrameRenderer, Serializable {

    @set:Required
    lateinit var worldHolder: WorldHolder
    @set:Required
    lateinit var modelRenderer: ModelRenderer
    @set:Required
    lateinit var primitivesDrawer: PrimitivesDrawer
    @set:Required
    lateinit var settings2d: Settings2D
    @set:Required
    lateinit var componentRenderer: ComponentRenderer

    override fun drawFrame(frame: GFrame) {
        val tileSet = worldHolder.graphicsInfo.getTileSet(GraphicsInfo.ORIG_GUI_SPRITES_ID)
        val camera = Camera(Vector2I(settings2d.screenWidth, settings2d.screenHeight),
                settings2d.screenHeight.toDouble() / tileSet.tileSize.y * frame.sizeSH.y / frame.sizeTiles.y)
        drawBorder(frame, tileSet, camera)
        drawMiddle(frame, tileSet, camera)
        drawContent(frame, camera)
    }

    /**
     * Draw only the border
     */
    private fun drawBorder(frame: GFrame, tileSet: TilesInfo, camera: Camera) {
        val grInfo = worldHolder.graphicsInfo
        val ul = grInfo.getModel(BORDER_UPPER_LEFT)
        val u = grInfo.getModel(BORDER_UPPER)
        val ur = grInfo.getModel(BORDER_UPPER_RIGHT)
        val r = grInfo.getModel(BORDER_RIGHT)
        val dr = grInfo.getModel(BORDER_LOWER_RIGHT)
        val d = grInfo.getModel(BORDER_LOWER)
        val dl = grInfo.getModel(BORDER_LOWER_LEFT)
        val l = grInfo.getModel(BORDER_LEFT)

        val td = TileDrawer(frame, camera)

        // corners
        primitivesDrawer.setColor(frame.borderColor, false)
        val drPos = frame.sizeTiles.substract(Vector2I(1, 1))
        td.drawTile(ul, Vector2I.ZERO)
        td.drawTile(ur, Vector2I(drPos.x, 0))
        td.drawTile(dr, drPos)
        td.drawTile(dl, Vector2I(0, drPos.y))

        // up and down horizontal borders
        for (i in 1 until drPos.x) {
            td.drawTile(u, Vector2I(i, 0))
            td.drawTile(d, Vector2I(i, drPos.y))
        }
        // left and right vertical borders
        for (i in 1 until drPos.y) {
            td.drawTile(l, Vector2I(0, i))
            td.drawTile(r, Vector2I(drPos.x, i))
        }
    }

    /**
     * Draw only the filling tiles.
     */
    private fun drawMiddle(frame: GFrame, tileSet: TilesInfo, camera: Camera) {
        val m = worldHolder.graphicsInfo.getModel(MIDDLE)
        val td = TileDrawer(frame, camera)

        primitivesDrawer.setColor(frame.middleColor, false)
        for (y in 1 until frame.sizeTiles.y - 1)
            for (x in 1 until frame.sizeTiles.x - 1)
                td.drawTile(m, Vector2I(x, y))
    }

    /**
     * Draw components on the frame. Will change the camera position.
     */
    private fun drawContent(frame: GFrame, camera: Camera) {
        primitivesDrawer.setColor(Color.WHITE, false)
        camera.pos = frame.posSH.scalarMultiply(settings2d.screenHeight.toDouble())
        for (component in frame.components)
            componentRenderer.renderComponent(component, frame, camera)
    }

    private inner class TileDrawer(private val frame: GFrame, private val camera: Camera) {
        private val bounds: DrawBounds

        init {
            val tw = frame.sizeSH.x * settings2d.screenHeight / frame.sizeTiles.x
            val th = frame.sizeSH.y * settings2d.screenHeight / frame.sizeTiles.y
            bounds = DrawBounds(0.0, 0.0, tw, th)
        }

        fun drawTile(model: ObjectModel, posGrid: Vector2I) {
            val posOnFrame = Vector2D(
                    posGrid.x * frame.sizeSH.x / frame.sizeTiles.x,
                    posGrid.y * frame.sizeSH.y / frame.sizeTiles.y)
            val pos = frame.posSH.add(posOnFrame).scalarMultiply(settings2d.screenHeight.toDouble())
            val glModel = model as GLModel
            if (glModel.type == ModelType.SIMPLE) {
                val m = glModel as SimpleModel
                primitivesDrawer.drawTexture(m.texture, pos, bounds, 1.0)
            } else
                modelRenderer.draw(glModel, null, pos, camera)
        }

    }

    companion object {

        private const val serialVersionUID = -1022332308847341159L

        private const val BORDER_UPPER_LEFT = "borderUL"
        private const val BORDER_UPPER = "borderU"
        private const val BORDER_UPPER_RIGHT = "borderUR"
        private const val BORDER_LOWER_RIGHT = "borderDR"
        private const val BORDER_LOWER_LEFT = "borderDL"
        private const val BORDER_LEFT = "borderL"
        private const val BORDER_LOWER = "borderD"
        private const val BORDER_RIGHT = "borderR"
        private const val MIDDLE = "middle"
    }

}