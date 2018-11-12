package secretAgent.io.map.orig

import cz.wa.secretagent.world.EntityMap
import cz.wa.secretagent.world.entity.EntityComparator
import cz.wa.secretagent.world.entity.EntityType
import cz.wa.secretagent.world.entity.bgswitch.SimpleSwitch
import cz.wa.secretagent.world.entity.bgswitch.SwitchType
import cz.wa.secretagent.world.entity.bgswitch.switchaction.AddTilesSwitchAction
import cz.wa.secretagent.world.entity.laser.LaserEntity
import cz.wa.secretagent.world.entity.laser.RectLaser
import cz.wa.secretagent.world.entity.projectile.LevelLaserProjectile
import cz.wa.secretagent.world.entity.projectile.ProjectileType
import cz.wa.secretagent.world.entity.usable.BuildingUsable
import cz.wa.secretagent.world.entity.usable.TeleportUsable
import cz.wa.secretagent.world.entity.usable.UsableEntity
import cz.wa.secretagent.world.entity.usable.UsableType
import cz.wa.secretagent.world.map.LevelMap
import cz.wa.secretagent.world.map.StoredTile
import cz.wa.secretagent.world.map.Tile
import cz.wa.secretagent.world.map.TileType
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo
import cz.wa.wautils.collection.Array2D
import cz.wa.wautils.math.Rectangle2D
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import org.slf4j.LoggerFactory
import secretAgent.io.map.orig.generator.MapGenerator
import secretAgent.io.map.orig.generator.entity.EntityFactory
import secretAgent.view.SamGraphics
import secretAgent.view.renderer.TileId
import secretAgent.world.SamWorld
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Loads map from file to SAMWorld.
 *
 * @author Ondrej Milenovsky
 */
