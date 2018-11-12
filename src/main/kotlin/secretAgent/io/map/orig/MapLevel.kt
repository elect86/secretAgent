package secretAgent.io.map.orig

import cz.wa.wautils.math.Vector2I

/**
 * Single parsed original level in byte format. There should be no nulls in the arrays after generating the map.
 *
 * @author Ondrej Milenovsky
 */
class MapLevel(val size: Vector2I,
               val bgTile: Int,
               val bgTileOver: Int,
               val tiles: Array<IntArray>,
               val overTiles: Array<IntArray>,
               val offsets: IntArray)