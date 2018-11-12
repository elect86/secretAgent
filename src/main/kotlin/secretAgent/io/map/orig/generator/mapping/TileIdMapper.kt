package secretAgent.io.map.orig.generator.mapping

/**
 * Maps from weird original codes to tile ids.
 *
 * @author Ondrej Milenovsky
 */
class TileIdMapper {

    /**
     * Returns list of tiles created by this cell.
     * Last item from the list is always on current position.
     * Then the list can contain other items. Each item is relative position and tile id,
     * that means, there need to be new tiles created.
     * The items are sorted by lines from left, possible positions are x-3..x, y-2..y,
     * so a tile can create only tiles left and up from it.
     *
     * If the code is 42, then it should be used only when x > 0.
     *
     * The list can be empty.
     */
    fun mapTile(origCode: Int, islandMap: Boolean): List<TileMap.Item> {
        if (origCode == EMPTY_CODE)
            return emptyList()
        return when {
            islandMap -> ISLAND_MAP.getTiles(origCode)
            else -> TILE_MAP.getTiles(origCode)
        }
    }

    fun mapBackground(origCode: Int): Int = BG_MAP[origCode] ?: BG_MAP[-1]!!

    companion object {

        const val EMPTY_CODE = 0x20
        const val TILE_CODE42 = 42

        val BG_MAP = TileMapDefinition.bgMap
        val ISLAND_MAP = TileMap(TileMapDefinition.islandMap, false)
        val TILE_MAP = TileMap(TileMapDefinition.tileMap, true)
    }
}