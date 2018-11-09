package cz.wa.secretagent.io.map.orig.generator;

import cz.wa.secretagent.io.map.orig.MapLevel;
import cz.wa.secretagent.io.map.orig.generator.entity.EntityFactory;
import cz.wa.secretagent.io.map.orig.generator.mapping.TileIdMapper;
import cz.wa.secretagent.io.map.orig.generator.mapping.TileMap;
import cz.wa.secretagent.io.map.orig.generator.mapping.TileMap.Item;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.model.SimpleModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.wautils.math.Vector2I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import secretAgent.world.SamWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Generates world from parsed map. Takes care of mapping and creating entities with default properties.
 * Some background tiles might not have linked textures.
 * The generated level contains too many lists and the list may contain tiles, that will never be visible, should be postprocessed.
 *
 * @author Ondrej Milenovsky
 */
public class MapGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MapGenerator.class);

    private final MapLevel level;
    private final ObjectGenerator generator;
    private final TileIdMapper mapper;

    private SamWorld world;

    private int levelId;

    public MapGenerator(MapLevel level, int levelId, GraphicsInfo graphicsInfo, EntityFactory entityFactory) {
        this.level = level;
        this.levelId = levelId;
        mapper = new TileIdMapper();
        generator = new ObjectGenerator(graphicsInfo, entityFactory, GraphicsInfo.ORIG_LEVEL_TILES_ID);
    }

    /**
     * Generates the world from parsed original map.
     * @param islandMap if the map is island map with houses or single house
     * @return generated world
     */
    public SamWorld generateWorld() {
        Vector2I size = level.getSize();
        // background tiles
        Tile bgTile = generateBackground();
        List<Item> bgOverTiles = Collections.emptyList(); // mapper.mapTile(level.getBgTileOver());
        world = new SamWorld(size, bgTile, levelId);

        // whole map
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                Vector2I pos = new Vector2I(x, y);

                // reset to background
                LevelMap map = world.getLevelMap();
                map.getBackground().set(pos, new ArrayList<Tile>(Arrays.asList(bgTile)));
                map.getForeground().set(pos, new ArrayList<Tile>(2));
                map.getTypes().set(pos, TileType.GHOST);

                List<Item> tiles;
                int origCode;

                // process back tile
                origCode = level.getTiles()[x][y];
                if (isValidTile(pos, origCode)) {
                    // some tile here
                    tiles = mapper.mapTile(origCode, levelId == 0);
                    generateTiles(pos, tiles);
                } else {
                    // empty tile here, process bg tile
                    if (!bgOverTiles.isEmpty()) {
                        generateTiles(pos, bgOverTiles);
                    }
                }

                // process forward tile
                origCode = level.getOverTiles()[x][y];
                if (isValidTile(pos, origCode)) {
                    tiles = mapper.mapTile(origCode, levelId == 0);
                    generateTiles(pos, tiles);
                }
            }
        }
        return world;
    }

    private boolean isValidTile(Vector2I pos, int origCode) {
        return (origCode != TileIdMapper.EMPTY_CODE)
                && ((origCode != TileIdMapper.TILE_CODE42) || (pos.getX() > 0));
    }

    private void generateTiles(Vector2I pos, List<TileMap.Item> tiles) {
        for (TileMap.Item item : tiles) {
            Vector2I p = pos.add(item.getPos());
            GeneratedObject obj = generator.generate(p, item.getTileId());
            processObject(obj, p);
        }
    }

    private void processObject(GeneratedObject obj, Vector2I pos) {
        if (obj == null) {
            return;
        }
        GeneratedType type = obj.getType();
        if (type == GeneratedType.ENTITY) {
            world.getEntityMap().addEntity((Entity) obj.getObject());
        } else {
            LevelMap map = world.getLevelMap();
            Tile tile = (Tile) obj.getObject();
            // update tile type
            if (tile.getType().isPreferredTo(map.getTypes().get(pos))) {
                map.getTypes().set(pos, tile.getType());
            }
            // insert tile
            if (type == GeneratedType.BACKGROUND) {
                map.getBackground().get(pos).add(tile);
            } else if (type == GeneratedType.FOREGROUND) {
                map.getForeground().get(pos).add(tile);
            }
        }
    }

    private Tile generateBackground() {
        int bgId = mapper.mapBackground(level.getBgTile());
        Object obj = generator.generate(null, bgId).getObject();
        if (obj instanceof Tile) {
            return (Tile) obj;
        } else {
            logger.warn("Background tile is not simple object");
            return new Tile(TileType.GHOST,
                    new SimpleModel(new TileId(GraphicsInfo.ORIG_LEVEL_TILES_ID, bgId)));
        }
    }

}
