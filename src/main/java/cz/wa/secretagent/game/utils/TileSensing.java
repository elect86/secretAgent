//package cz.wa.secretagent.game.utils;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.world.map.TileType;
//import cz.wa.wautils.math.Rectangle2D;
//import cz.wa.wautils.math.Vector2I;
//
///**
// * Class from sensor about a tile.
// *
// * @author Ondrej Milenovsky
// */
//public class TileSensing {
//    private final TileType type;
//    private final Rectangle2D bounds;
//
//    public TileSensing(TileType tileType, Vector2I pos, Vector2D tileSize) {
//        type = tileType;
//        double sizeX = tileSize.getX();
//        double sizeY = tileSize.getY();
//        bounds = new Rectangle2D(-sizeX / 2.0, -sizeY / 2.0, sizeX, sizeY).move(new Vector2D(pos.getX()
//                * sizeX, pos.getY() * sizeY));
//    }
//
//    public TileType getType() {
//        return type;
//    }
//
//    /**
//     * @return moved bounds of the tile
//     */
//    public Rectangle2D getBounds() {
//        return bounds;
//    }
//}
