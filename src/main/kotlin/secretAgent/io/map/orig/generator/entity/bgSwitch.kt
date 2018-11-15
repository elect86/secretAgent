package secretAgent.io.map.orig.generator.entity

import cz.wa.secretagent.world.entity.bgswitch.SimpleSwitch
import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity
import cz.wa.secretagent.world.entity.bgswitch.SwitchType
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.entity.bgSwitch.AddTilesSwitchAction
import secretAgent.world.entity.bgSwitch.DisableLaserSwitchAction
import secretAgent.world.entity.bgSwitch.SwitchAction
import java.util.LinkedHashSet

/**
 * Creates switches.
 *
 * @author Ondrej Milenovsky
 */
class SwitchEntityCreator : TypeEntityCreator<SwitchEntity>() {

    override fun getEnum(arg0: String): Any = SwitchType.valueOf(arg0)

    companion object {
        private val serialVersionUID = 4872658696537751315L
    }
}

/**
 * Creates simple switch.
 *
 * @author Ondrej Milenovsky
 */
class SimpleSwitchEntityCreator : EntityCreator<SimpleSwitch> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): SimpleSwitch? {
        if (args.size < 4) {
            logger.warn("Not enough arguments for switch: $tileId")
        } else {
            val lockType = args.removeAt(0)
            val singleUse = getSingleUse(args.removeAt(0), tileId)
            val description = args.removeAt(0)
            val action = createAction(args, tileId)!!
            return SimpleSwitch(model, pos, lockType, singleUse, description, listOf(action))
        }
        return null
    }

    private fun getSingleUse(str: String, tileId: TileId): Boolean {
        return when (str) {
            ONCE_USE -> true
            REPEAT_USE -> false
            else -> true.also { logger.warn("Unknown use type '$str' for switch: $tileId") }
        }
    }

    private fun createAction(args: MutableList<String>, tileId: TileId): SwitchAction? {
        val actionType = args.removeAt(0)
        return when (actionType) {
            DISABLE_LASER_ACTION -> DisableLaserSwitchAction()
            ADD_TILES_ACTION -> {
                if (args.isEmpty()) {
                    logger.warn("Missing tile ids for action ADD_TILES for switch: $tileId")
                    null
                } else
                    AddTilesSwitchAction(parseTileIds(args, tileId))
            }
            else -> null.also { logger.warn("Unknown action type '$actionType' for switch: $tileId") }
        }
    }

    private fun parseTileIds(args: MutableList<String>, tileId: TileId): Set<TileId> {
        val ret = LinkedHashSet<TileId>(args.size)
        for (arg in args)
            try {
                ret += TileId.from(arg)
            } catch (e: IllegalArgumentException) {
                logger.error("Wrong tileId format for switch: $tileId", e)
            }
        args.clear()
        return ret
    }

    companion object {
        private const val serialVersionUID = 6272627304801242570L

        private val logger = LoggerFactory.getLogger(SimpleSwitchEntityCreator::class.java)

        private const val DISABLE_LASER_ACTION = "DISABLE_LASER"
        private const val ADD_TILES_ACTION = "ADD_TILES"

        private const val ONCE_USE = "ONCE"
        private const val REPEAT_USE = "REPEAT"
    }
}