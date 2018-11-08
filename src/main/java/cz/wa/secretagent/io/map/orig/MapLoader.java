package cz.wa.secretagent.io.map.orig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.io.map.orig.generator.MapGenerator;
import cz.wa.secretagent.io.map.orig.generator.entity.EntityFactory;
import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityComparator;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.bgswitch.SimpleSwitch;
import cz.wa.secretagent.world.entity.bgswitch.SwitchType;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.AddTilesSwitchAction;
import cz.wa.secretagent.world.entity.bgswitch.switchaction.SwitchAction;
import cz.wa.secretagent.world.entity.laser.LaserEntity;
import cz.wa.secretagent.world.entity.laser.RectLaser;
import cz.wa.secretagent.world.entity.projectile.LevelLaserProjectile;
import cz.wa.secretagent.world.entity.projectile.ProjectileType;
import cz.wa.secretagent.world.entity.usable.BuildingUsable;
import cz.wa.secretagent.world.entity.usable.TeleportUsable;
import cz.wa.secretagent.world.entity.usable.UsableEntity;
import cz.wa.secretagent.world.entity.usable.UsableType;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.StoredTile;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.wautils.collection.Array2D;
import cz.wa.wautils.math.Rectangle2D;
import cz.wa.wautils.math.Vector2I;

/**
 * Loads map from file to SAMWorld. 
 * 
 * @author Ondrej Milenovsky
 */
public class MapLoader {
    private static final Logger logger = LoggerFactory.getLogger(MapLoader.class);

    private static final double LEVEL_LASER_DMG = 1000;
    private static final String LEVEL_LASER_MODEL = "levelLaser";
    private static final double LEVEL_LASER_HEIGHT = 4;

    private final File file;
    private final int levelId;
    private final GraphicsInfo graphicsInfo;
    private final SAMGraphics graphics;
    private final EntityFactory entityFactory;

    public MapLoader(File file, int levelId, GraphicsInfo graphicsInfo, SAMGraphics graphics,
            EntityFactory entityFactory) {
        this.file = file;
        this.levelId = levelId;
        this.graphicsInfo = graphicsInfo;
        this.graphics = graphics;
        this.entityFactory = entityFactory;
    }

    public SAMWorld loadMap() throws IOException {
        logger.info("Loading map: " + file.getAbsolutePath());
        MapLevel rawMap = new MapParser(file).parse();
        SAMWorld world = new MapGenerator(rawMap, levelId, graphicsInfo, entityFactory).generateWorld();
        // link textures to ghost tiles that are not defined in GraphicsInfo
        linkLevelTextures(world);
        // postprocess
        storeTiles(world);
        minimizeMap(world.getLevelMap());
        createUsableWalls(world);
        linkTeleports(world.getEntityMap());
        createLasers(world);
        if (levelId == 0) {
            linkBuildings(world);
        }
        return world;
    }

    /**
     * Create lasers from entities.
     */
    private void createLasers(SAMWorld world) {
        EntityMap entityMap = world.getEntityMap();
        // get all lasers and sort them
        List<LevelLaserProjectile> entities = new ArrayList<LevelLaserProjectile>();
        for (Entity entity : new ArrayList<Entity>(entityMap.getEntities(EntityType.PROJECTILE))) {
            if (entity.getSecondType() == ProjectileType.LEVEL_LASER) {
                entities.add((LevelLaserProjectile) entity);
                entityMap.removeEntity(entity);
            }
        }
        Collections.sort(entities, EntityComparator.INSTANCE);

        // create lasers
        LevelLaserProjectile lastBegin = null;
        double lastEnd = 0;
        for (LevelLaserProjectile entity : entities) {
            if (lastBegin == null) {
                lastBegin = entity;
                lastEnd = entity.getPos().getX();
            } else {
                if (lastBegin.getPos().getY() == entity.getPos().getY()) {
                    lastEnd = entity.getPos().getX();
                } else {
                    LaserEntity laser = createLevelLaser(lastBegin.getPos().getX(), lastEnd, lastBegin
                            .getPos().getY(), world.getLevelMap().getTileSize());
                    entityMap.addEntity(laser);
                    lastBegin = entity;
                    lastEnd = entity.getPos().getX();
                }
            }
        }
        if (lastBegin != null) {
            LaserEntity laser = createLevelLaser(lastBegin.getPos().getX(), lastEnd, lastBegin.getPos()
                    .getY(), world.getLevelMap().getTileSize());
            entityMap.addEntity(laser);
        }
    }

