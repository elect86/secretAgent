package secretAgent.view.renderer.hud

import cz.wa.secretagent.world.weapon.WeaponOrder
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Rectangle2I
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.game.PlayerHolder
import secretAgent.game.player.Camera
import secretAgent.view.DrawPosition
import secretAgent.view.Renderer
import secretAgent.view.Settings2D
import secretAgent.view.model.HealthBarModel
import secretAgent.view.renderer.DrawBounds
import secretAgent.view.renderer.PrimitivesDrawer
import secretAgent.view.renderer.TextureToDraw
import secretAgent.view.renderer.model.ModelRenderer
import secretAgent.world.GLModel
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.ItemType
import secretAgent.world.entity.KeyItem
import secretAgent.world.entity.agent.AgentEntity
import java.awt.Color
import java.util.*

/**
 * Renders health, weapons, keys on the screen
 *
 * @author Ondrej Milenovsky
 */
class ScreenHudRenderer : Renderer {

    lateinit var primitivesDrawer: PrimitivesDrawer
    lateinit var modelRenderer: ModelRenderer
    lateinit var settings2d: Settings2D
    lateinit var weaponOrder: WeaponOrder

    override fun init() {} // empty

    override fun dispose() {} // empty

    fun drawHUD(player: PlayerHolder, bounds: Rectangle2I, graphicsInfo: GraphicsInfo) {
        val size = bounds.height.toDouble()
        val addPos = Vector2D(bounds.x.toDouble(), bounds.y.toDouble())
        drawHealthBar(player.agent, graphicsInfo, addPos, size)
        drawInventory(player.agent, addPos, size)
        drawWeapons(player.agent, addPos, size)
        // TODO draw power ups
    }

    /**
     * Draw health bar to the upper left corner
     */
    private fun drawHealthBar(agent: AgentEntity, graphicsInfo: GraphicsInfo, addPos: Vector2D, size: Double) {
        val model = graphicsInfo.getModel(HB_MODEL) as? HealthBarModel
        if (model == null) {
            logger.warn("Missing model: $HB_MODEL")
            return
        }
        // get quad coords
        var drawBounds = DrawBounds(HB_BOUNDS)
        val pos = HB_POS.scalarMultiply(size).add(addPos)
        // draw frame
        primitivesDrawer.drawTexture(model.frameTexture!!, pos, drawBounds, size)

        // modify health quad and tex coords
        var healthTex = model.healthTexture!!
        val hp = agent.health / agent.capabilities.maxHealth
        if (hp < 1) {
            // tex coords
            val hb = healthTex.tileBounds
            healthTex = TextureToDraw(healthTex.texture,
                    Rectangle2D(hb.x, hb.y, hb.width * hp, hb.height))
            // quad coords
            val x1 = drawBounds.x1
            val x2 = drawBounds.x2
            drawBounds = DrawBounds(x1, drawBounds.y1, x1 + hp * (x2 - x1), drawBounds.y2)
        }
        // draw health
        primitivesDrawer.drawTexture(healthTex, pos, drawBounds, size)
    }

    /**
     * Draw all keys under the health bar
     */
    private fun drawInventory(agent: HumanAgent, addPos: Vector2D, size: Double) {
        // get all items and group them by lock
        val items = LinkedHashMap<String, ArrayList<KeyItem>>()
        for (entity in agent.inventory.getItems(ItemType.KEY)) {
            val key = entity as KeyItem
            val lock = key.lockType
            items.getOrPut(lock) { ArrayList(4) }.add(key)
        }

        // draw the items
        val camera = Camera(Vector2I(settings2d.screenWidth, settings2d.screenHeight),
                INV_SCALE * size)
        var lastX = INV_POS.x * size
        val posY = INV_POS.y * size
        for (list in items.values) {
            for (key in list) {
                val model = key.model
                if (model is GLModel)
                    modelRenderer.draw(model, key, Vector2D(lastX, posY).add(addPos), camera)
                else
                    logger.warn("Unknown KeyItem model: ${model.javaClass.simpleName}")
                lastX += INV_GAP * size
            }
        }
    }

