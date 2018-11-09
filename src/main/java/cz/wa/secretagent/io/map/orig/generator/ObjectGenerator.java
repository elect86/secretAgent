package cz.wa.secretagent.io.map.orig.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.Constants;
import cz.wa.secretagent.io.map.orig.generator.entity.EntityFactory;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.model.AnimatedModel;
import cz.wa.secretagent.view.model.SimpleModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.map.AnimatedTile;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.secretagent.worldinfo.graphics.GraphicsInfo;
import cz.wa.secretagent.worldinfo.graphics.ModelInfo;
import cz.wa.secretagent.worldinfo.graphics.TileInfo;
import cz.wa.secretagent.worldinfo.graphics.TilesInfo;
import cz.wa.wautils.math.Vector2I;
import secretAgent.world.ObjectModel;

/**
 * Generates object from tile id. The object can be tile, back tile or entity.
 * Some background tiles might not have linked texture.
 * 
 * @author Ondrej Milenovsky
 */
public class ObjectGenerator {

    /** Set of tile types that are drawn over entities */
    public static final Set<TileType> FRONT_TYPES = new HashSet<TileType>(Arrays.asList(TileType.GHOST_FRONT,
            TileType.SPIKES, TileType.WALL, TileType.WATER));

    private final GraphicsInfo graphicsInfo;
    private final EntityFactory entityFactory;
    private final int tileSetId;
    private final Map<TileId, GeneratedObject> staticTiles;

    public ObjectGenerator(GraphicsInfo graphicsInfo, EntityFactory entityFactory, int tileSetId) {
        this.graphicsInfo = graphicsInfo;
        this.entityFactory = entityFactory;
        this.tileSetId = tileSetId;
        staticTiles = new HashMap<TileId, GeneratedObject>();
    }

    public GeneratedObject generate(Vector2I p, int tileId) {
        if (tileId < 0) {
            return null;
        }
        Vector2D pos = generatePos(p);
        return generateObject(pos, new TileId(tileSetId, tileId));
    }

    /**
     * Creates object (tiles with caching).
     */
    private GeneratedObject generateObject(Vector2D pos, TileId tileId) {
        TilesInfo tileSet = graphicsInfo.getTileSet(tileSetId);
        TileInfo tileInfo = tileSet.getTile(tileId.getTileId());
        if ((tileInfo == null) || tileInfo.isTile()) {
            if (staticTiles.containsKey(tileId)) {
                return staticTiles.get(tileId);
            } else {
                GeneratedObject obj = createTile(tileId, tileInfo);
                staticTiles.put(tileId, obj);
                return obj;
            }
        } else {
            ObjectModel model = graphicsInfo.getTileInfo(tileId).getModelInfo().getModel();
            Entity entity = entityFactory.createEntity(tileInfo.getEntityInfo(), pos, tileId, model);
            if (entity != null) {
                return new GeneratedObject(GeneratedType.ENTITY, entity);
            } else {
                return null;
            }
        }
    }

    /**
     * Creates new object, model can be without linked texture
     */
    private GeneratedObject createTile(TileId tileId, TileInfo tileInfo) {
        if (tileInfo == null) {
            SimpleModel model = new SimpleModel(tileId);
            tileInfo = new TileInfo(TileType.GHOST, null, new ModelInfo(model));
        }

        ModelInfo modelInfo = tileInfo.getModelInfo();
        TileType type = tileInfo.getTileType();
        Tile tile;
        if (modelInfo.isAnimated()) {
            List<TileId> tileIds = ((AnimatedModel) modelInfo.getModel()).getTileIds();
            int frame = tileIds.indexOf(tileId);
            if (frame < 0) {
                frame = 0;
            }
            tile = new AnimatedTile(type, modelInfo.getModel(), frame);
        } else {
            tile = new Tile(type, modelInfo.getModel());
        }
        return new GeneratedObject(getTileDepth(tileId.getTileId(), type), tile);
    }

    private GeneratedType getTileDepth(int tileId, TileType type) {
        if (FRONT_TYPES.contains(type)) {
            return GeneratedType.FOREGROUND;
        } else {
            return GeneratedType.BACKGROUND;
        }
    }

    private Vector2D generatePos(Vector2I p) {
        if (p == null) {
            return null;
        }
        return new Vector2D((p.getX()) * Constants.TILE_SIZE.getX(), (p.getY()) * Constants.TILE_SIZE.getY());
    }
}
