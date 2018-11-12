//package cz.wa.secretagent.io.tiles;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cz.wa.secretagent.Constants;
//import cz.wa.secretagent.io.tiles.singleproperties.EntityProperties;
//import cz.wa.secretagent.io.tiles.singleproperties.ModelProperties;
//import cz.wa.secretagent.io.tiles.singleproperties.TileProperties;
//import cz.wa.secretagent.world.map.TileType;
//import cz.wa.wautils.io.AbstractParser;
//import cz.wa.wautils.math.Vector2I;
//import cz.wa.wautils.string.StringUtilsWa;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.ModelType;
//
///**
// * Parses tiles info file. For each tile id there is tile info,
// * that contains model and map tile or entity definition.
// *
// * @author Ondrej Milenovsky
// */
//public class TilesPropertiesParser extends AbstractParser {
//    private static final String COMMENT_START = "//";
//
//    private static final String TILE_SIZE_TEXT = "tileSize";
//    private static final char TILE_SIZE_SEP = '*';
//
//    private static final String ID_LIST_SEP_REGEX = "[\\s,]+";
//    private static final char ID_LIST_SEP = ',';
//    private static final char ID_RANGE_SEP = '-';
//    private static final char ID_SEP = '*';
//    private static final char ID_SEP_ALT = '.';
//
//    private static final String MODEL_DEF_START = "{";
//    private static final String MODEL_DEF_END = "}";
//    private static final char MODEL_TYPE_SEP = '.';
//    private static final char MODEL_PROPERTY_SEP = '=';
//
//    private static final char ENTITY_DEF_START = '(';
//    private static final char ENTITY_DEF_END = ')';
//    private static final String ENTITY_ARG_SEP = ",";
//
//    public static final Vector2I DEFAULT_TILE_SIZE = new Vector2I(16, 16);
//
//    private final File file;
//    private final int tileSetId;
//    private Map<TileId, TileProperties> tiles;
//    private Map<String, ModelProperties> models;
//    private Vector2I tileSize;
//
//    private ModelProperties currModel;
//
//    public TilesPropertiesParser(File file, int tileSetId) {
//        super(Constants.ENCODING);
//        this.file = file;
//        this.tileSetId = tileSetId;
//    }
//
//    public TilesProperties parse(boolean onlyModels) throws IOException {
//        tiles = new HashMap<TileId, TileProperties>();
//        models = new HashMap<String, ModelProperties>();
//        tileSize = null;
//        startParsing(file);
//
//        if (currModel != null) {
//            warn("Not closed model definition: " + currModel.getName());
//            closeCurrModel();
//        }
//
//        if (!onlyModels && (tileSize == null)) {
//            warn("Missing tile size definition, used default 16*16");
//            tileSize = DEFAULT_TILE_SIZE;
//        }
//        return new TilesProperties(tileSize, tiles, models);
//    }
//
//    @Override
//    protected boolean parseLine(String line) {
//        line = line.trim();
//        // remove comment
//        int cInd = line.indexOf(COMMENT_START);
//        if (cInd >= 0) {
//            line = line.substring(0, cInd);
//        }
//        if (line.isEmpty()) {
//            return true;
//        }
//
//        // process rest
//        if (line.startsWith(TILE_SIZE_TEXT)) {
//            if (tileSize != null) {
//                warn("Duplicate tile size definition");
//            }
//            tileSize = parseTileSize(line);
//        } else if (Character.isDigit(line.charAt(0))) {
//            parseTiles(line);
//        } else {
//            parseModel(line);
//        }
//        return true;
//    }
//
//    private void parseModel(String line) {
//        if (currModel == null) {
//            if (line.endsWith(MODEL_DEF_START)) {
//                line = line.substring(0, line.length() - MODEL_DEF_START.length()).trim();
//                startCurrModel(line);
//            } else {
//                warn("Model definition must end with " + MODEL_DEF_START);
//            }
//        } else {
//            if (line.equals(MODEL_DEF_END)) {
//                closeCurrModel();
//            } else {
//                parseModelProperty(line);
//            }
//        }
//    }
//
//    private void startCurrModel(String line) {
//        int ind = line.indexOf(MODEL_TYPE_SEP);
//        if (ind < 0) {
//            warn("Model definition must consist of name and type separated by: " + MODEL_TYPE_SEP);
//            currModel = new ModelProperties(line, null);
//            return;
//        }
//        String name = line.substring(0, ind).trim();
//        String typeStr = line.substring(ind + 1).trim();
//        ModelType type = null;
//        try {
//            type = ModelType.valueOf(typeStr);
//        } catch (IllegalArgumentException e) {
//            error("Wrong model type: " + typeStr, e);
//        }
//        currModel = new ModelProperties(name, type);
//    }
//
//    private void parseModelProperty(String line) {
//        int ind = line.indexOf(MODEL_PROPERTY_SEP);
//        if (ind < 0) {
//            warn("No property definition, missing " + MODEL_PROPERTY_SEP);
//            return;
//        }
//        String name = line.substring(0, ind).trim();
//        String value = line.substring(ind + 1).trim();
//        if (currModel.getProperties().containsKey(name)) {
//            warn("Duplicate model '" + currModel.getName() + "' property: " + name);
//        }
//        List<TileId> ids = parseIds(value);
//        if ((ids != null) && !ids.isEmpty()) {
//            currModel.getProperties().put(name, ids);
//        }
//    }
//
//    private void closeCurrModel() {
//        models.put(currModel.getName(), currModel);
//        currModel = null;
//    }
//
//    private void parseTiles(String line) {
//        // tile type
//        TileType tileType = null;
//        int beginTypeI = -1;
//        int endTypeI = -1;
//        for (int i = 0; i <= line.length(); i++) {
//            if (beginTypeI < 0) {
//                if ((i < line.length()) && isEnumChar(line.charAt(i))) {
//                    beginTypeI = i;
//                }
//            } else if ((i == line.length()) || !isEnumChar(line.charAt(i))) {
//                endTypeI = i;
//                String typeStr = line.substring(beginTypeI, i);
//                try {
//                    tileType = TileType.valueOf(typeStr);
//                    break;
//                } catch (IllegalArgumentException e) {
//                    warn("Unknown tile type: '" + typeStr + "'");
//                    return;
//                }
//            }
//        }
//        if (tileType == null) {
//            warn("Tile type not specified");
//            return;
//        }
//
//        // model
//        String modelRef = line.substring(endTypeI).trim();
//
//        // there could be entity, cut the definition
//        int ind = modelRef.indexOf(ENTITY_DEF_END);
//        if (ind > 0) {
//            modelRef = modelRef.substring(ind + 1).trim();
//        }
//
//        if (modelRef.isEmpty()) {
//            modelRef = null;
//        }
//
//        // tile ids
//        String tileIds = line.substring(0, beginTypeI).trim();
//        TileProperties tilePr = null;
//        if (tileType == TileType.ENTITY) {
//            EntityProperties entityPr = parseEntity(line.substring(endTypeI));
//            if (entityPr != null) {
//                tilePr = new TileProperties(entityPr, modelRef);
//            }
//        } else {
//            tilePr = new TileProperties(tileType, modelRef);
//        }
//
//        // store
//        if (tilePr != null) {
//            for (TileId tileId : parseIds(tileIds)) {
//                if (tileId.getTileSetId() == tileSetId) {
//                    putTileInfo(tileId, tilePr);
//                } else {
//                    warn("Definition tile id must be from this set: " + tileId);
//                }
//            }
//        }
//    }
//
//    private EntityProperties parseEntity(String line) {
//        // separate arguments
//        int ind1 = line.indexOf(ENTITY_DEF_START);
//        int ind2 = line.indexOf(ENTITY_DEF_END);
//        if (ind1 < 0) {
//            warn("Missing entity arguments opening  character: " + ENTITY_DEF_START);
//            return null;
//        }
//        if (ind2 < 0) {
//            warn("Missing entity arguments closing character: " + ENTITY_DEF_END);
//            return null;
//        }
//        line = line.substring(ind1 + 1, ind2).trim();
//
//        // parse arguments
//        List<String> args = StringUtilsWa.split(line, ENTITY_ARG_SEP);
//        for (int i = 0; i < args.size(); i++) {
//            args.set(i, args.get(i).trim());
//        }
//        return new EntityProperties(args);
//    }
//
//    private boolean isEnumChar(char c) {
//        return (c == '_') || (!Character.isWhitespace(c) && Character.isUpperCase(c));
//    }
//
//    private List<TileId> parseIds(String str) {
//        // find second -
//        int rangeInd = str.indexOf(ID_RANGE_SEP, 1);
//        // parse ids
//        if ((str.indexOf(ID_LIST_SEP) < 0) && (rangeInd > 0)) {
//            return parseRangeIds(str, rangeInd);
//        } else {
//            return parseListIds(str);
//        }
//    }
//
//    private List<TileId> parseListIds(String str) {
//        List<TileId> ret = new ArrayList<TileId>();
//        for (String idStr : str.split(ID_LIST_SEP_REGEX)) {
//            if (!idStr.isEmpty()) {
//                ret.add(parseTileId(idStr));
//            }
//        }
//        return ret;
//    }
//
//    private List<TileId> parseRangeIds(String str, int rangeSepInd) {
//        String num1 = str.substring(0, rangeSepInd).trim();
//        String num2 = str.substring(rangeSepInd + 1).trim();
//        List<TileId> ret = new ArrayList<TileId>();
//        // parse range
//        TileId id1 = parseTileId(num1);
//        TileId id2 = parseTileId(num2);
//        if (id1.getTileSetId() != id2.getTileSetId()) {
//            warn("Both tile ids from range must have same tile set id: " + id1 + ", " + id2);
//        } else {
//            // process
//            for (int i = id1.getTileId(); i <= id2.getTileId(); i++) {
//                ret.add(new TileId(id1.getTileSetId(), i));
//            }
//        }
//        return ret;
//    }
//
//    private TileId parseTileId(String str) {
//        try {
//            int ind = str.indexOf(ID_SEP);
//            if (ind < 0) {
//                ind = str.indexOf(ID_SEP_ALT);
//            }
//            if (ind >= 0) {
//                return TileId.from(str);
//            } else {
//                int id = Integer.decode(str);
//                if (id >= 0) {
//                    return new TileId(tileSetId, id);
//                } else {
//                    warn("Tile id must be >= 0: " + id);
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            error("Wrong id format: " + str, e);
//        }
//        return null;
//    }
//
//    private void putTileInfo(TileId tileId, TileProperties tileInfo) {
//        if (tiles.put(tileId, tileInfo) != null) {
//            warn("Duplicate tile id, rewriting: " + tileId);
//        }
//    }
//
//    private Vector2I parseTileSize(String line) {
//        String s = line.substring(TILE_SIZE_TEXT.length());
//        int ind = s.indexOf(TILE_SIZE_SEP);
//        if (ind < 0) {
//            warn("Wrong tile size definition, missing '" + TILE_SIZE_SEP + "': " + s);
//            return null;
//        }
//        String num1 = s.substring(0, ind).trim();
//        String num2 = s.substring(ind + 1).trim();
//        try {
//            int x = Integer.parseInt(num1);
//            int y = Integer.parseInt(num2);
//            if ((x <= 0) || (y <= 0)) {
//                warn("Tile size must be > 0: " + s);
//                return null;
//            }
//            return new Vector2I(x, y);
//        } catch (NumberFormatException e) {
//            error("Wrong number format: " + s, e);
//            return null;
//        }
//    }
//
//}
