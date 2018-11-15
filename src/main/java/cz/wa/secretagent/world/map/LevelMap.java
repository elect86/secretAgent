//package cz.wa.secretagent.world.map;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//
//import cz.wa.secretagent.Constants;
//import cz.wa.wautils.collection.Array2D;
//import cz.wa.wautils.collection.Array2DImpl;
//import cz.wa.wautils.math.Rectangle2I;
//import cz.wa.wautils.math.Vector2I;
//import secretAgent.io.map.orig.generator.ObjectGenerator;
//
///**
// * Level map containing all static tiles.
// * Consists of two arrays of lists of tiles:
// * background - tiles behind entities,
// * foreground - tiles in front of entities
// *
// * Each list consists of one or multiple tiles sorted from behind.
// *
// * The level also contains array of tile types.
// *
// * @author Ondrej Milenovsky
// */
//public class LevelMap {
//
//    private final Vector2I size;
//    private final Array2D<List<Tile>> background;
//    private final Array2D<List<Tile>> foreground;
//    private final Array2D<TileType> types;
//    private final Set<StoredTile> storedTiles;
//    private final Tile backgroundTile;
//    private long timeMs;
//
//    public LevelMap(Vector2I size, Tile backgroundTile) {
//        this.size = size;
//        this.backgroundTile = backgroundTile;
//        Rectangle2I rect = new Rectangle2I(size.getX(), size.getY());
//        foreground = new Array2DImpl<List<Tile>>(rect);
//        background = new Array2DImpl<List<Tile>>(rect);
//        types = new Array2DImpl<TileType>(rect);
//        storedTiles = new LinkedHashSet<StoredTile>();
//    }
//
//    public Array2D<List<Tile>> getForeground() {
//        return foreground;
//    }
//
//    public Array2D<List<Tile>> getBackground() {
//        return background;
//    }
//
//    public Tile getBackgroundTile() {
//        return backgroundTile;
//    }
//
//    public Array2D<TileType> getTypes() {
//        return types;
//    }
//
//    public Set<StoredTile> getStoredTiles() {
//        return storedTiles;
//    }
//
//    public Vector2I getSize() {
//        return size;
//    }
//
//    public Vector2D getTileSize() {
//        return Constants.TILE_SIZE;
//    }
//
//    public long getTimeMs() {
//        return timeMs;
//    }
//
//    public void addTime(long timeDMs) {
//        timeMs += timeDMs;
//    }
//
//    /**
//     * Add new tile to the map
//     * @param tile tile to add
//     */
//    public void addTile(StoredTile tile) {
//        if (ObjectGenerator.FRONT_TYPES.contains(tile.getTile().getType())) {
//            addTile(foreground, tile, true);
//        } else {
//            addTile(background, tile, true);
//        }
//        Vector2I pos = tile.getPos();
//        TileType type = types.get(pos);
//        if (tile.getTile().getType().isPreferredTo(type)) {
//            types.set(pos, tile.getTile().getType());
//        }
//    }
//
//    /**
//     * Adds tile to the array, the cell can contain unmodifiable list if size <= 1
//     * @param array array where to add
//     * @param tile tile to add
//     */
//    private void addTile(Array2D<List<Tile>> array, StoredTile tile, boolean addLast) {
//        List<Tile> list = array.get(tile.getPos());
//        if (list.isEmpty()) {
//            list = Collections.singletonList(tile.getTile());
//        } else if (list.size() == 1) {
//            Tile lastTile = list.get(0);
//            list = new ArrayList<Tile>(3);
//            if (addLast) {
//                list.add(lastTile);
//                list.add(tile.getTile());
//            } else {
//                list.add(lastTile);
//                list.add(tile.getTile());
//            }
//        } else {
//            if (addLast) {
//                list.add(tile.getTile());
//            } else {
//                list.add(0, tile.getTile());
//            }
//        }
//        array.set(tile.getPos(), list);
//    }
//
//    /**
//     * Update type at this position.
//     * @param i position
//     */
//    public void updateType(Vector2I i) {
//        TileType type = null;
//        for (Tile tile : background.get(i)) {
//            if (tile.getType().isPreferredTo(type)) {
//                type = tile.getType();
//            }
//        }
//        for (Tile tile : foreground.get(i)) {
//            if (tile.getType().isPreferredTo(type)) {
//                type = tile.getType();
//            }
//        }
//        types.set(i, type);
//    }
//
//    /**
//     * Removes the tile at the position. If the first tile in background is not the background tile,
//     * adds background tile here.
//     * @param pos position
//     * @param tile tile to remove
//     * @throws IllegalArgumentException if the tile is not in the map
//     */
//    public void removeTile(Vector2I pos, Tile tile) {
//        boolean removed = tryRemoveTile(pos, tile, background);
//        if (!removed) {
//            removed = tryRemoveTile(pos, tile, foreground);
//        }
//        if (!removed) {
//            throw new IllegalArgumentException("Tile " + tile.getType() + " is not at " + pos);
//        }
//        List<Tile> bgList = background.get(pos);
//        if (bgList.isEmpty() || (bgList.get(0) != backgroundTile)) {
//            addTile(background, new StoredTile(pos, backgroundTile), false);
//        }
//    }
//
//    private boolean tryRemoveTile(Vector2I pos, Tile tile, Array2D<List<Tile>> array) {
//        List<Tile> list = array.get(pos);
//        if ((list.size() == 1) && (list.get(0) == tile)) {
//            list = Collections.emptyList();
//            array.set(pos, list);
//            return true;
//        }
//        for (Iterator<Tile> it = list.iterator(); it.hasNext();) {
//            Tile tile2 = it.next();
//            if (tile2 == tile) {
//                it.remove();
//                if (list.size() == 1) {
//                    list = Collections.singletonList(list.get(0));
//                    array.set(pos, list);
//                }
//                return true;
//
//            }
//        }
//        return false;
//    }
//
//    public Vector2I getNearestTilePos(Vector2D pos) {
//        int x = (int) FastMath.round(pos.getX() / getTileSize().getX());
//        int y = (int) FastMath.round(pos.getY() / getTileSize().getY());
//        return new Vector2I(x, y);
//    }
//}