    private LaserEntity createLevelLaser(double x1, double x2, double y, Vector2D tileSize) {
        Vector2D pos = new Vector2D((x1 + x2) / 2.0, y);
        double width = x2 - x1 + tileSize.getX();
        double height = LEVEL_LASER_HEIGHT;
        Rectangle2D sizeBounds = new Rectangle2D(-width / 2.0, -height / 2.0, width, height);
        ObjectModel model = graphicsInfo.getModel(LEVEL_LASER_MODEL);
        return new RectLaser(model, pos, null, LEVEL_LASER_DMG, sizeBounds, true);
    }

    /**
     * Sets tile type to WALL to all tiles that are covered by wall usables
     */
    private void createUsableWalls(SAMWorld world) {
        for (Entity entity : world.getEntityMap().getEntities(EntityType.USABLE)) {
            UsableEntity usable = (UsableEntity) entity;
            if (usable.isWall()) {
                createUsableWall(world, usable);
            }
        }
    }

    /**
     * Sets tile type to WALL to all tiles covered by the usable
     */
    private void createUsableWall(SAMWorld world, UsableEntity usable) {
        Rectangle2D bounds = usable.getSizeBounds().move(usable.getPos());
        LevelMap levelMap = world.getLevelMap();
        double sx = levelMap.getTileSize().getX();
        double sy = levelMap.getTileSize().getY();
        int x1 = (int) FastMath.ceil(bounds.getX() / sx);
        int y1 = (int) FastMath.ceil(bounds.getY() / sy);
        int x2 = (int) FastMath.floor(bounds.getX2() / sx);
        int y2 = (int) FastMath.floor(bounds.getY2() / sy);

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                levelMap.getTypes().set(new Vector2I(x, y), TileType.WALL);
            }
        }
    }

    /**
     * Link textures to models.
     */
    private void linkLevelTextures(SAMWorld world) {
        LevelMap map = world.getLevelMap();
        for (Vector2I i : map.getBackground().getIndices()) {
            linkTextures(map.getBackground(), i);
            linkTextures(map.getForeground(), i);
        }
    }

    /**
     * Minimize map memory usage (remove invisible tiles, use empty list singletons)
     */
    private void minimizeMap(LevelMap levelMap) {
        for (Vector2I i : levelMap.getBackground().getIndices()) {
            minimizeTile(levelMap, i);
        }
    }

    /**
     * If there are any tiles that are added by a switch, removes the tiles from map and stores for later use.
     */
    private void storeTiles(SAMWorld world) {
        Set<TileId> tileIds = findIdsToStore(world);

        // remove the tiles from map and store
        LevelMap map = world.getLevelMap();
        for (Vector2I i : map.getBackground().getIndices()) {
            // background
            List<Tile> tiles = removeTiles(map.getBackground().get(i), tileIds);
            // foreground
            tiles.addAll(removeTiles(map.getForeground().get(i), tileIds));
            for (Tile tile : tiles) {
                map.getStoredTiles().add(new StoredTile(i, tile));
            }
            // update type
            if (!tiles.isEmpty()) {
                map.updateType(i);
            }
        }
    }

    /**
     * Removes and returns ids from a cell with model that contain any of the tileIds
     * @param list list of tiles
     * @param tileIds tile ids to remove
     * @return list of removed tiles
     */
    private List<Tile> removeTiles(List<Tile> list, Set<TileId> tileIds) {
        List<Tile> ret = new ArrayList<Tile>(list.size());
        for (Iterator<Tile> it = list.iterator(); it.hasNext();) {
            Tile tile = it.next();
            for (TileId tileId : tileIds) {
                if (tile.hasTileId(tileId)) {
                    ret.add(tile);
                    it.remove();
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Finds all tile ids that will be removed and stored.
     */
    private Set<TileId> findIdsToStore(SAMWorld world) {
        Set<TileId> tileIds = new HashSet<TileId>();
        for (Entity entity : world.getEntityMap().getEntities(EntityType.SWITCH)) {
            if (entity.getSecondType() == SwitchType.SIMPLE) {
                SimpleSwitch swt = (SimpleSwitch) entity;
                // not very good solution
                for (SwitchAction action : swt.getActions()) {
                    if (action instanceof AddTilesSwitchAction) {
                        tileIds.addAll(((AddTilesSwitchAction) action).getTileIds());
                    }
                }
            }
        }
        return tileIds;
    }

    /**
     * Links teleports. Teleports are sorted by lines, the link is cyclic along all of them.
     */
    private void linkTeleports(EntityMap entityMap) {
        // get all teleports and sort them
        Set<Entity> usables = entityMap.getEntities(EntityType.USABLE);
        List<TeleportUsable> teleports = new ArrayList<TeleportUsable>(32);
        for (Entity entity : usables) {
            if (entity.getSecondType() == UsableType.TELEPORT) {
                teleports.add((TeleportUsable) entity);
            }
        }
        Collections.sort(teleports, EntityComparator.INSTANCE);

        // link them
        TeleportUsable last = null;
        for (TeleportUsable teleport : teleports) {
            if (last != null) {
                last.setDestination(teleport.getPos());
            }
            last = teleport;
        }
        if (last != null) {
            last.setDestination(teleports.get(0).getPos());
        }
    }

    /**
     * Links buildings to levels.
     * All final buildings are linked to single level.
     */
    private void linkBuildings(SAMWorld world) {
        // get all buildings and sort them
        Set<Entity> usables = world.getEntityMap().getEntities(EntityType.USABLE);
        List<BuildingUsable> buildings = new ArrayList<BuildingUsable>(32);
        for (Entity entity : usables) {
            if (entity.getSecondType() == UsableType.BUILDING) {
                buildings.add((BuildingUsable) entity);
            }
        }
        Collections.sort(buildings, EntityComparator.INSTANCE);

        // fill level ids
        int i = 0;
        int finalId = -1;
        for (BuildingUsable building : buildings) {
            if (building.isFinalBuilding()) {
                if (finalId > 0) {
                    building.setLevelId(finalId);
                    continue;
                } else {
                    finalId = i + 1;
                }
            }
            i++;
            building.setLevelId(i);
        }
    }

    /**
     * Remove all hidden tiles and replace lists with immutable lists to minimize memory usage.
     * @param levelMap
     * @param p
     */
    private void minimizeTile(LevelMap levelMap, Vector2I p) {
        List<Tile> fg = levelMap.getForeground().get(p);
        List<Tile> bg = levelMap.getBackground().get(p);

        // FOREGROUND
        int lastSolid = findLastSolid(fg);

        // remove invisible tiles
        if (lastSolid > 0) {
            fg = fg.subList(lastSolid, fg.size());
        }
        // create unmodifiable list
        fg = createMinimalList(fg);

        // BACKGROUND
        if (lastSolid >= 0) {
            bg = Collections.emptyList();
        } else {
            lastSolid = findLastSolid(bg);

            // remove invisible tiles
            if (lastSolid > 0) {
                bg = bg.subList(lastSolid, bg.size());
            }
            // create unmodifiable list
            bg = createMinimalList(bg);
        }

        // set back to the map
        levelMap.getBackground().set(p, bg);
        levelMap.getForeground().set(p, fg);
    }

    /**
     * Creates list with minimal memory usage.
     */
    private List<Tile> createMinimalList(List<Tile> list) {
        if (list.isEmpty()) {
            list = Collections.emptyList();
        } else if (list.size() == 1) {
            list = Collections.singletonList(list.get(0));
        }
        return list;
    }

    private int findLastSolid(List<Tile> fg) {
        for (int i = fg.size() - 1; i >= 0; i--) {
            if (!fg.get(i).getModel().isTransparent()) {
                return i;
            }
        }
        return -1;
    }

    private void linkTextures(Array2D<List<Tile>> map, Vector2I i) {
        List<Tile> tiles = map.get(i);
        for (Tile tile : tiles) {
            ObjectModel model = tile.getModel();
            if (!model.hasLinkedTextures()) {
                model.linkTextures(graphics);
            }
        }
    }

}
