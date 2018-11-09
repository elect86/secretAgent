//package cz.wa.secretagent.world;
//
//import java.util.Set;
//
//import cz.wa.secretagent.view.SAMGraphics;
//import cz.wa.secretagent.view.TileId;
//import cz.wa.wautils.math.Rectangle2D;
//
///**
// * Model of tile or entity. Can be any class, the renderer must be able to process it.
// *
// * @author Ondrej Milenovsky
// */
//public interface ObjectModel {
//
//    /**
//     * @return set of all tile ids that the model uses
//     */
//    Set<TileId> getAllTileIds();
//
//    /**
//     * Link textures from the graphics (must be performed before rendering)
//     * @param graphics
//     */
//    void linkTextures(SAMGraphics graphics);
//
//    /**
//     * @return if the textures are linked
//     */
//    boolean hasLinkedTextures();
//
//    /**
//     * @return if the model has a transparent pixel in any texture
//     */
//    boolean isTransparent();
//
//    /**
//     * @return max visible bounds relative to center
//     */
//    Rectangle2D getMaxBounds();
//}
