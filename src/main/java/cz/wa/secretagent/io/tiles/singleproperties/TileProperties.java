package cz.wa.secretagent.io.tiles.singleproperties;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.world.map.TileType;

/**
 * Info about single tile. Tile can be static tile (part of map) or entity, always is one null of tileType and entityInfo.
 * If no model is specified, then it is simple model.
 * 
 * @author Ondrej Milenovsky
 */
public class TileProperties {
    private final TileType tileType;
    private final EntityProperties entityPr;
    private final String modelRef;

    public TileProperties(TileType tileType, String modelRef) {
        Validate.notNull(tileType, "tileType is null");
        Validate.isTrue(tileType != TileType.ENTITY, "tileType must not be ENTITY");
        this.tileType = tileType;
        this.entityPr = null;
        this.modelRef = modelRef;
    }

    public TileProperties(EntityProperties entityPr, String modelRef) {
        Validate.notNull(entityPr, "entityPr is null");
        this.tileType = null;
        this.entityPr = entityPr;
        this.modelRef = modelRef;
    }

    public TileType getTileType() {
        return tileType;
    }

    public EntityProperties getEntityProperties() {
        return entityPr;
    }

    public String getModelRef() {
        return modelRef;
    }

    public boolean isTile() {
        return tileType != null;
    }

}