class MapLoader(private val file: File,
                private val levelId: Int,
                private val graphicsInfo: GraphicsInfo,
                private val graphics: SamGraphics,
                private val entityFactory: EntityFactory) {

    @Throws(IOException::class)
    fun loadMap(): SamWorld {
        logger.info("Loading map: " + file.absolutePath)
        val rawMap = MapParser(file).parse()
        val world = MapGenerator(rawMap, levelId, graphicsInfo, entityFactory).generateWorld()
        // link textures to ghost tiles that are not defined in GraphicsInfo
        linkLevelTextures(world)
        // postprocess
        storeTiles(world)
        minimizeMap(world.levelMap)
        createUsableWalls(world)
        linkTeleports(world.entityMap)
        createLasers(world)
        if (levelId == 0)
            linkBuildings(world)
        return world
    }

    /**
     * Create lasers from entities.
     */
    private fun createLasers(world: SamWorld) {
        val entityMap = world.entityMap
        // get all lasers and sort them
        val entities = ArrayList<LevelLaserProjectile>()
        for (entity in ArrayList(entityMap.getEntities(EntityType.PROJECTILE)))
            if (entity.secondType == ProjectileType.LEVEL_LASER) {
                entities.add(entity as LevelLaserProjectile)
                entityMap.removeEntity(entity)
            }
        Collections.sort(entities, EntityComparator.INSTANCE)

        // create lasers
        var lastBegin: LevelLaserProjectile? = null
        var lastEnd = 0.0
        for (entity in entities) {
            if (lastBegin == null) {
                lastBegin = entity
                lastEnd = entity.pos.x
            } else {
                if (lastBegin.pos.y == entity.pos.y)
                    lastEnd = entity.pos.x
                else {
                    val laser = createLevelLaser(lastBegin.pos.x, lastEnd, lastBegin.pos.y, world.levelMap.tileSize)
                    entityMap.addEntity(laser)
                    lastBegin = entity
                    lastEnd = entity.pos.x
                }
            }
        }
        if (lastBegin != null) {
            val laser = createLevelLaser(lastBegin.pos.x, lastEnd, lastBegin.pos.y, world.levelMap.tileSize)
            entityMap.addEntity(laser)
        }
    }

    private fun createLevelLaser(x1: Double, x2: Double, y: Double, tileSize: Vector2D): LaserEntity {
        val pos = Vector2D((x1 + x2) / 2.0, y)
        val width = x2 - x1 + tileSize.x
        val height = LEVEL_LASER_HEIGHT
        val sizeBounds = Rectangle2D(-width / 2.0, -height / 2.0, width, height)
        val model = graphicsInfo.getModel(LEVEL_LASER_MODEL)
        return RectLaser(model, pos, null, LEVEL_LASER_DMG, sizeBounds, true)
    }

    /**
     * Sets tile type to WALL to all tiles that are covered by wall usables
     */
    private fun createUsableWalls(world: SamWorld) {
        for (entity in world.entityMap.getEntities(EntityType.USABLE)) {
            val usable = entity as UsableEntity
            if (usable.isWall)
                createUsableWall(world, usable)
        }
    }

    /**
     * Sets tile type to WALL to all tiles covered by the usable
     */
    private fun createUsableWall(world: SamWorld, usable: UsableEntity) {
        val bounds = usable.sizeBounds.move(usable.pos)
        val levelMap = world.levelMap
        val sx = levelMap.tileSize.x
        val sy = levelMap.tileSize.y
        val x1 = FastMath.ceil(bounds.x / sx).toInt()
        val y1 = FastMath.ceil(bounds.y / sy).toInt()
        val x2 = FastMath.floor(bounds.x2 / sx).toInt()
        val y2 = FastMath.floor(bounds.y2 / sy).toInt()

        for (y in y1..y2)
            for (x in x1..x2)
                levelMap.types.set(Vector2I(x, y), TileType.WALL)
    }

    /**
     * Link textures to models.
     */
    private fun linkLevelTextures(world: SamWorld) {
        val map = world.levelMap
        for (i in map.background.indices) {
            linkTextures(map.background, i)
            linkTextures(map.foreground, i)
        }
    }

    /**
     * Minimize map memory usage (remove invisible tiles, use empty list singletons)
     */
    private fun minimizeMap(levelMap: LevelMap) {
        for (i in levelMap.background.indices)
            minimizeTile(levelMap, i)
    }

    /**
     * If there are any tiles that are added by a switch, removes the tiles from map and stores for later use.
     */
    private fun storeTiles(world: SamWorld) {
        val tileIds = findIdsToStore(world)

        // remove the tiles from map and store
        val map = world.levelMap
        for (i in map.background.indices) {
            // background
            val tiles = removeTiles(map.background.get(i), tileIds)
            // foreground
            tiles += removeTiles(map.foreground.get(i), tileIds)
            for (tile in tiles)
                map.storedTiles.add(StoredTile(i, tile))
            // update type
            if (tiles.isNotEmpty())
                map.updateType(i)
        }
    }

    /**
     * Removes and returns ids from a cell with model that contain any of the tileIds
     * @param list list of tiles
     * @param tileIds tile ids to remove
     * @return list of removed tiles
     */
    private fun removeTiles(list: MutableList<Tile>, tileIds: Set<TileId>): MutableList<Tile> {
        val ret = ArrayList<Tile>(list.size)
        val it = list.iterator()
        while (it.hasNext()) {
            val tile = it.next()
            for (tileId in tileIds)
                if (tile.hasTileId(tileId)) {
                    ret += tile
                    it.remove()
                    break
                }
        }
        return ret
    }

    /**
     * Finds all tile ids that will be removed and stored.
     */
    private fun findIdsToStore(world: SamWorld): Set<TileId> {
        val tileIds = HashSet<TileId>()
        for (entity in world.entityMap.getEntities(EntityType.SWITCH)) {
            if (entity.secondType == SwitchType.SIMPLE) {
                val swt = entity as SimpleSwitch
                // not very good solution
                for (action in swt.actions)
                    if (action is AddTilesSwitchAction)
                        tileIds += action.tileIds
            }
        }
        return tileIds
    }

    /**
     * Links teleports. Teleports are sorted by lines, the link is cyclic along all of them.
     */
    private fun linkTeleports(entityMap: EntityMap) {
        // get all teleports and sort them
        val usables = entityMap.getEntities(EntityType.USABLE)
        val teleports = ArrayList<TeleportUsable>(32)
        for (entity in usables)
            if (entity.secondType == UsableType.TELEPORT)
                teleports.add(entity as TeleportUsable)
        Collections.sort(teleports, EntityComparator.INSTANCE)

        // link them
        var last: TeleportUsable? = null
        for (teleport in teleports) {
            last?.destination = teleport.pos
            last = teleport
        }
        last?.destination = teleports[0].pos
    }

    /**
     * Links buildings to levels.
     * All final buildings are linked to single level.
     */
    private fun linkBuildings(world: SamWorld) {
        // get all buildings and sort them
        val usables = world.entityMap.getEntities(EntityType.USABLE)
        val buildings = ArrayList<BuildingUsable>(32)
        for (entity in usables)
            if (entity.secondType == UsableType.BUILDING)
                buildings += entity as BuildingUsable
        Collections.sort(buildings, EntityComparator.INSTANCE)

        // fill level ids
        var i = 0
        var finalId = -1
        for (building in buildings) {
            if (building.isFinalBuilding) {
                if (finalId > 0) {
                    building.levelId = finalId
                    continue
                } else
                    finalId = i + 1
            }
            i++
            building.levelId = i
        }
    }

    /**
     * Remove all hidden tiles and replace lists with immutable lists to minimize memory usage.
     * @param levelMap
     * @param p
     */
    private fun minimizeTile(levelMap: LevelMap, p: Vector2I) {
        var fg = levelMap.foreground.get(p)
        var bg = levelMap.background.get(p)

        // FOREGROUND
        var lastSolid = findLastSolid(fg)

        // remove invisible tiles
        if (lastSolid > 0)
            fg = fg.subList(lastSolid, fg.size)
        // create unmodifiable list
        fg = createMinimalList(fg)

        // BACKGROUND
        if (lastSolid >= 0)
            bg = emptyList()
        else {
            lastSolid = findLastSolid(bg)

            // remove invisible tiles
            if (lastSolid > 0)
                bg = bg.subList(lastSolid, bg.size)
            // create unmodifiable list
            bg = createMinimalList(bg)
        }

        // set back to the map
        levelMap.background.set(p, bg)
        levelMap.foreground.set(p, fg)
    }

    /** Creates list with minimal memory usage.     */
    private fun createMinimalList(list: List<Tile>): List<Tile> = when {
        list.isEmpty() -> emptyList()
        list.size == 1 -> listOf(list[0])
        else -> list
    }

    private fun findLastSolid(fg: List<Tile>): Int {
        for (i in fg.indices.reversed())
            if (!fg[i].model.isTransparent)
                return i
        return -1
    }

    private fun linkTextures(map: Array2D<List<Tile>>, i: Vector2I) {
        val tiles = map.get(i)
        for (tile in tiles) {
            val model = tile.model
            if (!model.hasLinkedTextures())
                model.linkTextures(graphics)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MapLoader::class.java)

        private const val LEVEL_LASER_DMG = 1000.0
        private const val LEVEL_LASER_MODEL = "levelLaser"
        private const val LEVEL_LASER_HEIGHT = 4.0
    }
}