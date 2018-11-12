package secretAgent.io.map.orig.generator.mapping

import cz.wa.wautils.math.Vector2I
import java.util.*
import kotlin.collections.ArrayList

/**
 * Mapping for tiles.
 *
 * @author Ondrej Milenovsky
 */
class TileMap(tileMap: Array<Pair<Int, IntArray>>, asArray: Boolean) {

    private val list: ArrayList<List<Item>>?
    private val map: MutableMap<Int, List<Item>>?

    init {
        if (asArray) {
            map = null
            list = ArrayList(SIZE)
            // init with empty lists
            for (i in 0 until SIZE)
                list += listOf<Item>()
            // fill
            for (line in tileMap) {
                val code = line.first
                val items = createItems(line.second)
                list[code] = items
            }
        } else {
            list = null
            map = HashMap()
            // fill
            for (line in tileMap) {
                val code = line.first
                val items = createItems(line.second)
                map[code] = items
            }
        }
    }

    private fun createItems(tiles: IntArray): List<Item> {
        val list = ArrayList<Item>(1)
        for (dy in 0..2)
            for (dx in 0..3) {
                val tileId = tiles[dy * 4 + dx]
                if (tileId >= 0)
                    list += Item(Vector2I(dx - 3, dy - 2), tileId)
            }
        return list
    }

    fun getTiles(tileCode: Int): List<Item> = list?.get(tileCode) ?: map!![tileCode]!!

    /** One item in map, contains relative position and tile id.     */
    class Item(val pos: Vector2I, val tileId: Int)

    companion object {
        private const val SIZE = 256
    }
}