    /**
     * Draw weapons with ammo and selected frame
     */
    private fun drawWeapons(agent: HumanAgent, addPos: Vector2D, size: Double) {
        val camera = Camera(Vector2I(settings2d.screenWidth, settings2d.screenHeight),
                WEAP_SCALE * size)
        val inventory = agent.inventory
        val weapons = inventory.weapons
        var lastX = WEAP_POS.x * size
        val weapPosY = WEAP_POS.y * size
        val ammoPosY = AMMO_POS.y * size
        for (weapon in weaponOrder) {
            // selected weapon
            if (agent.weapon === weapon) {
                val rect = WEAP_SELECTED_RECT.multiply(size)
                        .move(Vector2D(lastX, weapPosY).add(addPos))
                primitivesDrawer.fillRect(Rectangle2I(rect), WEAP_SELECTED_COLOR)
            }
            // draw weapon
            val model = weapon.model
            val hasWeapon = weapons.contains(weapon)
            if (model is GLModel) {
                val pos = Vector2D(lastX, weapPosY).add(addPos)
                if (!hasWeapon)
                    primitivesDrawer.setTexColor(WEAP_MISSING_COLOR)
                primitivesDrawer.drawPosition = DrawPosition.CENTER
                modelRenderer.draw(model, agent, pos, camera)
                primitivesDrawer.drawPosition = DrawPosition.UPPER_LEFT
            } else
                logger.warn("Weapon '${weapon.name}' has unknown model: ${model.javaClass.simpleName}")
            // draw ammo
            val pos = Vector2D(lastX + AMMO_POS.x * size, ammoPosY).add(addPos)
            val color: Color
            color = if (hasWeapon) AMMO_WEAP_COLOR else AMMO_COLOR
            primitivesDrawer.drawPosition = DrawPosition.UPPER_RIGHT
            primitivesDrawer.drawText(inventory.getAmmo(weapon).toString() + "", pos, AMMO_SIZE * size, color)
            primitivesDrawer.drawPosition = DrawPosition.CENTER

            lastX += WEAP_GAP * size
        }
    }

    companion object {
        private const val serialVersionUID = -7577907590623804209L

        private val logger = LoggerFactory.getLogger(ScreenHudRenderer::class.java)

        private const val SIZE = 48.0

        private const val HB_MODEL = "healthBar"
        private val HB_POS = Vector2D(5.5 / SIZE, 5.5 / SIZE)
        private val HB_BOUNDS = Rectangle2D(-44.0 / SIZE, -5.5 / SIZE, 88.0 / SIZE,
                11.0 / SIZE)

        private val INV_POS = Vector2D(5.5 / SIZE, 22.0 / SIZE)
        private val INV_SCALE = 1 / SIZE
        private val INV_GAP = 16 / SIZE

        private val WEAP_POS = Vector2D(142.0 / SIZE, 14 / SIZE)
        private val WEAP_GAP = 48.0 / SIZE
        private val WEAP_SCALE = 2.5 / SIZE
        private val WEAP_MISSING_COLOR = Color(60, 60, 60, 128)
        private val WEAP_SELECTED_COLOR = Color(200, 200, 200, 128)
        private val WEAP_SELECTED_RECT = Rectangle2D(-WEAP_GAP / 2, -9.5 / SIZE,
                WEAP_GAP, 38.5 / SIZE)
        private val AMMO_POS = Vector2D(16 / SIZE, 24 / SIZE)
        private val AMMO_WEAP_COLOR = Color(190, 190, 190)
        private val AMMO_COLOR = Color(100, 100, 100)
        private val AMMO_SIZE = 16 / SIZE

        protected fun getFrameNum(currTimeMs_: Long, durationMs: Long, textures: List<*>): Int {
            val currTimeMs = currTimeMs_ % durationMs
            return (currTimeMs / durationMs.toDouble() * textures.size).toInt()
        }
    }

}
