package cz.wa.secretagent.worldinfo.graphics;

import secretAgent.world.TileType;

/**
 * Info about tile from a tile set.
 * 
 * @author Ondrej Milenovsky
 */
public class TileInfo {
    private final TileType tileType;
    private final EntityInfo entityInfo;
    private final ModelInfo modelInfo;

    public TileInfo(TileType tileType, EntityInfo entityInfo, ModelInfo modelInfo) {
        this.tileType = tileType;
        this.entityInfo = entityInfo;
        this.modelInfo = modelInfo;
    }

    public TileType getTileType() {
        return tileType;
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public boolean isTile() {
        return tileType != null;
    }
}
