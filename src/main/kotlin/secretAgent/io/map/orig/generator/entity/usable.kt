package secretAgent.io.map.orig.generator.entity

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.io.SamIO
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import secretAgent.world.Tile
import secretAgent.world.TileType
import secretAgent.world.entity.*
import java.util.*

/**
 * Generates island map building.
 *
 * @author Ondrej Milenovsky
 */
class BuildingEntityCreator : EntityCreator<BuildingUsable> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): BuildingUsable {
        var finalBuilding = false
        var finishedModel: String? = null
        if (args.isNotEmpty())
            finishedModel = args.removeAt(0)
        else
            logger.warn("BUILDING has no finished model: $tileId")
        if (args.isNotEmpty()) {
            val arg0 = args.removeAt(0)
            if (arg0 == FINAL_BUILDING)
                finalBuilding = true
            else
                logger.warn("Unknown argument for BUILDING $tileId: $arg0")
        }
        return BuildingUsable(model, pos, finishedModel, finalBuilding)
    }

    companion object {
        private const val serialVersionUID = -2735846873194693731L
        private val logger = LoggerFactory.getLogger(BuildingEntityCreator::class.java)
        private const val FINAL_BUILDING = "FINAL"
    }
}

/**
 * Creates door that is opened by a key.
 *
 * @author Ondrej Milenovsky
 */
class DoorEntityCreator : EntityCreator<DoorUsable> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): DoorUsable? {
        if (args.isEmpty()) {
            logger.warn("Not enough arguments for door: $tileId")
        } else {
            val lockType = args.removeAt(0)
            if (lockType.isEmpty())
                logger.warn("Lock type is empty for door: $tileId")
             else
                return DoorUsable(model, pos, lockType)
        }
        return null
    }

    companion object {
        private const val serialVersionUID = 216540582465239681L
        private val logger = LoggerFactory.getLogger(DoorEntityCreator::class.java)
    }
}

/**
 * Creates closed exit door.
 *
 * @author Ondrej Milenovsky
 */
class ExitDoorEntityCreator : EntityCreator<ExitDoorUsable> {

    lateinit var samIO: SamIO

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): ExitDoorUsable {
        var openModel: String? = null
        if (args.isEmpty())
            logger.warn("No open model for exit door: $tileId")
         else
            openModel = args.removeAt(0)
        val replaceTiles = createReplaceTiles(args, tileId)
        return ExitDoorUsable(model, pos, openModel, replaceTiles)
    }

    private fun createReplaceTiles(args: MutableList<String>, tileId: TileId): Map<TileId, Tile> {
        val ret = HashMap<TileId, Tile>(args.size / 2)
        for (i in 0 until args.size / 2)
            try {
                val findTileId = TileId.from(args.removeAt(0))
                val modelName = args.removeAt(0)
                val model = samIO.worldHolder.graphicsInfo.getModel(modelName)
                ret[findTileId] = Tile(TileType.GHOST_FRONT, model)
            } catch (e: IllegalArgumentException) {
                logger.error("Error creating replace tile for OPEN_EXIT action: $tileId", e)
            }
        return ret
    }

    companion object {
        private const val serialVersionUID = 1972964051271447812L
        private val logger = LoggerFactory.getLogger(ExitDoorEntityCreator::class.java)
    }
}

/**
 * Creates open exit door.
 *
 * @author Ondrej Milenovsky
 */
class ExitEntityCreator : EntityCreator<ExitUsable> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): ExitUsable =
            ExitUsable(model, pos)

    companion object {
        private const val serialVersionUID = -5234799660153295525L
    }
}

/**
 * Creates fence blocking final level.
 *
 * @author Ondrej Milenovsky
 */
class FenceEntityCreator : EntityCreator<FenceUsable> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): FenceUsable =
            FenceUsable(model, pos)

    companion object {
        private val serialVersionUID = 8907848311734064349L
    }
}

/**
 * Creates teleport.
 *
 * @author Ondrej Milenovsky
 */
class TeleportEntityCreator : EntityCreator<TeleportUsable> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): TeleportUsable =
            TeleportUsable(model, pos)

    companion object {
        private const val serialVersionUID = -4814593624320559660L
    }
}

/**
 * Creates usable entities.
 *
 * @author Ondrej Milenovsky
 */
class UsableEntityCreator : TypeEntityCreator<UsableEntity>() {

    override fun getEnum(arg0: String): Any = UsableType.valueOf(arg0)

    companion object {
        private const val serialVersionUID = -5004753812757342207L
    }
}

