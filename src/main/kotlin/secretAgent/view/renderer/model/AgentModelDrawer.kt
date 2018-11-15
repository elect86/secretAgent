package secretAgent.view.renderer.model

import cz.wa.secretagent.world.weapon.Weapon
import cz.wa.secretagent.worldinfo.WorldHolder
import cz.wa.wautils.math.Rectangle2D
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.slf4j.LoggerFactory
import secretAgent.view.model.AgentModel
import secretAgent.view.model.WeaponModel
import secretAgent.view.renderer.DrawBounds
import secretAgent.view.renderer.TextureToDraw
import secretAgent.world.entity.Entity
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.AgentAction

/**
 * Draws agent and his weapon.
 *
 * @author Ondrej Milenovsky
 */
class AgentModelDrawer : AbstractModelDrawer<AgentModel>() {

    lateinit var worldHolder: WorldHolder
    /** speed of animation (relative to agent's speed)  */
    var animSpeed: Double = 0.0

    override fun draw(model: AgentModel, entity: Entity?, pos: Vector2D, scale: Double) {
        if (entity !is HumanAgent) {
            logger.warn("The entity must be instance of Agent, but is ${entity?.javaClass?.simpleName}")
            return
        }

        // bounds and texture list
        val bounds: DrawBounds
        val txs: List<TextureToDraw>?
        val dir = entity.direction
        val action = entity.currentAction

        if (dir == EntityXDirection.LEFT) {
            var lTex = model.leftTextures
            if (lTex != null)
                bounds = DrawBounds(model.bounds)
            else {
                bounds = DrawBounds(model.bounds, true)
                lTex = model.rightTextures
            }
            txs = lTex!!.getTextures(action)
        } else {
            var rTex = model.rightTextures
            if (rTex != null)
                bounds = DrawBounds(model.bounds)
            else {
                bounds = DrawBounds(model.bounds, true)
                rTex = model.leftTextures
            }
            txs = rTex!!.getTextures(action)
        }

        // texture from list
        val tex: TextureToDraw
        val timeMs = entity.actionTime
        tex = when {
            txs!!.size > 1 -> {
                val durationMs = when (action) {
                    AgentAction.STAY -> model.stayDurationMs
                    else -> FastMath.round(1000.0 / (animSpeed * entity.speed.add(entity.moveSpeed).norm))
                }
                AbstractModelDrawer.getFrame(timeMs, durationMs, txs)!!
            }
            else -> txs[0]
        }
        // draw
        primitivesDrawer.drawTexture(tex, pos, bounds, scale)
        if (entity.weapon != null) {
            var weaponPos = model.weaponCenter.scalarMultiply(scale)
            if (entity.direction == EntityXDirection.LEFT)
                weaponPos = Vector2D(-weaponPos.x, weaponPos.y)
            weaponPos = weaponPos.add(pos)
            drawWeapon(entity, entity.weapon!!, weaponPos, scale)
        }
    }

    private fun drawWeapon(agent: HumanAgent, weapon: Weapon, pos: Vector2D, scale: Double) {
        val model1 = weapon.model
        if (model1 == null) {
            logger.warn("Weapon '" + weapon.name + "' has not linked model")
            return
        }
        if (model1 !is WeaponModel) {
            logger.warn("Weapon '" + weapon.name + "' has unknown model: "
                    + model1.javaClass.simpleName)
            return
        }
        // get all current values from agent
        var angle = agent.aimAngle
        val aimLeft = agent.direction == EntityXDirection.LEFT
        val reloadingS = FastMath.round(agent.reloadTimeRemainingS * 1000)
        if (aimLeft)
            angle = -angle
        val reloadTimeS = FastMath.round(weapon.reloadTimeS * 1000)
        val firing = reloadingS > 0
        // get parameters from model
        val modelScale = model1.scale
        var wCenter = model1.getCenter(firing).scalarMultiply(-modelScale)
        if (aimLeft)
            wCenter = Vector2D(-wCenter.x, wCenter.y)
        val textures = model1.getModel(firing).textures
        val tex = when {
            reloadingS > 0 -> AbstractModelDrawer.getFrame(reloadTimeS - reloadingS, reloadTimeS, textures!!)
            else -> AbstractModelDrawer.getFrame(worldHolder.world.levelMap.timeMs, model1.getModel(false).durationMs, textures!!)
        }
        if (tex == null) {
            logger.warn("Weapon model '" + weapon.modelName + "' has not linked textures")
            return
        }
        // compute canvas coords
        val texWidth = tex.texture.width * modelScale
        val texHeight = tex.texture.height * modelScale
        val rect = Rectangle2D(wCenter.x - texWidth / 2.0, wCenter.y - texHeight / 2.0, texWidth, texHeight)
        val bounds = DrawBounds(rect)
        // draw the weapon
        primitivesDrawer.drawTexture(tex, pos, bounds, scale, angle, aimLeft)
    }

    companion object {
        private const val serialVersionUID = -3663846828569231873L
        private val logger = LoggerFactory.getLogger(AgentModelDrawer::class.java)
    }
}