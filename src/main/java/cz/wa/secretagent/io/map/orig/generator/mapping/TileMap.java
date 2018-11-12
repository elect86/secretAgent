//package cz.wa.secretagent.io.map.orig.generator.mapping;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cz.wa.wautils.math.Vector2I;
//
///**
// * Mapping for tiles.
// *
// * @author Ondrej Milenovsky
// */
//public class TileMap {
//
//    private static final int SIZE = 256;
//
//    private final List<List<Item>> list;
//    private final Map<Integer, List<Item>> map;
//
//    @SuppressWarnings("unchecked")
//    public TileMap(int[][][] tileMap, boolean asArray) {
//        if (asArray) {
//            map = null;
//            list = new ArrayList<List<Item>>(SIZE);
//            // init with empty lists
//            for (int i = 0; i < SIZE; i++) {
//                list.add(Collections.EMPTY_LIST);
//            }
//            // fill
//            for (int[][] line : tileMap) {
//                int code = line[0][0];
//                List<Item> items = createItems(line[1]);
//                list.set(code, items);
//            }
//        } else {
//            list = null;
//            map = new HashMap<Integer, List<Item>>();
//            // fill
//            for (int[][] line : tileMap) {
//                int code = line[0][0];
//                List<Item> items = createItems(line[1]);
//                map.put(code, items);
//            }
//        }
//    }
//
//    private List<Item> createItems(int[] tiles) {
//        List<Item> list = new ArrayList<Item>(1);
//        for (int dy = 0; dy < 3; dy++) {
//            for (int dx = 0; dx < 4; dx++) {
//                int tileId = tiles[dy * 4 + dx];
//                if (tileId >= 0) {
//                    list.add(new Item(new Vector2I(dx - 3, dy - 2), tileId));
//                }
//            }
//        }
//        return list;
//    }
//
//    public List<Item> getTiles(int tileCode) {
//        if (list != null) {
//            return list.get(tileCode);
//        } else {
//            return map.get(tileCode);
//        }
//    }
//
//    /**
//     * One item in map, contains relative position and tile id.
//     */
//    public static class Item {
//        private final Vector2I pos;
//        private final int tileId;
//
//        public Item(Vector2I pos, int tileId) {
//            this.pos = pos;
//            this.tileId = tileId;
//        }
//
//        public Vector2I getPos() {
//            return pos;
//        }
//
//        public int getTileId() {
//            return tileId;
//        }
//    }
//}
