package secretAgent.io.map.orig.generator.entity

import cz.wa.secretagent.world.entity.Entity
import cz.wa.secretagent.world.entity.EntityType
import cz.wa.secretagent.worldinfo.graphics.EntityInfo
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.slf4j.LoggerFactory
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Creates an entity for some EntityType.
 *
 * @author Ondrej Milenovsky
 */
interface EntityCreator<E : Entity> : Serializable {
    /**
     * Creates entity from parsed information and model.
     * @param args remaining arguments, the list should be emptied or else it will report warnings
     * @param pos position
     * @param tileId tile id
     * @param model model
     * @return new entity
     */
    fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): E?
}

/**
 * Creates entities from parsed info. Has map of creators for each entity type.
 *
 * @author Ondrej Milenovsky
 */
class EntityFactory : TypeEntityCreator<Entity>() {

    fun createEntity(entityInfo: EntityInfo, pos: Vector2D, tileId: TileId, model: ObjectModel): Entity? {
        val args = ArrayList(entityInfo.args)
        val entity = createEntity(args, pos, tileId, model)
        if (args.isNotEmpty())
            logger.warn("Entity definition $tileId has too many arguments, ignoring: $args")
        return entity
    }

    override fun getEnum(arg0: String): Any = EntityType.valueOf(arg0)

    companion object {
        private const val serialVersionUID = 867646705640136421L
        private val logger = LoggerFactory.getLogger(EntityFactory::class.java)
    }
}

/**
 * Creates entity by creator from map retrieved by arg0 as an enum
 * @author Ondrej Milenovsky
 */
abstract class TypeEntityCreator<E : Entity> : EntityCreator<E> {

    lateinit var creators: Map<Any, EntityCreator<E>>

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): E? {
        if (args.isEmpty()) {
            logger.warn("Not enough arguments in entity definition: $tileId")
            return null
        }
        // get subcreator
        val type: Any
        val arg0 = args.removeAt(0)
        try {
            type = getEnum(arg0)
        } catch (e: IllegalArgumentException) {
            logger.error("Unknown type: $arg0", e)
            return null
        }

        // create
        val creator = creators[type]
        return when (creator) {
            null -> null.also { logger.warn("No entity creator for: $type") }
            else -> creator.createEntity(args, pos, tileId, model)
        }
    }

    protected abstract fun getEnum(arg0: String): Any

    companion object {
        private const val serialVersionUID = 3016926627492944449L
        private val logger = LoggerFactory.getLogger(TypeEntityCreator::class.java)
    }
}