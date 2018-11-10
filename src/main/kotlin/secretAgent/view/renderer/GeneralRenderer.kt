package secretAgent.view.renderer

import cz.wa.secretagent.view.renderer.hud.WorldHUDRenderer
import cz.wa.secretagent.worldinfo.WorldHolder
import org.slf4j.LoggerFactory
import secretAgent.game.player.Camera
import secretAgent.view.renderer.gui.GuiRenderer
import secretAgent.view.renderer.hud.ScreenHudRenderer
import java.io.Serializable

/**
 * Renders the screen.
 *
 * @author Ondrej Milenovsky
 */
class GeneralRenderer : Renderer, Serializable {

    lateinit var settings: Settings2D

    lateinit var primitivesDrawer: PrimitivesDrawer
    lateinit var levelRenderer: LevelRenderer
    lateinit var entitiesRenderer: EntitiesRenderer
    lateinit var worldHudRenderer: WorldHUDRenderer
    lateinit var screenHudRenderer: ScreenHudRenderer
    lateinit var guiRenderer: GuiRenderer

    override fun init() {
        primitivesDrawer.init()
        levelRenderer.init()
        entitiesRenderer.init()
        worldHudRenderer.init()
        guiRenderer.init()
    }

    override fun dispose() {
        levelRenderer.dispose()
        entitiesRenderer.dispose()
        worldHudRenderer.dispose()
        guiRenderer.dispose()
        primitivesDrawer.dispose()
    }

    fun render(world: WorldHolder) {
        val menuHolder = world.menuHolder
        try {
            if (world.world != null && !world.world.isEmpty && !menuHolder.isSolid) {
                // draw scene
                initScene(world.playerHolder.camera)
                drawSceneByCamera(world)

                // draw HUD
                drawHudByCamera(world)
                initHud()
                drawHudOnScreen(world)
            }
        } catch (e: Throwable) {
            logger.error("Cannot draw scene", e)
        }

        try {
            if (menuHolder.isMenuActive)
                guiRenderer.drawGUI(menuHolder) // draw GUI
        } catch (e: Throwable) {
            logger.error("Cannot draw GUI", e)
        }

    }

    private fun drawHudByCamera(world: WorldHolder) {
        val playerHolder = world.playerHolder
        worldHudRenderer.drawHUD(world.world, playerHolder, playerHolder.camera)
    }

    private fun drawHudOnScreen(world: WorldHolder) {
        val playerHolder = world.playerHolder
        val bounds = playerHolder.camera.hudBounds!!
        primitivesDrawer.fillRect(bounds, world.graphicsInfo.bgColor)
        screenHudRenderer.drawHUD(playerHolder, bounds, world.graphicsInfo)
    }

    private fun drawSceneByCamera(world: WorldHolder) {
        val map = world.world
        val camera = world.playerHolder.camera
        levelRenderer.drawBackground(map.levelMap, camera)
        entitiesRenderer.drawEntities(map.entityMap, camera)
        levelRenderer.drawForeground(map.levelMap, camera)
        entitiesRenderer.drawOverEntities(map.entityMap, camera)
    }

    private fun initScene(camera: Camera) {
        primitivesDrawer.drawPosition = DrawPosition.CENTER
    }

    private fun initHud() {
        primitivesDrawer.drawPosition = DrawPosition.UPPER_LEFT
    }

    companion object {
        private const val serialVersionUID = -1940709352630583876L
        private val logger = LoggerFactory.getLogger(GeneralRenderer::class.java)
    }
}