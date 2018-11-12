//package cz.wa.secretagent.io.map.orig.generator.mapping;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
///**
// * Maps from weird original codes to tile ids.
// *
// * @author Ondrej Milenovsky
// */
//public class TileIdMapper {
//
//    public static final int EMPTY_CODE = 0x20;
//    public static final int TILE_CODE42 = 42;
//
//    public static final Map<Integer, Integer> BG_MAP = TileMapDefinition.getBgMap();
//    public static final TileMap ISLAND_MAP = new TileMap(TileMapDefinition.getIslandMap(), false);
//    public static final TileMap TILE_MAP = new TileMap(TileMapDefinition.getTileMap(), true);
//
//    public TileIdMapper() {
//    }
//
//    /**
//     * Returns list of tiles created by this cell.
//     * Last item from the list is always on current position.
//     * Then the list can contain other items. Each item is relative position and tile id,
//     * that means, there need to be new tiles created.
//     * The items are sorted by lines from left, possible positions are x-3..x, y-2..y,
//     * so a tile can create only tiles left and up from it.
//     *
//     * If the code is 42, then it should be used only when x > 0.
//     *
//     * The list can be empty.
//     */
//    public List<TileMap.Item> mapTile(int origCode, boolean islandMap) {
//        if (origCode == EMPTY_CODE) {
//            return Collections.emptyList();
//        }
//        if (islandMap) {
//            return ISLAND_MAP.getTiles(origCode);
//        } else {
//            return TILE_MAP.getTiles(origCode);
//        }
//    }
//
//    public int mapBackground(int origCode) {
//        if (BG_MAP.containsKey(origCode)) {
//            return BG_MAP.get(origCode);
//        } else {
//            return BG_MAP.get(-1);
//        }
//    }
//}
