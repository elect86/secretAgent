package secretAgent.view.renderer

/**
 * Tile set id and tile id.
 *
 * @author Ondrej Milenovsky
 */
class TileId(
    val tileSetId: Int,
    val tileId: Int) {

    init {
        assert(tileId >= 0) {"tileId must be >= 0: $tileId"}
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + tileId
        result = prime * result + tileSetId
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        return other is TileId && tileId == other.tileId
    }

    override fun toString(): String = "$tileSetId*$tileId"

    companion object {
        /**
         * Parses the tile id from string. Format is [tileSetId]*[tileId], white spaces are removed, the separator can be '.'
         * @param str the input string
         * @throws IllegalArgumentException if the input string is in wrong format
         * @throws NumberFormatException if any of the ids is not integer
         */
        @JvmStatic
        fun from(str: String): TileId {
            val split = str.filter { it != ' ' }.split(Regex("[*.]"))
            assert(split.size == 2) {"The tile id must have 2 numbers separated by * or .: $str"}
            return TileId(Integer.parseInt(split[0]), Integer.parseInt(split[1]))
        }
    }
}